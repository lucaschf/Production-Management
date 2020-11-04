package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_PATTERN;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.Production;

public class ProductionFile extends BinaryFile<Production> {

	public ProductionFile(String fileName) throws FileNotFoundException {
		openFile(fileName, OpenMode.READ_WRITE);
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