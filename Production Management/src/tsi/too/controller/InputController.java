package tsi.too.controller;

import tsi.too.Constants;
import tsi.too.io.InputDialog.InputValidator;
import tsi.too.io.InputsFile;
import tsi.too.model.Input;
import tsi.too.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class InputController {
	private final InputsFile inputsFile;

	private static InputController instance;

	public final InputValidator<String> nameValidator = input -> {
		if (input.isBlank())
			return Constants.NAME_CANNOT_BE_BLANK;
		if (input.length() < 4)
			return Constants.NAME_IS_TO_SHORT;
		return null;
	};

	public final InputValidator<Double> priceValidator = input -> {
		if (input <= 0)
			return Constants.PRICE_MUST_BE_GREATER_THAN_ZERO;

		return null;
	};

	private InputController() throws FileNotFoundException {
		inputsFile = InputsFile.getInstance();
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

	public Vector<Input> fetchInputsAsVector() throws IOException {
		return inputsFile.readAllFileAsVector();
	}

	public Pair<Input, Long> findByName(final String name) throws IOException {
		return inputsFile.findByName(name);
	}

	public Pair<Input, Long> findById(final long id) throws IOException {
		return inputsFile.fetch(p -> p.getId() == id);
	}

	public Input insert(Input p) throws IOException {
		var target = p.withId(inputsFile.getLastId() + 1);
		inputsFile.writeAtEnd(target);
		return target;
	}

	public void update(Long position, Input p) throws IOException {
		inputsFile.update(position, p);
	}
}