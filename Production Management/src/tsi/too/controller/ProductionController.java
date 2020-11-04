package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import tsi.too.exception.InsufficientStockException;
import tsi.too.ext.LocalDateExt;
import tsi.too.io.ProductionFile;
import tsi.too.model.Input;
import tsi.too.model.Product;
import tsi.too.model.Production;
import tsi.too.util.Pair;

public class ProductionController {
	private final ProductionFile productionFile;
	private static ProductionController instance;

	private final ProductController productController;

	private final InputEntryController inputEntryController;

	private ProductionController() throws FileNotFoundException {
		productionFile = ProductionFile.getInstance();
		productController = ProductController.getInstance();
		inputEntryController = InputEntryController.getInstance();
	}

	public static ProductionController getInstance() throws FileNotFoundException {
		synchronized (ProductController.class) {
			if (instance == null)
				instance = new ProductionController();

			return instance;
		}
	}

	public void insert(Production p) throws IOException {
		productionFile.writeAtEnd(p);
	}

	public List<Production> fetchAll() throws IOException {
		return productionFile.readAllFile();
	}

	public List<Production> fetchByPeriod(LocalDate start, LocalDate end) throws IOException {
		return productionFile.readAllFile(t -> LocalDateExt.isInPeriod(t.getDate(), start, end));
	}

	public List<Pair<String, Production>> fetchPairedByPeriod(LocalDate start, LocalDate end) throws IOException {
		var periodProduction = fetchByPeriod(start, end);
		var paired = new ArrayList<Pair<String, Production>>();

		periodProduction.forEach(p -> {
			Pair<Product, Long> product = null;
			try {
				product = productController.fetch(pr -> pr.getId() == p.getProductId());
			} catch (IOException | CloneNotSupportedException e) {
				e.printStackTrace();
			}

			if (product == null)
				paired.add(new Pair<>(null, p));
			else
				paired.add(new Pair<>(product.getFirst().getName(), p));
		});

		return paired;
	}

	public void withdraw(List<Pair<Long, Input>> inputsReserved)
			throws IOException, IllegalArgumentException, InsufficientStockException {
		for (Pair<Long, Input> p : inputsReserved) {
			inputEntryController.withdraw(p.getFirst(), p.getSecond().getQuantity());
		}
	}
}