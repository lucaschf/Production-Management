package tsi.too.controller;

import tsi.too.exception.InsufficientStockException;
import tsi.too.io.InputStockFile;
import tsi.too.model.Input;
import tsi.too.model.InputEntry;
import tsi.too.model.ProductInput;
import tsi.too.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InputEntryController {
	private final InputStockFile inputStockFile;

	private static InputEntryController instance;
	private final InputController inputController;

	private InputEntryController() throws FileNotFoundException {
		inputStockFile = InputStockFile.getInstance();
		inputController = InputController.getInstance();
	}

	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static InputEntryController getInstance() throws FileNotFoundException {
		synchronized (InputEntryController.class) {
			if (instance == null)
				instance = new InputEntryController();

			return instance;
		}
	}

	/**
	 * Generates an id for the {@link InputEntry} and inserts it to the file.
	 * 
	 * @param entry the {@link InputEntry} to be inserted.
	 * @throws IOException                if an I / O error occurs.
	 * @throws CloneNotSupportedException If cloning the {@code entry} with the new
	 *                                    id fails.
	 */
	public void insert(InputEntry entry) throws IOException, CloneNotSupportedException {
		inputStockFile.writeAtEnd(entry.withId(inputStockFile.getLastId() + 1));
	}

	/**
	 * Fetch all recorded {@link InputEntry} paired with the {@link Input} name.
	 * 
	 * @return a list with all paired {@link InputEntry}.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Pair<String, InputEntry>> fetchPaired() throws IOException {
		var entries = inputStockFile.readAllFile();
		var result = new ArrayList<Pair<String, InputEntry>>();

		String name;

		for (InputEntry entry : entries) {
			try {
				name = inputController.findById(entry.getInputId()).getFirst().getName();
			} catch (Exception e) {
				name = "";
			}
			result.add(new Pair<>(name, entry));
		}

		return result;
	}

	/**
	 * Fetch all recorded {@link InputEntry} for a {@link Input} based on its id.
	 * 
	 * @param inputId the target input id.
	 * @return a list with all {@link InputEntry}.
	 * @throws IOException if an I / O error occurs.
	 */
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
	 * Reserves production inputs for stock withdraw.
	 * 
	 * @param items      the requested {@link ProductInput} list.
	 * @param multiplier the amount of each {@link ProductInput} that should be
	 *                   withdrawn.
	 * @param date       the withdrawn date
	 * @return A {@link Pair} containing the total value of the withdrawn and a list
	 *         of the {@link InputEntry} with the new available values after the
	 *         withdrawn.
	 *         <p>
	 *         <b>WARNING:</b>This method does not performs the withdrawn, if a
	 *         withdrawn is required, use {@link #update(List) for the list of the
	 * returned Pair.</p>
	 * @throws IOException                if an I / O error occurs.
	 * @throws InsufficientStockException if any of the request {@link ProductInput}
	 *                                    has no sufficient stock.
	 */
	public Pair<Double, List<InputEntry>> reserveForWithdraw(List<ProductInput> items, double multiplier,
			LocalDate date) throws IOException, InsufficientStockException {
		var totalCost = 0.0;
		var checkingOut = new ArrayList<InputEntry>();

		for (ProductInput i : items) {
			var checking = reserveForWithdraw(i, multiplier, date);
			checkingOut.addAll(checking.getSecond());
			totalCost += checking.getFirst() / checking.getSecond().size();
		}

		return new Pair<>(totalCost, checkingOut);
	}

	/**
	 * Reserve a {@link ProductInput} for stock withdraw.
	 * 
	 * @param item       the requested {@link ProductInput}
	 * @param multiplier the amount of each {@link ProductInput} that should be
	 *                   withdrawn.
	 * @param date       the withdrawn date
	 * @return A {@link Pair} containing the total value of the withdrawn and a list
	 *         of the {@link InputEntry} with the new available values after the
	 *         withdrawn.
	 *         <p>
	 *         <b>WARNING:</b>This method does not performs the withdrawn, if a
	 *         withdrawn is required, use {@link #update(List) for the list of the
	 * @throws IOException                if an I / O error occurs.
	 * @throws InsufficientStockException if the request {@link ProductInput} has no
	 *                                    sufficient stock.
	 */
	private Pair<Double, List<InputEntry>> reserveForWithdraw(ProductInput item, double multiplier, LocalDate date)
			throws IOException, InsufficientStockException {
		var stock = fetchByProductBeforeDate(item.getInputId(), date);

		double totalNeeded = item.getInputQuantity() * multiplier;
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

		return new Pair<>(totalValue, checkingOut);
	}

	/**
	 * Fetch an {@link InputEntry} and its position in file.
	 * 
	 * @param id the target id.
	 * @return A {@link Pair} with the found {@link InputEntry} and its position in file, null if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public Pair<InputEntry, Long> fetchById(long id) throws IOException {
		return inputStockFile.fetch(p -> p.getId() == id);
	}

	/**
	 * Performs a loop updating all the entries in file.
	 * 
	 * @param entries the target entries.
	 * @throws IOException if an I / O error occurs.
	 */
	public void update(List<InputEntry> entries) throws IOException {
		Objects.requireNonNull(entries);

		for (InputEntry entry : entries) {
			var p = fetchById(entry.getId());
			inputStockFile.update(p.getSecond(), entry);
		}
	}
}
