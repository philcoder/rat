package com.philipp.exception;

public class NotFoundMachineException extends Exception {
	private static final long serialVersionUID = 2770462144389894479L;

	public NotFoundMachineException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotFoundMachineException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundMachineException(String message) {
		super(message);
	}

}
