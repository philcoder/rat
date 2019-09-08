package com.philipp.manager.exception;

public final class ExecuteRemoteCommandException extends Exception {
	private static final long serialVersionUID = -1487940142896901504L;

	public ExecuteRemoteCommandException(String message) {
		super(message);
	}

	public ExecuteRemoteCommandException(String message, Throwable cause) {
		super(message, cause);

	}
}
