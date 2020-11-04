package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.Patterns;
import tsi.too.model.InputEntry;

public class InputStockFile extends BinaryFile<InputEntry> {
	private static final String FILE_NAME = "inputStock.dat";
	private static InputStockFile instance;
	
	private InputStockFile() throws FileNotFoundException {
		openFile(FILE_NAME, OpenMode.READ_WRITE);
	}
	
	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static InputStockFile getInstance() throws FileNotFoundException {
		synchronized (InputStockFile.class) {
			if(instance == null)
				instance = new InputStockFile();
			
			return instance;			
		}
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // entryId
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

	/**
	 * Retrieves the last {@link InputEntry} id registered in the file.
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