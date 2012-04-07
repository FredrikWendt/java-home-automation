package se.wendt.home.eventlog;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.ObservableMessageConsumerBase;

/**
 * Takes events of the JMS queue.
 */
public class EventLogTopicConsumer extends ObservableMessageConsumerBase<EventLog> implements EventLog {

	public EventLogTopicConsumer(JmsUtils jms) {
		super(jms, EventLog.JMS_TOPIC);
		log("properly setup");
	}

	@Override
	public void handle(Message message) {
		String eventDescription = message.getStringProperty(EventLog.JMS_PROPERTY_WITH_DESCRIPTION);
		String eventSource = message.getStringProperty(EventLog.JMS_PROPERTY_WITH_SOURCE);
		recordEvent(eventSource, eventDescription);
	}

	@Override
	public void recordEvent(final String eventSource, final String eventDescription) {
		fireEvent(new EventCallback<EventLog>() {
			@Override
			public void process(EventLog listener) {
				listener.recordEvent(eventSource, eventDescription);
			}
		});
	}

}
