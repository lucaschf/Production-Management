package tsi.too.controller;

import tsi.too.Constants;
import tsi.too.io.InputDialog.InputValidator;
import tsi.too.io.ProductFile;
import tsi.too.model.Product;
import tsi.too.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ProductController {
	ProductFile productFile;
	private static ProductController instance;

	public final InputValidator<String> productNameValidator = input -> {
		if (input == null || input.isBlank())
			return Constants.NAME_CANNOT_BE_BLANK;
		if (input.length() < 4)
			return Constants.NAME_IS_TO_SHORT;

		return null;
	};

	private ProductController() throws FileNotFoundException {
		this.productFile = ProductFile.getInstance();
	}

	public static ProductController getInstance() throws FileNotFoundException {
		synchronized (ProductController.class) {
			if (instance == null)
				instance = new ProductController();

			return instance;
		}
	}

	public List<Product> fetchProducts() throws IOException {
		return productFile.readAllFile();
	}
	
	public Vector<Product> fetchProductsAsVector() throws IOException {
		return productFile.readAllFileAsVector();
	}

	public Pair<Product, Long> findByName(final String name) throws IOException {
		return productFile.findByName(name);
	}

	public void insert(Product p) throws IOException {
		productFile.writeAtEnd(p.withId(productFile.getLastId() + 1));
	}

	public void update(Long position, Product p) throws IOException {
		productFile.update(position, p);
	}
}