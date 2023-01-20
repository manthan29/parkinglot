package com.sahaj.parking.exception;

public enum ErrorCode {
	
	FILE_NOT_FOUND(1001);
	
	public final int code;
	private ErrorCode(int code) {
		this.code = code;
	}
}
