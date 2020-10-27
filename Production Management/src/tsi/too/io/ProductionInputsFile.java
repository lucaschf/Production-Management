package tsi.too.io;

import static tsi.too.model.ProductionInput.MAX_NAME_LENGTH;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.ProductionInput;

public class ProductionInputsFile extends BinaryFile<ProductionInput> {

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
				+ Integer.BYTES // quantity
				+ Long.BYTES // code
				+ Double.BYTES // price
		;
	}

	@Override
	public void write(ProductionInput e) throws IOException {
		writeString(e.getName(), MAX_NAME_LENGTH);
		file.writeInt(e.getQuantity());
		file.writeLong(e.getId());
		file.writeDouble(e.getPrice());
	}
	
	public ProductionInput findById(long id) throws IOException {
		long high = recordSize();
		long low = 0;
		ProductionInput current;

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

	@Override
	public ProductionInput read() throws IOException {
		return new ProductionInput(readString(MAX_NAME_LENGTH), file.readInt(), file.readLong(), file.readDouble());
	}
}