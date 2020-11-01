package tsi.too.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import tsi.too.ext.LocalDateExt;
import tsi.too.io.ProductionFile;
import tsi.too.model.Production;

public class ProductionController {
	private static final String fileName = "Production.dat";

	private ProductionFile productionFile;
	private static ProductionController instance;

	private ProductionController() throws FileNotFoundException {
		productionFile = new ProductionFile(fileName);
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
	
	public List<Production> fetchAll() throws IOException{
		return productionFile.readAllFile();
	}
	
	public List<Production> fetchByPeriod(LocalDate start, LocalDate end) throws IOException{
		return new ArrayList<>(productionFile.readAllFile(t -> LocalDateExt.isInPeriod(t.getDate(), start, end)));
	}
}
