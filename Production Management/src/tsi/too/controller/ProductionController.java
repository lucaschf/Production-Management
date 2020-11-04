package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tsi.too.ext.LocalDateExt;
import tsi.too.io.ProductionFile;
import tsi.too.model.InputEntry;
import tsi.too.model.Product;
import tsi.too.model.Production;
import tsi.too.util.Pair;

public class ProductionController {
	private static final String fileName = "Production.dat";

	private ProductionFile productionFile;
	private static ProductionController instance;

	private ProductController productController;

	private InputEntryController inputEntryController;

	private ProductionController() throws FileNotFoundException {
		productionFile = new ProductionFile(fileName);
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
		var f = fetchByPeriod(start, end);
		var paired = new ArrayList<Pair<String, Production>>();

		f.forEach(p -> {
			Pair<Product, Long> product = null;
			try {
				product = productController.fetch(pr -> pr.getId() == p.getProductId());
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (product == null)
				paired.add(new Pair<String, Production>(null, p));
			else
				paired.add(new Pair<String, Production>(product.getFirst().getName(), p));
		});

		return paired;
	}

	public void checkout(List<InputEntry> entries) throws IOException {
		Objects.requireNonNull(entries);

		for (InputEntry entry : entries) {
			var p = inputEntryController.fetchById(entry.getId());
			inputEntryController.update(p.getSecond(), entry);
		}
	}
}