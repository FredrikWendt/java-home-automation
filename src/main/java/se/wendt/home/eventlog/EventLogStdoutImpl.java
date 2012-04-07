package se.wendt.home.eventlog;

import java.io.PrintStream;

public class EventLogStdoutImpl implements EventLog {

	private PrintStream logStream;

	public EventLogStdoutImpl() {
		this(System.out);
	}

	public EventLogStdoutImpl(PrintStream out) {
		this.logStream = out;
	}

	@Override
	public void recordEvent(String eventSource, String eventDescription) {
		logStream.println(String.format("LOG: %s %s", eventSource, eventDescription));
	}

}
