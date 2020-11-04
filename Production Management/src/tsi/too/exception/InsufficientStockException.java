package tsi.too.exception;

public class InsufficientStockException extends Exception {

	public InsufficientStockException() {
		super();
	}

	public InsufficientStockException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InsufficientStockException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsufficientStockException(String message) {
		super(message);
	}

	public InsufficientStockException(Throwable cause) {
		super(cause);
	}
}
