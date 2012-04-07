package se.wendt.home.bus.impl;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import se.wendt.home.bus.MessageReceiver;
import se.wendt.home.util.LogEnabledBase;

public abstract class MessageConsumerBase extends LogEnabledBase implements MessageReceiver {

	protected MessageConsumer consumer;
	protected Connection connection;
	protected Session session;

	public MessageConsumerBase(JmsUtils jms, String topicName) {
		super();

		log("setting up");
		connection = jms.createConnection();
		session = jms.createAutoAcknowledgeSession(connection);
		Destination jmsTopic = jms.getTopic(topicName);
		consumer = jms.createMessageConsumer(session, jmsTopic);
		try {
			connection.start();
			consumer.setMessageListener(new MessageListenerOneWayBridge(this));
		} catch (JMSException e) {
			throw new RuntimeException("Failed to start JMS Connection", e);
		}
		log("setup complete - listening to " + topicName);
	}
}
