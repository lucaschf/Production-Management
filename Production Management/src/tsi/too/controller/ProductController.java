package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

import tsi.too.io.ProductFile;
import tsi.too.model.Product;
import tsi.too.util.Pair;

public class ProductController {
	ProductFile productFile;
	private static ProductController instance;

	private final ProductInputsController productInputsController;

	private ProductController() throws FileNotFoundException {
		this.productFile = ProductFile.getInstance();
		productInputsController = ProductInputsController.getInstance();
	}

	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static ProductController getInstance() throws FileNotFoundException {
		synchronized (ProductController.class) {
			if (instance == null)
				instance = new ProductController();

			return instance;
		}
	}

	/**
	 * Fetch all recorded {@link Product}.
	 * 
	 * @return a list with all {@link Product}.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Product> fetchProducts() throws IOException {
		return productFile.readAllFile();
	}

	/**
	 * Fetch all recorded {@link Product} as a {@link Vector}.
	 * 
	 * @return a {@link Vector} with all {@link Product}.
	 * @throws IOException if an I / O error occurs.
	 */
	public Vector<Product> fetchProductsAsVector() throws IOException {
		return productFile.readAllFileAsVector();
	}

	/**
	 * Fetch a {@link Product} by its name (case insensitive).
	 * 
	 * @param name the target name.
	 * @return A {@link Pair} with the {@link Product} and its position in file or
	 *         null if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public Pair<Product, Long> findByName(final String name) throws IOException, CloneNotSupportedException {
		return fetch(p -> p.getName().equalsIgnoreCase(name));
	}

	/**
	 * Fetch a {@link Product} based on a {@link Predicate}.
	 * 
	 * @param predicate the target predicate.
	 * @return A {@link Pair} with the {@link Product} and its position in file or
	 *         null if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public Pair<Product, Long> fetch(Predicate<Product> predicate) throws IOException, CloneNotSupportedException {
		var product = productFile.fetch(predicate);

		if (product != null)
			retrieveInputs(product.getFirst());

		return product;
	}

	/**
	 * Retrieves all linked inputs for a {@link Product} by adding a copy of each on the {@code product} inputs.
	 * 
	 * @param product the target input.
	 * @throws IOException if an I / O error occurs.
	 * @throws CloneNotSupportedException if an Input cloning error occurs.
	 */
	private void retrieveInputs(Product product) throws IOException, CloneNotSupportedException {
		var inputs = productInputsController.fetchLinkedInputs(product.getId());

		product.addProductionInput(inputs);
	}

	/**
	 * Generates an id for the {@code product} and inserts it to the file.
	 * 
	 * @param product the {@link Product} to be inserted.
	 * @throws IOException                if an I / O error occurs.
	 * @throws CloneNotSupportedException If cloning the {@code input} with the new
	 *                                    id fails.
	 */
	public void insert(Product product) throws IOException, CloneNotSupportedException {
		productFile.writeAtEnd(product.withId(productFile.getLastId() + 1));
	}

	/**
	 * Update the {@link Product} data.
	 * 
	 * @param position the target position.
	 * @param newData  the new data.
	 * @throws IOException if an I / O error occurs.
	 */
	public void update(Long position, Product newData) throws IOException {
		productFile.update(position, newData);
	}
}