package tsi.too.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tsi.too.util.Pair;

/**
 * Provides operations for a binary file using the services of a file with
 * random access. The definitions on this class are independent of the structure
 * of a specific file. Therefore, the services can be used by any class that
 * wants to use a structure and implement the services of a file with random
 * access.
 * 
 * <p>
 * <b>WARNING:</b> The file structure in relation to the amount and types of
 * data in the record is defined by the user of the class. However, the size of
 * the records is required to be fixed. This restriction is fundamental for the
 * correct functioning of the methods of the <code>BinaryFile</code> class.
 * </p>
 * 
 * @param <E> the type of record.
 *
 * @author Lucas Cristovam
 * @author Prof. Márlon Oliveira da Silva
 * @version 0.40
 */
public abstract class BinaryFile<E> {
	private String fileName;

	protected RandomAccessFile file;

	protected BinaryFile() {
	}

	protected BinaryFile(String fileName, OpenMode mode) throws FileNotFoundException {
		openFile(fileName, mode);
	}	

	/**
	 * Gets the file name from disk.
	 * 
	 * @return a <code>String</code> with the file name
	 * 
	 * @since 0.35
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets a {@link FileChannel} for reading and writing to the file.
	 * 
	 * @return the {@link FileChannel} associated with this file.
	 * 
	 * @since 0.35
	 */
	public FileChannel getFileChannel() {
		return file.getChannel();
	}

	/**
	 * Gets the file length.
	 * 
	 * @return the size in {@link Byte}.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public long length() throws IOException {
		return file.length();
	}

	/**
	 * Opens a file in {@link OpenMode} mode.
	 * 
	 * @param fileName the file name on the disk.
	 * @param mode     the {@link OpenMode} to the file.
	 * @throws FileNotFoundException if the file does not open (due to protected
	 *                               recording, user without write privileges ...).
	 * 
	 * @since 0.36
	 */
	protected void openFile(String fileName, OpenMode mode) throws FileNotFoundException {
		this.fileName = fileName;
		file = new RandomAccessFile(fileName, mode.getMode());
	}

	/**
	 * Calculates the number of records in file.
	 * 
	 * @return the number of records in file.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public long countRecords() throws IOException {
		return length() / recordSize();
	}

	/**
	 * Position the file pointer at the position assigned by <code>offset</code>
	 * considering the record zero as the first record in the file.
	 * 
	 * @param offset number corresponding to a record in the file.
	 * @throws IOException if {@code offset} is less than {@code 0} or if
	 *                     {@code offset} is greater than {@link #countRecords()} or
	 *                     if an I/O error occurs.
	 * 
	 * @since 0.36
	 */
	public void seekRecord(long offset) throws IOException {
		if (offset > countRecords())
			throw new IOException("Offset out of bounds");

		file.seek(offset * recordSize());
	}

	/**
	 * Position the file pointer at the last record position.
	 * 
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.36
	 */
	public void seekLastRecord() throws IOException {
		seekRecord(countRecords() - 1);
	}

	/**
	 * Writes a {@link LocalDateTime} in file.
	 * 
	 * @param date    the {@link LocalDateTime} to write.
	 * @param pattern the date pattern.
	 * @throws IOException if an I / O occurs.
	 * 
	 * @since 0.40
	 */
	public void writeLocalDateTime(LocalDateTime date, String pattern) throws IOException {
		var target = date.format(DateTimeFormatter.ofPattern(pattern));
		writeString(target, pattern.length());
	}

