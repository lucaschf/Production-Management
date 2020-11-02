package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.Patterns;
import tsi.too.model.InputStock;

public class InputStockFile extends BinaryFile<InputStock> {

	public InputStockFile(String fileName) throws FileNotFoundException {
		openFile(fileName, OpenMode.READ_WRITE);
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // inputId
				Double.BYTES + // incoming
				Double.BYTES + // price
				Double.BYTES + // available
				Patterns.BRAZILIAN_DATE_PATTERN.length() * Character.BYTES // date
		;
	}

	@Override
	public void write(InputStock e) throws IOException {
		file.writeLong(e.getInputId());
		file.writeDouble(e.getPrice());
		file.writeDouble(e.getIncoming());
		file.writeDouble(e.getAvailable());
		writeLocalDate(e.getDate(), Patterns.BRAZILIAN_DATE_PATTERN);
	}

	@Override
	public InputStock read() throws IOException {
		var inputId = file.readInt();
		var price = file.readDouble();
		var incoming = file.readDouble();
		var available = file.readDouble();
		var date = readDate(Patterns.BRAZILIAN_DATE_PATTERN);

		return new InputStock(inputId, incoming, price, available, date);
	}
}