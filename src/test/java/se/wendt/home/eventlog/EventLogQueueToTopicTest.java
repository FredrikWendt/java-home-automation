package se.wendt.home.eventlog;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static se.wendt.home.eventlog.EventLog.JMS_PROPERTY_WITH_DESCRIPTION;
import static se.wendt.home.eventlog.EventLog.JMS_PROPERTY_WITH_SOURCE;

import org.junit.Before;
import org.junit.Test;

import se.wendt.home.util.JmsUtilsTestBase;

public class EventLogQueueToTopicTest extends JmsUtilsTestBase {

	private EventLogQueueToTopic testee;
	private final String eventDescription = "description";
	private final String eventSource = "source";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new EventLogQueueToTopic(jms);
	}

	@Test
	public void testname() throws Exception {
		testee.recordEvent(eventSource, eventDescription);

		verify(jmsMessage).setStringProperty(JMS_PROPERTY_WITH_SOURCE, eventSource);
		verify(jmsMessage).setStringProperty(JMS_PROPERTY_WITH_DESCRIPTION, eventDescription);
	}
}