	/**
	 * Reads a {@link LocalDateTime} from file.
	 * 
	 * @param pattern the date pattern.
	 * @return the read LocalDateTime
	 * @throws IOException if an I / O occurs.
	 * 
	 * @since 0.40
	 */
	public LocalDateTime readDateTime(String pattern) throws IOException {
		var strDate = readString(pattern.length());
		return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * Writes a {@link LocalDate} in file.
	 * 
	 * @param date    the {@link LocalDate} to write.
	 * @param pattern the date pattern.
	 * @throws IOException if an I / O occurs.
	 * 
	 * @since 0.41
	 */
	public void writeLocalDate(LocalDate date, String pattern) throws IOException {
		var target = date.format(DateTimeFormatter.ofPattern(pattern));
		writeString(target, pattern.length());
	}

	/**
	 * Reads a {@link LocalDate} from file.
	 * 
	 * @param pattern the date pattern.
	 * @return the read LocalDateTime
	 * @throws IOException if an I / O occurs.
	 * 
	 * @since 0.41
	 */
	public LocalDate readDate(String pattern) throws IOException {
		var strDate = readString(pattern.length());
		return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * Writes a string in file.
	 * 
	 * @param str  the string to be write.
	 * @param size the string length
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.36
	 */
	protected void writeString(String str, int size) throws IOException {
		StringBuffer buffer = null;

		if (str != null) {
			buffer = new StringBuffer(str);
			buffer.setLength(size);
		} else
			buffer = new StringBuffer(size);

		file.writeChars(buffer.toString());
	}

	/**
	 * Reads a string from file.
	 * 
	 * @param size the string length
	 * @return the read string
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public String readString(int size) throws IOException {
		char str[] = new char[size];

		for (int i = 0; i < str.length; i++)
			str[i] = file.readChar();

		return new String(str).replace('\0', ' ').trim();
	}

	/**
	 * Reads a record of the file based on its position.
	 * 
	 * @param position the record position in file.
	 * @return the read record as object of type <code>E</code>.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.36
	 */
	public E read(long position) throws IOException {
		seekRecord(position);

		return read();
	}

	/**
	 * Adds a new record to the end of the file.
	 * 
	 * @param e the <code>E</code> to be recorded.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.38
	 */
	public void writeAtEnd(E e) throws IOException {
		file.seek(file.length());
		write(e);
	}

	/**
	 * Updates the record for the given <code>pos</code>.
	 * 
	 * <p>
	 * <b> WARNING: </b> this action will overwrite ALL the current data of the
	 * target position, if you are using Ids to control the stored data, make sure
	 * the new data has the same id as the current record.
	 * 
	 * @param pos     the position of the record in file.
	 * @param newData the new record data.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.38
	 */
	public void update(long pos, E newData) throws IOException {
		seekRecord(pos);
		write(newData);
	}

	/**
	 * Reads all file as a list.
	 * 
	 * @return the resulting list.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.37
	 */
	public List<E> readAllFile() throws IOException {
		var list = new ArrayList<E>();

		seekRecord(0);

		for (long i = 0; i < countRecords(); i++)
			list.add(read());

		return list;
	}

	/**
	 * Reads all file as a vector.
	 * 
	 * @return the resulting vector.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.37
	 */
	public Vector<E> readAllFileAsVector() throws IOException {
		Vector<E> v = new Vector<E>();

		seekRecord(0);

		for (long i = 0; i < countRecords(); i++)
			v.add(read());

		return v;
	}

	public List<E> readAllFile(Predicate<E> predicate) throws IOException {
		return readAllFile().stream().filter(predicate).collect(Collectors.toList());
	}

	/**
	 * Fetch a record based on a{@link Predicate}
	 * 
	 * @param p the {@link Predicate} for item test.
	 * @return a {@link Pair} containing the record found and its position on file
	 *         if found, null otherwise.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.39
	 */
	public Pair<E, Long> fetch(Predicate<E> p) throws IOException {
		seekRecord(0);

		E item;

		for (long i = 0; i < countRecords(); i++) {
			item = read();

			if (p.test(item))
				return new Pair<E, Long>(item, i);
		}

		return null;
	}

	/**
	 * Reads the last record of the file.
	 * 
	 * @return the read record as object of type <code>E</code> or null if there is
	 *         no records in file.
	 * 
	 * @since 0.37
	 */
	public E readLastOrNull() {
		try {
			seekLastRecord();
			return read();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Removes a record based on its position in the file.
	 * 
	 * @param position the position of the record in the file
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public void removeRecord(long position) throws IOException {
		seekLastRecord();

		// if the position is not the last, overwrite the destination record with the
		// last one in the file.
		if (position != countRecords() - 1) {
			var record = read();

			seekRecord(position);

			write(record); // overwrites the current record with the last one.
		}

		// Delete the last record by truncating the file.
		file.setLength(length() - recordSize());
	}

	/**
	 * Clean the file by truncating it.
	 * 
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.36
	 */
	public void clear() throws IOException {
		file.setLength(0);
	}

	/**
	 * Closes the file.
	 * 
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public void closeFile() throws IOException {
		if (file != null)
			file.close();
	}

	/**
	 * Calculates the record size in bytes based on its attributes.
	 * 
	 * @return the record size.
	 * 
	 * @since 0.35
	 */
	public abstract int recordSize();

	/**
	 * Adds a new record to the file.
	 * 
	 * @param e the <code>E</code> to be recorded.
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public abstract void write(E e) throws IOException;

	/**
	 * Reads a record of the file.
	 * 
	 * @return the read record as an object of the type <code>E</code>
	 * @throws IOException if an I/O error occurs.
	 * 
	 * @since 0.35
	 */
	public abstract E read() throws IOException;

	/**
	 * Represents the file opening mode.
	 * 
	 * @author Lucas Cristovam
	 * @version 0.1
	 * 
	 * @since 0.36
	 */
	public enum OpenMode {

		/**
		 * Read mode. Calling write methods will result in an IOException.
		 */
		READ("r"),

		/**
		 * Read and write mode.
		 */
		READ_WRITE("rw"),

		/**
		 * Read and write mode - synchronously. All updates to file content is written
		 * to the disk synchronously.
		 */
		READ_WRITE_D("rwd"),

		/**
		 * Read and write mode - synchronously. All updates to file content or meta data
		 * is written to the disk synchronously.
		 */
		READ_WRITE_S("rws");

		private String mode;

		/**
		 * @return the file mode as string.
		 * 
		 * @since 0.1
		 */
		public String getMode() {
			return mode;
		}

		private OpenMode(String mode) {
			this.mode = mode;
		}
	}
}