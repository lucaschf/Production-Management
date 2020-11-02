package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.InputsByProdcutFile;
import tsi.too.model.Input;
import tsi.too.model.ProductInputRelation;
import tsi.too.util.Pair;

public class InputsByProductController {
	private final InputsByProdcutFile inputsByProdcutFile;
	private final InputController inputController;
	private static InputsByProductController instance;

	private InputPriceEntryController inputPriceEntryController;

	private InputsByProductController() throws FileNotFoundException {
		inputsByProdcutFile = InputsByProdcutFile.getInstance();
		inputController = InputController.getInstance();
		inputPriceEntryController = InputPriceEntryController.getInstance();
	}

	public static InputsByProductController getInstance() throws FileNotFoundException {
		synchronized (InputController.class) {
			if (instance == null)
				instance = new InputsByProductController();

			return instance;
		}
	}

	/**
	 * Links an input to a product.
	 * 
	 * @param target The relationship between inputs and output.
	 * @throws IOException if an I / O error occurs.
	 */
	public void link(ProductInputRelation target) throws IOException {
		inputsByProdcutFile.writeAtEnd(target);
	}

	public void update(long pos, ProductInputRelation newData) throws IOException {
		inputsByProdcutFile.update(pos, newData);
	}

	/**
	 * Unlinks an input from a product.
	 * 
	 * @param productId the target product id.
	 * @param inputId   the target input id.
	 * @param forQuantity 
	 * @throws IOException if an I / O error occurs.
	 */
	public void unLink(long productId, long inputId, double forQuantity) throws IOException {
		var target = inputsByProdcutFile.fetch(p -> p.getInputId() == inputId && p.getProductId() == productId
				&& p.getAmountProduced() == forQuantity);
		if (target == null)
			return;

		inputsByProdcutFile.removeRecord(target.getSecond());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId   the target product id.
	 * @param forQuantity
	 * @return a list with all unlinked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchUnlinkedInputs(long productId, double forQuantity) throws IOException {
		List<Long> linkedids = fetchLinkedInputs(productId, forQuantity).stream().map(i -> i.getId())
				.collect(Collectors.toList());

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
	public List<Input> fetchLinkedInputs(long productId, double forQuantity) throws IOException {
		var linked = inputsByProdcutFile
				.readAllFile(t -> t.getProductId() == productId && t.getAmountProduced() == forQuantity);
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

	/**
	 * Fetch all unlinked inputs with the last price for a given product in a given
	 * date.
	 * 
	 * @param productId   the target product id.
	 * @param date
	 * @param forQuantity
	 * @return a list with all linked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchLinkedInputsWithPrice(long productId, LocalDate date, double forQuantity)
			throws IOException {
		var linked = inputsByProdcutFile
				.readAllFile(t -> t.getProductId() == productId && t.getAmountProduced() == forQuantity);
		var result = new ArrayList<Input>();

		linked.forEach(item -> {
			try {
				var price = inputPriceEntryController.fetchPricePerDate(item.getInputId(), date);

				var input = new Input(inputController.findById(item.getInputId()).getFirst().getName(),
						item.getInputQuantity(), item.getInputId(), price.getPrice());

				result.add(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		return result;
	}

	public Pair<ProductInputRelation, Long> fetchByProductAndInput(long productId, long inputId) throws IOException {
		return inputsByProdcutFile.fetch(t -> t.getInputId() == inputId && t.getProductId() == productId);
	}
}