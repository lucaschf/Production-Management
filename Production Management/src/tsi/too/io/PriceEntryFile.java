package tsi.too.io;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tsi.too.model.PriceEntry;

public class PriceEntryFile extends BinaryFile<PriceEntry>{

	private final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
	
	@Override
	public int recordSize() {
		return Double.BYTES
				+ Character.BYTES * DATE_TIME_PATTERN.length();
	}

	@Override
	public void write(PriceEntry e) throws IOException {
		file.writeDouble(e.getPrice());
		writeLocalDateTime(e.getDate());
	}

	@Override
	public PriceEntry read() throws IOException {
		return new PriceEntry(file.readDouble(), readDateTime());
	}
	
	private void writeLocalDateTime(LocalDateTime date) throws IOException {
		var target = date.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
		writeString(target, DATE_TIME_PATTERN.length());
	}
	
	private LocalDateTime readDateTime() throws IOException {
		var strDate = readString(DATE_TIME_PATTERN.length());
		return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
	}
}
