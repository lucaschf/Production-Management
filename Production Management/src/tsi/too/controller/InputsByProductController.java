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
	 * @throws IOException if an I / O error occurs.
	 */
	public void unLink(long productId, long inputId) throws IOException {
		var target = inputsByProdcutFile.fetch(p -> p.getInputId() == inputId && p.getProductId() == productId);
		if (target == null)
			return;

		inputsByProdcutFile.removeRecord(target.getSecond());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId the target product id.
	 * @return a list with all unlinked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchUnlinkedInputs(long productId) throws IOException {
		var linked = inputsByProdcutFile.readAllFile(t -> t.getProductId() == productId).stream()
				.map(ProductInputRelation::getInputId).collect(Collectors.toList());

		return inputController.fetchInputs().stream().filter(p -> !linked.contains(p.getId()))
				.collect(Collectors.toList());
	}

	/**
	 * Fetch all unlinked inputs for a given product.
	 * 
	 * @param productId the target product id.
	 * @return a list with all linked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchLinkedInputs(long productId) throws IOException {
		var linked = inputsByProdcutFile.readAllFile(t -> t.getProductId() == productId);
		var result = new ArrayList<Input>();

		linked.forEach(item -> {
			try {
				result.add(new Input(inputController.findById(item.getInputId()).getFirst().getName(),
						item.getQuantity(), item.getInputId(), 0));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		return result;
	}
	
	/**
	 * Fetch all unlinked inputs with the las price for a given product in a given date.
	 * 
	 * @param productId the target product id.
	 * @param date 
	 * @return a list with all linked inputs.
	 * @throws IOException if an I / O error occurs.
	 */
	public List<Input> fetchLinkedInputsWithPrice(long productId, LocalDate date) throws IOException {
		var linked = inputsByProdcutFile.readAllFile(t -> t.getProductId() == productId);
		var result = new ArrayList<Input>();

		linked.forEach(item -> {
			try {
				var price  = inputPriceEntryController.fetchPricePerDate(item.getInputId(), date);
				
				var input = new Input(inputController.findById(item.getInputId()).getFirst().getName(),
						item.getQuantity(), item.getInputId(), price.getPrice());
				
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