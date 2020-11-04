package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.Patterns;
import tsi.too.model.InputEntry;

public class InputStockFile extends BinaryFile<InputEntry> {

	public InputStockFile(String fileName) throws FileNotFoundException {
		openFile(fileName, OpenMode.READ_WRITE);
	}

	@Override
	public int recordSize() {
		return
				Long.BYTES + // entryid
				Long.BYTES + // inputId
				Double.BYTES + // incoming
				Double.BYTES + // price
				Double.BYTES + // available
				Patterns.BRAZILIAN_DATE_PATTERN.length() * Character.BYTES // date
		;
	}

	@Override
	public void write(InputEntry e) throws IOException {
		file.writeLong(e.getId());
		file.writeLong(e.getInputId());
		file.writeDouble(e.getPrice());
		file.writeDouble(e.getIncoming());
		file.writeDouble(e.getAvailable());
		writeLocalDate(e.getDate(), Patterns.BRAZILIAN_DATE_PATTERN);
	}

	@Override
	public InputEntry read() throws IOException {
		var id = file.readLong();
		var inputId = file.readLong();
		var price = file.readDouble();
		var incoming = file.readDouble();
		var available = file.readDouble();
		var date = readDate(Patterns.BRAZILIAN_DATE_PATTERN);

		return new InputEntry(id, inputId, incoming, price, available, date);
	}
	
	public long getLastId() throws IOException {
		var numberOfRecords = countRecords();

		if (numberOfRecords == 0)
			return 0;

		return read(numberOfRecords - 1).getId();
	}
}