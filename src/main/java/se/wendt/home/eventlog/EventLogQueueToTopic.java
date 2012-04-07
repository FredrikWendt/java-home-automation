package se.wendt.home.eventlog;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;
import se.wendt.home.util.HomeAutomationException;
import se.wendt.home.util.ObservableBase;

/**
 * Takes events off the JMS queue and resends them to the topic.
 */
public class EventLogQueueToTopic extends MessageProducerBase implements EventLog {

	public EventLogQueueToTopic(JmsUtils jms) {
		super(jms, EventLog.JMS_TOPIC);
		new EventLogQueueConsumer(jms).addListener(this);
		log("properly setup");
	}

	@Override
	public void recordEvent(String eventSource, String eventDescription) {
		se.wendt.home.bus.Message msg = createMessage();
		msg.setStringProperty(JMS_PROPERTY_WITH_SOURCE, eventSource);
		msg.setStringProperty(JMS_PROPERTY_WITH_DESCRIPTION, eventDescription);
		log("Sending event log entry %s %s to topic", eventSource, eventDescription);
		send(msg);
	}

	class EventLogQueueConsumer extends ObservableBase<EventLog> implements EventLog, MessageListener {

		private MessageConsumer consumer;
		private Connection connection;

		public EventLogQueueConsumer(JmsUtils jms) {
			connection = jms.createConnection();
			Session session = jms.createAutoAcknowledgeSession(connection);
			Destination queue = jms.getQueue(EventLog.JMS_QUEUE);
			consumer = jms.createMessageConsumer(session, queue);
			try {
				consumer.setMessageListener(this);
				connection.start();
			} catch (JMSException e) {
				throw new HomeAutomationException(e, "Failed to set JMS message listener");
			}
			log("properly setup");
		}

		@Override
		public void onMessage(Message message) {
			try {
				String eventDescription = message.getStringProperty(EventLog.JMS_PROPERTY_WITH_DESCRIPTION);
				String eventSource = message.getStringProperty(EventLog.JMS_PROPERTY_WITH_SOURCE);
				this.recordEvent(eventSource, eventDescription);
				message.acknowledge();
			} catch (JMSException e) {
				throw new HomeAutomationException(e, "Failed to get device name and action from message %s", message);
			}
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
}
