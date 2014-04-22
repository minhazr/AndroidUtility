package com.android.utility.exception;

public class ObjectInstanceNotCreatedException extends RuntimeException {

	public ObjectInstanceNotCreatedException() {
		super();
	}

	public ObjectInstanceNotCreatedException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ObjectInstanceNotCreatedException(String detailMessage) {
		super(detailMessage);
	}

	public ObjectInstanceNotCreatedException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3630574422654376185L;

}
