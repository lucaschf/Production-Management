package tsi.too.io;

import java.io.IOException;

import tsi.too.model.Product;

public class ProducFile extends BinaryFile<Product>{

	@Override
	public int recordSize() {
		
		
		return 0;
	}

	@Override
	public void write(Product e) throws IOException {
		
		
	}

	@Override
	public Product read() throws IOException {
		
		return null;
	}	
}
