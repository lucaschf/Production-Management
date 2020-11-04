package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_PATTERN;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.Production;

public class ProductionFile extends BinaryFile<Production> {
	private static final String FILE_NAME = "Production.dat";

	private static ProductionFile instance;

	private ProductionFile() throws FileNotFoundException {
		openFile(FILE_NAME, OpenMode.READ_WRITE);
	}

	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static ProductionFile getInstance() throws FileNotFoundException {
		synchronized (ProductionFile.class) {
			if (instance == null)
				instance = new ProductionFile();

			return instance;
		}
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // id
				Long.BYTES + // product id
				Double.BYTES + // amountProduced
				BRAZILIAN_DATE_PATTERN.length() * Character.BYTES + // date
				Double.BYTES + // unitaryManufacturingCost
				Double.BYTES + // unitarySaleValue
				Double.BYTES // available
		;
	}

	@Override
	public void write(Production e) throws IOException {
		file.writeLong(e.getId());
		file.writeLong(e.getProductId());
		file.writeDouble(e.getAmountProduced());
		writeLocalDate(e.getDate(), BRAZILIAN_DATE_PATTERN);
		file.writeDouble(e.getUnitaryManufacturingCost());
		file.writeDouble(e.getUnitarySaleValue());
		file.writeDouble(e.getAvailable());
	}

	@Override
	public Production read() throws IOException {
		var id = file.readLong();
		var productId = file.readLong();
		var amountProduced = file.readDouble();
		var date = readDate(BRAZILIAN_DATE_PATTERN);
		var unitaryManufacturingCost = file.readDouble();
		var unitarySaleValue = file.readDouble();
		var available = file.readDouble();

		return new Production(id, productId, amountProduced, date, unitaryManufacturingCost, unitarySaleValue,
				available);
	}

	/**
	 * Retrieves the last {@link Production} id registered in the file.
	 * 
	 * @return the last id or {@code 0} if the file has no records.
	 * @throws IOException if an I / O error occurs.
	 */
	public long getLastId() throws IOException {
		var numberOfRecords = countRecords();

		if (numberOfRecords == 0)
			return 0;

		return read(numberOfRecords - 1).getId();
	}
}