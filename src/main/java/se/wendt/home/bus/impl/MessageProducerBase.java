package se.wendt.home.bus.impl;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageSender;
import se.wendt.home.util.HomeAutomationException;
import se.wendt.home.util.LogEnabledBase;

public abstract class MessageProducerBase extends LogEnabledBase implements MessageSender {

	private MessageProducer producer;
	private Connection connection;
	private Session session;

	public MessageProducerBase(JmsUtils jms, String topicName) {
		log("setting up");
		connection = jms.createConnection();
		session = jms.createAutoAcknowledgeSession(connection);
		Destination jmsTopic = jms.getTopic(topicName);
		producer = jms.createTopicProducer(session, jmsTopic);
		try {
			connection.start();
		} catch (JMSException e) {
			throw new HomeAutomationException("Failed to start JMS Connection", e);
		}
		log("setup complete - talking to " + topicName);
	}

	public Message createMessage() {
		try {
			return new MessageJmsImpl(session.createMessage());
		} catch (JMSException e) {
			throw new HomeAutomationException("Failed to create JMS Message", e);
		}
	}

	public void send(Message message) {
		try {
			message.setStringProperty("producer", getLogName());
			producer.send(((MessageJmsImpl) message).getBackingMessage());
		} catch (JMSException e) {
			throw new HomeAutomationException("Failed to send JMS Message", e);
		}
	}
}
