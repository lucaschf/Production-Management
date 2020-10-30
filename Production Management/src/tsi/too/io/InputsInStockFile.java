package tsi.too.io;

import static tsi.too.Patterns.BRAZILIAN_DATE_TIME_PATTERN;
import static tsi.too.model.Input.MAX_NAME_LENGTH;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tsi.too.model.Input;
import tsi.too.util.Pair;

public class InputsInStockFile extends BinaryFile<Pair<Input, LocalDateTime>> {

	@Override
	public int recordSize() {
		var inputSize = Input.MAX_NAME_LENGTH * Character.BYTES // name
				+ Integer.BYTES // quantity
				+ Long.BYTES // code
				+ Double.BYTES // price
		;

		return inputSize + Character.BYTES * BRAZILIAN_DATE_TIME_PATTERN.length();
	}

	@Override
	public void write(Pair<Input, LocalDateTime> e) throws IOException {
		writeString(e.getFirst().getName(), MAX_NAME_LENGTH);
		file.writeInt(e.getFirst().getQuantity());
		file.writeLong(e.getFirst().getId());
		file.writeDouble(e.getFirst().getPrice());
		writeString(e.getSecond().format(DateTimeFormatter.ofPattern(BRAZILIAN_DATE_TIME_PATTERN)),
				BRAZILIAN_DATE_TIME_PATTERN.length());
	}

	@Override
	public Pair<Input, LocalDateTime> read() throws IOException {
		var i = new Input(readString(MAX_NAME_LENGTH), file.readInt(), file.readLong(), file.readDouble());
		var strDate = readString(BRAZILIAN_DATE_TIME_PATTERN.length());
		return new Pair<Input, LocalDateTime>(i,
				LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(BRAZILIAN_DATE_TIME_PATTERN)));
	}
	
	public void writeOffStock() {
		throw new RuntimeException("Not implemented");
	}
}
