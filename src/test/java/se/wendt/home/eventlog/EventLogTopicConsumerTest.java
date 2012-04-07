package se.wendt.home.eventlog;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.util.JmsUtilsTestBase;

public class EventLogTopicConsumerTest extends JmsUtilsTestBase {

	@Mock
	private EventLog listener1;
	@Mock
	private EventLog listener2;

	private final String eventDescription = "description";
	private final String eventSource = "source";

	private EventLogTopicConsumer testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new EventLogTopicConsumer(jms);
		testee.addListener(listener1);
		testee.addListener(listener2);
	}

	@Test
	public void messagesArePassedOnToAllListeners() throws Exception {
		given(message1.getStringProperty(EventLog.JMS_PROPERTY_WITH_DESCRIPTION)).willReturn(eventDescription);
		given(message1.getStringProperty(EventLog.JMS_PROPERTY_WITH_SOURCE)).willReturn(eventSource);

		testee.handle(message1);

		verify(listener1).recordEvent(eventSource, eventDescription);
		verify(listener2).recordEvent(eventSource, eventDescription);
	}
}
