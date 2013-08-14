package edu.cmu.cs.dickerson.voting.io.exceptions;

public class LoaderDataException extends LoaderException {

	private static final long serialVersionUID = 1L;

	public LoaderDataException(int lineNumber, String message) {
		super("[Line " + lineNumber + "]: " + message);
	}
	public LoaderDataException(int lineNumber) {
		this(lineNumber, "Loader encountered a data value issue.");
	}
}
