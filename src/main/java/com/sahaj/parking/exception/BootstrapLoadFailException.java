package com.sahaj.parking.exception;

public class BootstrapLoadFailException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final ErrorCode errorCode;
	
	public BootstrapLoadFailException (String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
