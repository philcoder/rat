package com.philipp.manager.exception;

public class NotFoundLogHistoryException extends Exception {
	private static final long serialVersionUID = -2191064769683406675L;

	public NotFoundLogHistoryException() {
		this("Not found log history");
	}

	public NotFoundLogHistoryException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundLogHistoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundLogHistoryException(String message) {
		super(message);
	}

}
