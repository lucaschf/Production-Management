package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import tsi.too.io.InputsFile;
import tsi.too.model.Input;
import tsi.too.util.Pair;

public class InputController {
	private final InputsFile inputsFile;

	private static InputController instance;

	private InputController() throws FileNotFoundException {
		inputsFile = InputsFile.getInstance();
	}

	/**
	 * Ensures that only one instance is created
	 * 
	 * @return the created instance.
	 * @throws FileNotFoundException if persistence file opening fails.
	 */
	public static InputController getInstance() throws FileNotFoundException {
		synchronized (InputController.class) {
			if (instance == null)
				instance = new InputController();

			return instance;
		}
	}

	/**
	 * Fetch all {@link Input} in file.
	 * 
	 * @return a list with all {@link Input}
	 * @throws IOException if an I / o error occurs.
	 */
	public List<Input> fetchInputs() throws IOException {
		return inputsFile.readAllFile();
	}

	/**
	 * Fetch all {@link Input} in file as a {@link Vector}.
	 * 
	 * @return a {@link Vector} with all {@link Input}
	 * @throws IOException if an I / o error occurs.
	 */
	public Vector<Input> fetchInputsAsVector() throws IOException {
		return inputsFile.readAllFileAsVector();
	}

	/**
	 * Fetch a {@link Input} by its name (case insensitive).
	 * 
	 * @param name the target name.
	 * @return A {@link Pair} with the {@link Input} and its position in file or
	 *         null if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public Pair<Input, Long> findByName(final String name) throws IOException {
		return inputsFile.fetch(p -> p.getName().equalsIgnoreCase(name));
	}

	/**
	 * Fetch a {@link Input} by its id.
	 * 
	 * @param id the target id.
	 * @return A {@link Pair} with the {@link Input} and its position in file or
	 *         null if not found.
	 * @throws IOException if an I / O error occurs.
	 */
	public Pair<Input, Long> findById(final long id) throws IOException {
		return inputsFile.fetch(p -> p.getId() == id);
	}

	/**
	 * Generates an id for the {@link Input} and inserts it to the file.
	 * 
	 * @param input the {@link Input} to be inserted.
	 * @throws IOException                if an I / O error occurs.
	 * @throws CloneNotSupportedException If cloning the {@code input} with the new
	 *                                    id fails.
	 */
	public Input insert(Input input) throws IOException, CloneNotSupportedException {
		var target = input.withId(inputsFile.getLastId() + 1);
		inputsFile.writeAtEnd(target);
		return target;
	}

	/**
	 * Update the {@link Input} data.
	 * 
	 * @param position the target position.
	 * @param newData  the new data.
	 * @throws IOException if an I / O error occurs.
	 */
	public void update(Long position, Input newData) throws IOException {
		inputsFile.update(position, newData);
	}
}