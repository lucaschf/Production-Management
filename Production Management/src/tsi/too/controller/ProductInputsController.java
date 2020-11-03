package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.ProductInputFile;
import tsi.too.model.Input;
import tsi.too.model.InputEntry;
import tsi.too.model.ProductInput;
import tsi.too.util.Pair;

public class ProductInputsController {
	private final ProductInputFile prodcutInputsFile;
	private final InputController inputController;
	private static ProductInputsController instance;

	private InputEntryController inputStockController;

	private ProductInputsController() throws FileNotFoundException {
		prodcutInputsFile = ProductInputFile.getInstance();
		inputController = InputController.getInstance();
		inputStockController = InputEntryController.getInstance();
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
		prodcutInputsFile.writeAtEnd(target);
	}

	public void update(long pos, ProductInput newData) throws IOException {
		prodcutInputsFile.update(pos, newData);
	}

	/**
	 * Unlinks an input from a product.
	 * 
	 * @param productId the target product id.
	 * @param inputId   the target input id.
	 * @throws IOException if an I / O error occurs.
	 */
	public void unLink(long productId, long inputId) throws IOException {
		var target = prodcutInputsFile.fetch(p -> p.getInputId() == inputId && p.getProductId() == productId);
		if (target == null)
			return;

		prodcutInputsFile.removeRecord(target.getSecond());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId   the target product id.
	 * @param forQuantity
	 * @return a list with all unlinked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchUnlinkedInputs(long productId) throws IOException {
		List<Long> linkedids = fetchLinkedInputs(productId).stream().map(i -> i.getId()).collect(Collectors.toList());

		return inputController.fetchInputs().stream().filter(i -> !linkedids.contains(i.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId   the target product id.
	 * @param forQuantity
	 * @return a list with all linked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchLinkedInputs(long productId) throws IOException {
		var linked = prodcutInputsFile.readAllFile(t -> t.getProductId() == productId);
		var result = new ArrayList<Input>();

		linked.forEach(item -> {
			try {
				var target = inputController.findById(item.getInputId()).getFirst();
				var input = new Input(target.getName(), item.getInputQuantity(), item.getInputId(), 0);

				result.add(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		return result;
	}

	public List<ProductInput> fetchAll(long productId) throws IOException{
		return prodcutInputsFile.readAllFile(p-> p.getProductId() == productId);
	}

	public Pair<ProductInput, Long> fetchByProductAndInput(long productId, long inputId) throws IOException {
		return prodcutInputsFile.fetch(t -> t.getInputId() == inputId && t.getProductId() == productId);
	}

	public List<ProductInput> fetchByProduct(long productId) throws IOException {
		return prodcutInputsFile.readAllFile(p -> productId == p.getProductId());
	}
}