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
		synchronized (ProductionFile.class){
			if(instance == null)
				instance = new ProductionFile();

			return instance;
		}
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // product id
				Double.BYTES + // amountProduced
				BRAZILIAN_DATE_PATTERN.length() * Character.BYTES + // date
				Double.BYTES + // unitaryManufacturingCost
				Double.BYTES + // unitarySaleValue
				Double.BYTES // available
		;
	}

	@Override
	public void write(Production e) throws IOException {
		file.writeLong(e.getProductId());
		file.writeDouble(e.getAmountProduced());
		writeLocalDate(e.getDate(), BRAZILIAN_DATE_PATTERN);
		file.writeDouble(e.getUnitaryManufacturingCost());
		file.writeDouble(e.getUnitarySaleValue());
		file.writeDouble(e.getAvailable());
	}

	@Override
	public Production read() throws IOException {
		var productId = file.readLong();
		var amountProduced = file.readDouble();
		var date = readDate(BRAZILIAN_DATE_PATTERN);
		var unitaryManufacturingCost = file.readDouble();
		var unitarySaleValue = file.readDouble();
		var available = file.readDouble();

		return new Production(productId, amountProduced, date, unitaryManufacturingCost,
				unitarySaleValue, available);
	}
}