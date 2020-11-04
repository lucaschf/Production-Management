package tsi.too.io;

import static tsi.too.model.Input.MAX_NAME_LENGTH;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.Input;

public class InputsFile extends BinaryFile<Input> {

	private final static String NAME = "Inputs.dat";

	private static InputsFile instance;

	private InputsFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static InputsFile getInstance() throws FileNotFoundException {
		synchronized (InputsFile.class) {
			if (instance == null)
				instance = new InputsFile();

			return instance;
		}
	}

	@Override
	public int recordSize() {
		return MAX_NAME_LENGTH * Character.BYTES // name
				+ Double.BYTES // quantity
				+ Long.BYTES // code
				+ Double.BYTES // price
		;
	}

	@Override
	public void write(Input e) throws IOException {
		writeString(e.getName(), MAX_NAME_LENGTH);
		file.writeDouble(e.getQuantity());
		file.writeLong(e.getId());
		file.writeDouble(e.getPrice());
	}

	/**
	 * Retrieves the last {@link Input} id registered in the file.
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

	@Override
	public Input read() throws IOException {
		return new Input(readString(MAX_NAME_LENGTH), file.readDouble(), file.readLong(), file.readDouble());
	}
}