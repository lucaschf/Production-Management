package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tsi.too.model.ProductInputRelation;
import tsi.too.util.Pair;

public class InputsByProdcutFile extends BinaryFile<ProductInputRelation> {

	private static final String NAME = "ProductInputsRelation.dat";

	private static InputsByProdcutFile instance;

	public static InputsByProdcutFile getInstance() throws FileNotFoundException {
		synchronized (InputsByProdcutFile.class) {
			if (instance == null)
				instance = new InputsByProdcutFile();

			return instance;
		}
	}

	public InputsByProdcutFile() throws FileNotFoundException {
		openFile(NAME, OpenMode.READ_WRITE);
	}

	@Override
	public int recordSize() {
		return Long.BYTES * 2 + Integer.BYTES;
	}

	@Override
	public void write(ProductInputRelation e) throws IOException {
		file.writeLong(e.getProductId());
		file.writeLong(e.getInputId());
		file.writeInt(e.getQuantity());
	}

	@Override
	public ProductInputRelation read() throws IOException {
		return new ProductInputRelation(file.readLong(), file.readLong(), file.readInt());
	}

	public List<ProductInputRelation> findByProductId(long productId) throws IOException {
		var items = new ArrayList<ProductInputRelation>();
		ProductInputRelation item;
		seekRecord(0);

		for (long i = 0; i < recordSize(); i++) {
			item = read();

			if (item.getProductId() == productId)
				items.add(item);
		}

		return items;
	}

	public Pair<ProductInputRelation, Long> findByProductAndInput(long productId, long inputId) throws IOException{
		ProductInputRelation item;

		for (long i = 0; i < recordSize(); i++) {
			item = read(i);

			if (item.getProductId() == productId && item.getInputId() == inputId)
				return new Pair<ProductInputRelation, Long>(item, i);
		}

		return null;
	}
	
	public void updateQuantity(ProductInputRelation p) {
			
	}
}