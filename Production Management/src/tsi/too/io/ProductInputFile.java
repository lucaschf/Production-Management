package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsi.too.model.ProductInput;
import tsi.too.util.Pair;

public class ProductInputFile extends BinaryFile<ProductInput> {

	private static final String NAME = "ProductInputsRelation.dat";

	private static ProductInputFile instance;

	public static ProductInputFile getInstance() throws FileNotFoundException {
		synchronized (ProductInputFile.class) {
			if (instance == null)
				instance = new ProductInputFile();

			return instance;
		}
	}

	public ProductInputFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	@Override
	public int recordSize() {
		return Long.BYTES * 2 // ids
				+ Double.BYTES // quantity
		;
	}

	@Override
	public void write(ProductInput e) throws IOException {
		file.writeLong(e.getProductId());
		file.writeLong(e.getInputId());
		file.writeDouble(e.getInputQuantity());
	}

	@Override
	public ProductInput read() throws IOException {
		return new ProductInput(file.readLong(), file.readLong(), file.readDouble());
	}

	public List<ProductInput> findByProductId(long productId) throws IOException {
		var items = new ArrayList<ProductInput>();
		ProductInput item;
		seekRecord(0);

		for (long i = 0; i < recordSize(); i++) {
			item = read();

			if (item.getProductId() == productId)
				items.add(item);
		}

		return items;
	}

	public Pair<ProductInput, Long> findByProductAndInput(long productId, long inputId) throws IOException {
		ProductInput item;

		for (long i = 0; i < recordSize(); i++) {
			item = read(i);

			if (item.getProductId() == productId && item.getInputId() == inputId)
				return new Pair<ProductInput, Long>(item, i);
		}

		return null;
	}
}