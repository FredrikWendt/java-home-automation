package se.wendt.home.eventlog;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class EventLogStdoutImplTest {

	@Test
	public void loggingWritesOnTheAssignedPrintStream() throws Exception {
		String eventDescription = "D";
		String eventSource = "S";
		PrintStream out = mock(PrintStream.class);

		new EventLogStdoutImpl(out).recordEvent(eventSource, eventDescription);

		ArgumentCaptor<String> argumentCatcher = ArgumentCaptor.forClass(String.class);
		verify(out).println(argumentCatcher.capture());
		String logStatement = argumentCatcher.getValue();
		assertTrue("event source missing: " + logStatement, logStatement.contains(eventSource));
		assertTrue("event description missing: " + logStatement, logStatement.contains(eventDescription));
	}

}
