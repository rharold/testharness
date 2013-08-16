package com.spx.regression;

public class TestException extends Exception {

	public TestException(Throwable t) {
		super(t);
	}
	
	public TestException(String message, Throwable t) {
		super(message, t);
	}
	
}
