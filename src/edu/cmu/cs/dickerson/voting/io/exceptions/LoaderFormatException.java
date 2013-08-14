package edu.cmu.cs.dickerson.voting.io.exceptions;

public class LoaderFormatException extends LoaderException {

	private static final long serialVersionUID = 1L;

	public LoaderFormatException(int lineNumber, String message) {
		super("[Line " + lineNumber + "]: " + message);
	}
	public LoaderFormatException(int lineNumber) {
		this(lineNumber, "Loader encountered a data format issue.");
	}
}
