package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_TIME_PATTERN;

import java.io.FileNotFoundException;
import java.io.IOException;

import tsi.too.model.PriceEntry;

public class PriceEntryFile extends BinaryFile<PriceEntry>{

	public PriceEntryFile(String fileName) throws FileNotFoundException {
		openFile(fileName, OpenMode.READ_WRITE);
	}
	
	@Override
	public int recordSize() {
		return Double.BYTES
				+ Long.BYTES
				+ Character.BYTES * BRAZILIAN_DATE_TIME_PATTERN.length();
	}

	@Override
	public void write(PriceEntry e) throws IOException {
		file.writeLong(e.getItemId());
		file.writeDouble(e.getPrice());
		writeLocalDateTime(e.getDate(), BRAZILIAN_DATE_TIME_PATTERN);
	}

	@Override
	public PriceEntry read() throws IOException {
		return new PriceEntry(file.readLong(), file.readDouble(), readDateTime(BRAZILIAN_DATE_TIME_PATTERN));
	}
	
}