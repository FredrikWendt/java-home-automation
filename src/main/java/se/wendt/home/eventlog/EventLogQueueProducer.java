package se.wendt.home.eventlog;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.util.LogEnabledBase;

/*
 * Events are added on the QUEUE!
 */
public class EventLogQueueProducer extends LogEnabledBase implements EventLog {

	private Session session;
	private MessageProducer producer;

	public EventLogQueueProducer(JmsUtils jms) {
		Connection connection = jms.createConnection();
		session = jms.createAutoAcknowledgeSession(connection);
		Destination eventLogQueue = jms.getQueue(JMS_QUEUE);
		producer = jms.createTopicProducer(session, eventLogQueue);
		try {
			connection.start();
		} catch (JMSException e) {
			throw new RuntimeException("Failed to start JMS Connection", e);
		}
		log("properly setup");
	}

	@Override
	public void recordEvent(String eventSource, String eventDescription) {
		sendMessage(eventSource, eventDescription);
	}

	private void sendMessage(String eventSource, String eventDescription) {
		try {
			Message msg = session.createMessage();
			msg.setStringProperty(JMS_PROPERTY_WITH_SOURCE, eventSource);
			msg.setStringProperty(JMS_PROPERTY_WITH_DESCRIPTION, eventDescription);
			log("Sending event log entry %s %s", eventSource, eventDescription);
			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
