package se.wendt.home.eventlog;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import se.wendt.home.util.JmsUtilsTestBase;

public class EventLogQueueProducerTest extends JmsUtilsTestBase {

	private EventLogQueueProducer testee;

	private final String eventDescription = "description";
	private final String eventSource = "source";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new EventLogQueueProducer(jms);
	}

	@Test
	public void testname() throws Exception {
		testee.recordEvent(eventSource, eventDescription);

		verify(jmsMessage).setStringProperty(EventLog.JMS_PROPERTY_WITH_DESCRIPTION, eventDescription);
		verify(jmsMessage).setStringProperty(EventLog.JMS_PROPERTY_WITH_SOURCE, eventSource);
	}
}
