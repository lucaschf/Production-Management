package tsi.too.io;

import static tsi.too.model.ProductionInput.MAX_NAME_LENGTH;

import java.io.IOException;

import tsi.too.model.ProductionInput;

public class ProductionInputsFile extends BinaryFile<ProductionInput> {

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
		file.writeLong(e.getCode());
		file.writeDouble(e.getPrice());
	}

	@Override
	public ProductionInput read() throws IOException {
		return new ProductionInput(readString(MAX_NAME_LENGTH), file.readInt(), file.readLong(), file.readDouble());
	}
}