package com.davicro.core.dao;

public class DataAccessException extends Exception {
	public DataAccessException(String message, Exception cause) {
		super(message, cause);
	}
}
