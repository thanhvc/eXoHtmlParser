package exo.html.parser.util;

@SuppressWarnings("serial")
public class ParserException extends ChainedException {
	public ParserException() {
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(Throwable throwable) {
		super(throwable);
	}

	public ParserException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
