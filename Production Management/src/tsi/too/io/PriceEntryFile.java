package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_TIME_PATTERN;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tsi.too.model.PriceEntry;

public class PriceEntryFile extends BinaryFile<PriceEntry>{

	@Override
	public int recordSize() {
		return Double.BYTES
				+ Long.BYTES
				+ Character.BYTES * BRAZILIAN_DATE_TIME_PATTERN.length();
	}

	@Override
	public void write(PriceEntry e) throws IOException {
		file.writeLong(e.getItemCode());
		file.writeDouble(e.getPrice());
		writeLocalDateTime(e.getDate());
	}

	@Override
	public PriceEntry read() throws IOException {
		return new PriceEntry(file.readLong(), file.readDouble(), readDateTime());
	}
	
	private void writeLocalDateTime(LocalDateTime date) throws IOException {
		var target = date.format(DateTimeFormatter.ofPattern(BRAZILIAN_DATE_TIME_PATTERN));
		writeString(target, BRAZILIAN_DATE_TIME_PATTERN.length());
	}
	
	private LocalDateTime readDateTime() throws IOException {
		var strDate = readString(BRAZILIAN_DATE_TIME_PATTERN.length());
		return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(BRAZILIAN_DATE_TIME_PATTERN));
	}
}