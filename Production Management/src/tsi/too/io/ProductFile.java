package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;

public class ProductFile extends BinaryFile<Product> {

	private final static String NAME = "products.dat";

	private static ProductFile instance;

	private ProductFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	public static ProductFile getInstance() throws FileNotFoundException {
		synchronized (ProductFile.class) {
			if (instance == null)
				instance = new ProductFile();

			return instance;
		}
	}

	@Override
	public int recordSize() {
		return Long.BYTES + // code
				Product.MAX_NAME_LENGTH * Character.BYTES + // name
				Integer.BYTES + // measureUnity
				Double.BYTES + // profitMargin
				Double.BYTES // size
		;
	}

	@Override
	public void write(Product e) throws IOException {
		file.writeLong(e.getId());
		writeString(e.getName(), Product.MAX_NAME_LENGTH);
		file.writeInt(e.getMeasureUnity().getCode());
		file.writeDouble(e.getPercentageProfitMargin());
		file.writeDouble(e.getSize());
	}

	@Override
	public Product read() throws IOException {
		return new Product(file.readLong(), readString(Product.MAX_NAME_LENGTH), MeasureUnity.from(file.readInt()),
				file.readDouble(), file.readDouble());
	}

	/**
	 * Retrieves the last {@link Product} id registered in the file.
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