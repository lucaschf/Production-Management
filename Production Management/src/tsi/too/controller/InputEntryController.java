package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.exception.InsufficientStockException;
import tsi.too.io.InputStockFile;
import tsi.too.model.InputEntry;
import tsi.too.model.ProductInput;
import tsi.too.util.Pair;

public class InputEntryController {
	private final InputStockFile inputStockFile;

	private static final String fileName = "inputStock.dat";

	private static InputEntryController instance;
	private InputController inputController;

	private InputEntryController() throws FileNotFoundException {
		inputStockFile = new InputStockFile(fileName);
		inputController = InputController.getInstance();
	}

	public static InputEntryController getInstance() throws FileNotFoundException {
		synchronized (InputEntryController.class) {
			if (instance == null)
				instance = new InputEntryController();

			return instance;
		}
	}

	public void insert(InputEntry iStock) throws IOException {
		inputStockFile.writeAtEnd(iStock.withId(inputStockFile.getLastId() + 1));
	}

	public List<InputEntry> fetchAll() throws IOException {
		return inputStockFile.readAllFile();
	}

	public List<Pair<String, InputEntry>> fetchPaired() throws IOException {
		var entries = inputStockFile.readAllFile();
		var result = new ArrayList<Pair<String, InputEntry>>();
		
		String name;
		
		for(InputEntry entry : entries) {
			try {
				name = inputController.findById(entry.getInputId()).getFirst().getName();
			}catch (Exception e) {
				name = "";
			}
			result.add(new Pair<String, InputEntry>(name, entry));
		}

		return result;
	}

	public List<InputEntry> fetchByInput(long inputId) throws IOException {
		return new ArrayList<>(inputStockFile.readAllFile(p -> p.getInputId() == inputId));
	}

	/**
	 * Retrieves all price entries for an input before a given
	 * {@link LocalDateTime}.
	 *
	 * @param inputId       the target input id.
	 * @param dateInclusive the target {@link LocalDate}.
	 * @return a list with the entries.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<InputEntry> fetchByProductBeforeDate(long inputId, LocalDate dateInclusive) throws IOException {
		return fetchByInput(inputId).stream()
				.filter(pr -> pr.getDate().isBefore(dateInclusive) || pr.getDate().isEqual(dateInclusive))
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves all prices entries for a given input in a given period.
	 *
	 * @param inputId            the target input id;
	 * @param startDateInclusive the start date.
	 * @param endDateInclusive   the end date.
	 * @return a list with the entries.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<InputEntry> fetchByPeriod(long inputId, LocalDate startDateInclusive, LocalDate endDateInclusive)
			throws IOException {
		if (startDateInclusive.isAfter(endDateInclusive))
			return new ArrayList<>();

		return fetchByInput(inputId).stream().filter(t -> {
			LocalDate d = LocalDate.from(t.getDate());

			return (d.equals(startDateInclusive) || d.equals(endDateInclusive))
					|| (d.isBefore(endDateInclusive) && d.isAfter(startDateInclusive));
		}).collect(Collectors.toList());
	}

	/**
	 * Retrieves the last entry for the input for a given date. If there is no
	 * entry, an empty entry will be returned.
	 *
	 * @param inputId       the target input id
	 * @param dateInclusive the target date inclusive.
	 * @return the last input entry if found, an empty entry if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public InputEntry fetchWithLastPrice(long inputId, LocalDate dateInclusive) throws IOException {
		var all = fetchByProductBeforeDate(inputId, dateInclusive);

		all.sort(Comparator.comparing(InputEntry::getDate).reversed());

		try {
			return all.get(0);
		} catch (IndexOutOfBoundsException e) {
			return new InputEntry(inputId, 0, 0, dateInclusive);
		}
	}

	public Pair<Double, List<InputEntry>> reserveForCheckOut(List<ProductInput> items, double forQuantity,
			LocalDate date) throws IOException, InsufficientStockException {
		var totalCost = 0.0;
		var checkingOut = new ArrayList<InputEntry>();

		for (ProductInput i : items) {
			var checking = reserveForCheckOut(i, forQuantity, date);
			checkingOut.addAll(checking.getSecond());
			totalCost += checking.getFirst() / checking.getSecond().size();
		}

		return new Pair<Double, List<InputEntry>>(totalCost, checkingOut);
	}

	private Pair<Double, List<InputEntry>> reserveForCheckOut(ProductInput item, double forQuantity, LocalDate date)
			throws IOException, InsufficientStockException {
		var stock = fetchByProductBeforeDate(item.getInputId(), date);

		double totalNeeded = item.getInputQuantity() * forQuantity;
		var totalInStock = stock.stream().mapToDouble(InputEntry::getAvailable).sum();

		if (totalInStock < totalNeeded)
			throw new InsufficientStockException();

		stock.sort(Comparator.comparing(InputEntry::getDate));

		var checkingOut = new ArrayList<InputEntry>();

		double totalChecked = 0.0;
		double totalValue = 0.0;

		for (InputEntry entry : stock) {
			var needed = totalNeeded - totalChecked;

			if (needed > 0) {
				if (needed > entry.getAvailable()) {
					totalValue += entry.getAvailable() * entry.getPrice();
					totalChecked += entry.getAvailable();
					checkingOut.add(entry.minus(entry.getAvailable()));
				} else {
					totalChecked += needed;
					totalValue += needed * entry.getPrice();
					checkingOut.add(entry.minus(needed));
				}
			} else
				break;
		}

		return new Pair<Double, List<InputEntry>>(totalValue, checkingOut);
	}

	public double fetchLastPriceEntry(long inputId) throws IOException {
		var all = inputStockFile.readAllFile(p -> p.getInputId() == inputId);
		all.sort(Comparator.comparing(InputEntry::getDate).reversed());

		try {
			return all.get(0).getPrice();
		} catch (Exception e) {
			return 0;
		}
	}

	public Pair<InputEntry, Long> fetchById(long id) throws IOException {
		return inputStockFile.fetch(p -> p.getId() == id);
	}

	public void update(Long position, InputEntry newData) throws IOException {
		inputStockFile.update(position, newData);
	}
}
