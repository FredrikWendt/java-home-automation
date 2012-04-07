package se.wendt.home.util;

public class HomeAutomationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HomeAutomationException(Throwable e, String format, Object... args) {
		super(String.format(format, args), e);
	}

	public HomeAutomationException(String format, Object... args) {
		super(String.format(format, args));
	}

	public HomeAutomationException(String message) {
		super(message);
	}

	public HomeAutomationException(String message, Throwable t) {
		super(message, t);
	}

	public HomeAutomationException() {
	}

}
