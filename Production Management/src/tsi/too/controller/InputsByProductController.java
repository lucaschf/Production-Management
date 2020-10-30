package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import tsi.too.io.InputsByProdcutFile;
import tsi.too.model.Input;
import tsi.too.model.ProductInputRelation;
import tsi.too.util.Pair;

public class InputsByProductController {
	private InputsByProdcutFile inputsByProdcutFile;

	private InputController inputController;

	private static InputsByProductController instance;

	private InputsByProductController() throws FileNotFoundException {
		inputsByProdcutFile = InputsByProdcutFile.getInstance();
		inputController = InputController.getInstance();
	}

	public static InputsByProductController getInstance() throws FileNotFoundException {
		synchronized (InputController.class) {
			if (instance == null)
				instance = new InputsByProductController();

			return instance;
		}
	}

	public void link(ProductInputRelation target) throws IOException {
		inputsByProdcutFile.writeAtEnd(target);
	}

	public void link(Collection<ProductInputRelation> targets) throws IOException {
		for (ProductInputRelation p : targets)
			link(p);
	}

	public void update(long pos, ProductInputRelation newData) throws IOException {
		inputsByProdcutFile.update(pos, newData);
	}
	
	public void unLink(long productId, long inputId) throws IOException {
		var target = inputsByProdcutFile.fetch(p -> p.getInputId() == inputId && p.getProductId() == productId);
		if(target == null)
			return;
		
		inputsByProdcutFile.removeRecord(target.getSecond());
	}

	public List<Input> fetchUnlinkedInputs(long productId) throws IOException {
		var linked = inputsByProdcutFile.ReadAllFile(t -> t.getProductId() == productId).stream()
				.map(p -> p.getInputId())
				.collect(Collectors.toList());

		return inputController.fetchInputs().stream()
				.filter(p -> !linked.contains(p.getId()))
				.collect(Collectors.toList());
	}

	public List<Input> fetchLinkedInputs(long productId) throws IOException {
		var linked = inputsByProdcutFile.ReadAllFile(t -> t.getProductId() == productId);
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

	public Pair<ProductInputRelation, Long> fetchByPordutAndInput(long produtcId, long inputId) throws IOException {
		return inputsByProdcutFile.fetch(t -> t.getInputId() == inputId && t.getProductId() == produtcId);
	}
}