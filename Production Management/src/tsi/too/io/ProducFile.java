package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;

public class ProducFile extends BinaryFile<Product> {

	private final static String NAME = "products.dat";

	private static ProducFile instance;

	private ProducFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	public static ProducFile getInstance() throws FileNotFoundException {
		synchronized (ProducFile.class) {
			if(instance == null)
				instance = new ProducFile();
			
			return instance;
		}
	}

	public ProducFile(String name) throws FileNotFoundException {
		openFile(name, OpenMode.READ_WRITE);
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // code
				Product.MAX_NAME_LENGTH * Character.BYTES + // name
				Integer.BYTES + // measureUnity
				Double.BYTES // profitMargin
		;
	}

	@Override
	public void write(Product e) throws IOException {
		file.writeLong(e.getCode());
		writeString(e.getName(), Product.MAX_NAME_LENGTH);
		file.writeInt(e.getMeasureUnity().getCode());
		file.writeDouble(e.getPercentageProfitMargin());
	}

	@Override
	public Product read() throws IOException {
		return new Product(file.readLong(), readString(Product.MAX_NAME_LENGTH), MeasureUnity.from(file.readInt()),
				file.readDouble());
	}
}