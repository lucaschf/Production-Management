package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.MeasureUnity;
import tsi.too.model.Product;
import tsi.too.util.Pair;

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

	private ProductFile(String name) throws FileNotFoundException {
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
		file.writeLong(e.getId());
		writeString(e.getName(), Product.MAX_NAME_LENGTH);
		file.writeInt(e.getMeasureUnity().getCode());
		file.writeDouble(e.getPercentageProfitMargin());
	}

	@Override
	public Product read() throws IOException {
		return new Product(file.readLong(), readString(Product.MAX_NAME_LENGTH), MeasureUnity.from(file.readInt()),
				file.readDouble());
	}

	public long getLastId() throws IOException {
		var numberOfRecords = countRecords();

		if (numberOfRecords == 0)
			return 0;

		return read(numberOfRecords - 1).getId();
	}
	
	public Pair<Product, Long> findByName(final String name) throws IOException {
		seekRecord(0);
		
		Product product;
		
		for(long i = 0; i < countRecords(); i++) {
			product = read();
			
			if(product.getName().equalsIgnoreCase(name))
				return new Pair<>(product, i);
		}
		
		return null;
	}
}