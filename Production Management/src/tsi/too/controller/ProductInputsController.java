package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.ProductInputFile;
import tsi.too.model.Input;
import tsi.too.model.ProductInput;
import tsi.too.util.Pair;

public class ProductInputsController {
	private final ProductInputFile productInputsFile;
	private final InputController inputController;
	private static ProductInputsController instance;

	private ProductInputsController() throws FileNotFoundException {
		productInputsFile = ProductInputFile.getInstance();
		inputController = InputController.getInstance();
	}

	public static ProductInputsController getInstance() throws FileNotFoundException {
		synchronized (InputController.class) {
			if (instance == null)
				instance = new ProductInputsController();

			return instance;
		}
	}

	/**
	 * Links an input to a product.
	 * 
	 * @param target The relationship between inputs and output.
	 * @throws IOException if an I / O error occurs.
	 */
	public void link(ProductInput target) throws IOException {
		productInputsFile.writeAtEnd(target);
	}

	public void update(long pos, ProductInput newData) throws IOException {
		productInputsFile.update(pos, newData);
	}

	/**
	 * Unlinks an input from a product.
	 * 
	 * @param productId the target product id.
	 * @param inputId   the target input id.
	 * @throws IOException if an I / O error occurs.
	 */
	public void unLink(long productId, long inputId) throws IOException {
		var target = productInputsFile.fetch(p -> p.getInputId() == inputId && p.getProductId() == productId);
		if (target == null)
			return;

		productInputsFile.removeRecord(target.getSecond());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId   the target product id.
	 * @return a list with all unlinked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchUnlinkedInputs(long productId) throws IOException {
		List<Long> linkedIds = fetchLinkedInputs(productId).stream().map(Input::getId).collect(Collectors.toList());

		return inputController.fetchInputs().stream().filter(i -> !linkedIds.contains(i.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId   the target product id.
	 * @return a list with all linked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchLinkedInputs(long productId) throws IOException {
		var linked = productInputsFile.readAllFile(t -> t.getProductId() == productId);
		var result = new ArrayList<Input>();

		linked.forEach(item -> {
			try {
				var target = inputController.findById(item.getInputId()).getFirst();
				var input = new Input(target.getName(), item.getInputQuantity(), item.getInputId(), 0);

				result.add(input);
			} catch (NullPointerException | IOException e) {
				System.out.println(item);
				System.out.println(e.getMessage());
			}
		});

		return result;
	}

	public List<ProductInput> fetchAll(long productId) throws IOException {
		return productInputsFile.readAllFile(p -> p.getProductId() == productId);
	}

	public Pair<ProductInput, Long> fetchByProductAndInput(long productId, long inputId) throws IOException {
		return productInputsFile.fetch(t -> t.getInputId() == inputId && t.getProductId() == productId);
	}

	public List<ProductInput> fetchByProduct(long productId) throws IOException {
		return productInputsFile.readAllFile(p -> productId == p.getProductId());
	}
}