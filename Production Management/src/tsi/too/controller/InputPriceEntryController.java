package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.PriceEntryFile;
import tsi.too.model.PriceEntry;

public class InputPriceEntryController {
	private static final String fileName = "InputPriceEntries.dat";

	private final PriceEntryFile inputPriceFile;

	private static InputPriceEntryController instance;

	private InputPriceEntryController() throws FileNotFoundException {
		inputPriceFile = new PriceEntryFile(fileName);
	}

	public static InputPriceEntryController getInstance() throws FileNotFoundException {
		synchronized (InputPriceEntryController.class) {
			if (instance == null)
				instance = new InputPriceEntryController();

			return instance;
		}
	}

	public void insert(PriceEntry entry) throws IOException {
		inputPriceFile.writeAtEnd(entry);
	}

	public List<PriceEntry> fetchAll() throws IOException {
		return inputPriceFile.readAllFile();
	}

	public List<PriceEntry> fetchByInput(long inputId) throws IOException {
		return new ArrayList<>(inputPriceFile.readAllFile(p -> p.getItemId() == inputId));
	}

	/**
	 * Retrieves all price entries for an input before a given
	 * {@link LocalDateTime}.
	 *
	 * @param inputId the target input id.
	 * @param date    the target {@link LocalDateTime}.
	 * @return a list with the entries.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<PriceEntry> fetchByProductBeforeDateTime(long inputId, LocalDateTime date) throws IOException {
		return fetchByInput(inputId).stream().filter(pr -> pr.getDate().isBefore(date)).collect(Collectors.toList());
	}

	/**
	 * Retrieves all prices entries for a given input before a give
	 * {@link LocalDate}
	 *
	 * @param inputId the target input id.
	 * @param date    the target {@link LocalDate}.
	 * @return a list with the entries.
	 * @throws IOException if an I / O occurs.
	 */
	public List<PriceEntry> fetchByProductBeforeDate(long inputId, LocalDate date) throws IOException {
		return fetchByProductBeforeDateTime(inputId, LocalDateTime.from(date.plusDays(1).atStartOfDay()));
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
	public List<PriceEntry> fetchByPeriod(long inputId, LocalDate startDateInclusive, LocalDate endDateInclusive)
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
	public PriceEntry fetchPricePerDate(long inputId, LocalDate dateInclusive) throws IOException {
		var all = fetchByProductBeforeDate(inputId, dateInclusive);

		all.sort(Comparator.comparing(PriceEntry::getDate).reversed());

		try {
			return all.get(0);
		} catch (IndexOutOfBoundsException e) {
			return new PriceEntry(inputId, 0);
		}
	}
}
