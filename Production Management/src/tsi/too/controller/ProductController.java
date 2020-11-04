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
import java.util.function.Predicate;

public class ProductController {
	ProductFile productFile;
	private static ProductController instance;

	private ProductInputsController productInputsController;

	public final InputValidator<String> productNameValidator = input -> {
		if (input == null || input.isBlank())
			return Constants.NAME_CANNOT_BE_BLANK;
		if (input.length() < 4)
			return Constants.NAME_IS_TO_SHORT;

		return null;
	};

	private ProductController() throws FileNotFoundException {
		this.productFile = ProductFile.getInstance();
		productInputsController = ProductInputsController.getInstance();
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
		return fetch(p -> p.getName().equalsIgnoreCase(name));
	}

	public Pair<Product, Long> fetch(Predicate<Product> p) throws IOException {
		var product = productFile.fetch(p);

		if(product != null)
			retrieveInputs(product.getFirst());

		return product;
	}

	private void retrieveInputs(Product p) throws IOException {
		var inputs = productInputsController.fetchLinkedInputs(p.getId());

		p.addProductionInput(inputs);
	}

	public void insert(Product p) throws IOException {
		productFile.writeAtEnd(p.withId(productFile.getLastId() + 1));
	}

	public void update(Long position, Product p) throws IOException {
		productFile.update(position, p);
	}
}