package se.wendt.home.eventlog;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import se.wendt.home.util.PlainTextSender;
import se.wendt.home.util.Thread;

public class EventLogMailDigestImplTest {

	private final String eventDescription = "description/a device connected";
	private final String eventSource = "source/blutooth";

	@Mock
	private PlainTextSender mailBackend;

	private EventLogMailDigestImpl testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new EventLogMailDigestImpl(mailBackend) {
			@Override
			protected long getMilliSecondsToNextDigest() {
				return this.now();
			}
		};
	}

	@Test
	public void sendsMail() throws Exception {
		testee.recordEvent(eventSource, eventDescription);
		testee.start();
		testee.stop();

		Thread.sleep(100);

		ArgumentCaptor<String> argumentCatcher = ArgumentCaptor.forClass(String.class);
		verify(mailBackend).send(eq(EventLogMailDigestImpl.SUBJECT), argumentCatcher.capture());
		String body = argumentCatcher.getValue();
		assertTrue("event source missing: " + body, body.contains(eventSource));
		assertTrue("event description missing: " + body, body.contains(eventDescription));
	}
}
