package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.InputStockFile;
import tsi.too.model.InputStock;

public class InputStockController {
	private final InputStockFile inputStockFile;

	private static final String fileName = "inputStock.dat";

	private static InputStockController instance;

	private InputStockController() throws FileNotFoundException {
		inputStockFile = new InputStockFile(fileName);
	}

	public static InputStockController getInstance() throws FileNotFoundException {
		synchronized (InputStockController.class) {
			if (instance == null)
				instance = new InputStockController();

			return instance;
		}
	}

	public void insert(InputStock iStock) throws IOException {
		inputStockFile.writeAtEnd(iStock);
	}

	public List<InputStock> fetchAll() throws IOException {
		return inputStockFile.readAllFile();
	}

	public List<InputStock> fetchByInput(long inputId) throws IOException {
		return new ArrayList<>(inputStockFile.readAllFile(p -> p.getInputId() == inputId));
	}

	/**
	 * Retrieves all price entries for an input before a given
	 * {@link LocalDateTime}.
	 *
	 * @param inputId the target input id.
	 * @param date    the target {@link LocalDate}.
	 * @return a list with the entries.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<InputStock> fetchByProductBeforeDate(long inputId, LocalDate date) throws IOException {
		return fetchByInput(inputId).stream().filter(pr -> pr.getDate().isBefore(date)).collect(Collectors.toList());
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
	public List<InputStock> fetchByPeriod(long inputId, LocalDate startDateInclusive, LocalDate endDateInclusive)
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
	 * Retrieves the last price entry for the input for a given date. If there is no
	 * price entry, an empty entry will be returned.
	 *
	 * @param inputId       the target input id
	 * @param dateInclusive the target date inclusive.
	 * @return the last price entry if found, an empty price entry if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public InputStock fetchPricePerDate(long inputId, LocalDate dateInclusive) throws IOException {
		var all = fetchByProductBeforeDate(inputId, dateInclusive);

		all.sort(Comparator.comparing(InputStock::getDate).reversed());

		try {
			return all.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
