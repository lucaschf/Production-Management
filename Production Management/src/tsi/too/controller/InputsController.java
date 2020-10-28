package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import tsi.too.io.ProductionInputsFile;
import tsi.too.model.Input;
import tsi.too.util.Pair;

public class InputsController {
	private ProductionInputsFile inputsFile;

	private static InputsController instance;

	private InputsController() throws FileNotFoundException {
		inputsFile = ProductionInputsFile.getInstance();
	}

	public static InputsController getInstance() throws FileNotFoundException {
		synchronized (InputsController.class) {
			if (instance == null)
				instance = new InputsController();

			return instance;
		}
	}

	public List<Input> fetchInputs() throws IOException {
		return inputsFile.readAllFile();
	}

	public Pair<Input, Long> findByName(final String name) throws IOException {
		return inputsFile.findByName(name);
	}

	public void insert(Input p) throws IOException {
		inputsFile.writeAtEnd(p.withId(inputsFile.getLastId() + 1));
	}

	public void update(Long position, Input p) throws IOException {
		inputsFile.update(position, p);
	}
}