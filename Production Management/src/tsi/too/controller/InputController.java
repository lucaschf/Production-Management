package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import tsi.too.Constants;
import tsi.too.io.ProductionInputsFile;
import tsi.too.io.InputDialog.InputValidator;
import tsi.too.model.Input;
import tsi.too.util.Pair;

public class InputController {
	private ProductionInputsFile inputsFile;

	private static InputController instance;

	public final InputValidator<String> nameValidator = new InputValidator<String>() {
		@Override
		public String getErrorMessage(String input) {
			if (input.isBlank())
				return Constants.NAME_CANNOT_BE_BLANK;
			if (input.length() < 4)
				return Constants.NAME_IS_TO_SHORT;
			return null;
		}
	};
	
	private InputController() throws FileNotFoundException {
		inputsFile = ProductionInputsFile.getInstance();
	}

	public static InputController getInstance() throws FileNotFoundException {
		synchronized (InputController.class) {
			if (instance == null)
				instance = new InputController();

			return instance;
		}
	}

	public List<Input> fetchInputs() throws IOException {
		return inputsFile.readAllFile();
	}

	public Pair<Input, Long> findByName(final String name) throws IOException {
		return inputsFile.findByName(name);
	}
	
	public Pair<Input, Long> findById(final long id) throws IOException {
		return inputsFile.fetch(p -> p.getId() == id);
	}

	public void insert(Input p) throws IOException {
		inputsFile.writeAtEnd(p.withId(inputsFile.getLastId() + 1));
	}

	public void update(Long position, Input p) throws IOException {
		inputsFile.update(position, p);
	}
}