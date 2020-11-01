package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_TIME_PATTERN;

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
				Double.BYTES + // quantity
				BRAZILIAN_DATE_TIME_PATTERN.length() * Character.BYTES + // date
				Double.BYTES + // production cost
				Double.BYTES // production sale value
				;
	}

	@Override
	public void write(Production e) throws IOException {
		file.writeLong(e.getProductId());
		file.writeDouble(e.getQuantity());
		writeLocalDateTime(e.getDate().atStartOfDay(), BRAZILIAN_DATE_TIME_PATTERN);
		file.writeDouble(e.getManufacturingCost());
		file.writeDouble(e.getTotalSaleValue());
	}

	@Override
	public Production read() throws IOException {
		var productId = file.readLong();
		var quantity = file.readDouble();
		var date = readDateTime(BRAZILIAN_DATE_TIME_PATTERN).toLocalDate();
		var manufacturingCost = file.readDouble();
		var totalSaleValue = file.readDouble();
		
		return new Production(productId, quantity, date, manufacturingCost, totalSaleValue);
	}
}