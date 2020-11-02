package tsi.too.io;

import static tsi.too.model.Input.MAX_NAME_LENGTH;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.Input;
import tsi.too.util.Pair;

public class ProductionInputsFile extends BinaryFile<Input> {

	private final static String NAME = "ProdctionInputs.dat";

	private static ProductionInputsFile instance;

	private ProductionInputsFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	public static ProductionInputsFile getInstance() throws FileNotFoundException {
		synchronized (ProductionInputsFile.class) {
			if (instance == null)
				instance = new ProductionInputsFile();

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
	
	public Input findById(long id) throws IOException {
		long high = recordSize();
		long low = 0;
		Input current;

		while (low <= high) {
			long middle = (low + high) / 2;
			seekRecord(middle);
			current = read();

			if (id == current.getId())
				return current;

			if (id < current.getId()) {
				high = middle - 1;
			} else {
				low = middle + 1;
			}
		}
		
		return null;
	}
	
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

	public Pair<Input, Long> findByName(final String name) throws IOException {
		return fetch( p -> p.getName().equalsIgnoreCase(name));
	}
}