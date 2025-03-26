package de.bcxp.challenge.common;

public class InvalidDataException extends RuntimeException {

	private static final long serialVersionUID = 4869315560971993703L;

	public InvalidDataException(final String message) {
		super(message);
	}

}
