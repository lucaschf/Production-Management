package tsi.too.io;

import tsi.too.model.ProductInput;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProductInputFile extends BinaryFile<ProductInput> {

    private static final String NAME = "ProductInputsRelation.dat";

    private static ProductInputFile instance;

    /**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
    public static ProductInputFile getInstance() throws FileNotFoundException {
        synchronized (ProductInputFile.class) {
            if (instance == null)
                instance = new ProductInputFile();

            return instance;
        }
    }

    private ProductInputFile() throws FileNotFoundException {
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
}