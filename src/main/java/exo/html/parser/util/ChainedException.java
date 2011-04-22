package exo.html.parser.util;

@SuppressWarnings("serial")
public class ChainedException extends Exception {

	protected Throwable throwable;

	public ChainedException() {
	}

	public ChainedException(String message) {
		super(message);
	}

	public ChainedException(Throwable throwable) {
		this.throwable = throwable;
	}

	public ChainedException(String message, Throwable throwable) {
		super(message);
		this.throwable = throwable;
	}

}
