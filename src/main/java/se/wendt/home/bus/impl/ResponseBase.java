package se.wendt.home.bus.impl;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageResponse;
import se.wendt.home.util.HomeAutomationException;
import se.wendt.home.util.LogEnabledBase;

public abstract class ResponseBase extends LogEnabledBase implements MessageResponse {

	protected MessageConsumer consumer;
	protected Connection connection;
	protected Session session;
	private final String queueName;

	public ResponseBase(JmsUtils jms, String queueName) {
		super();
		this.queueName = queueName;
		log("setting up");
		connection = jms.createConnection();
		session = jms.createAutoAcknowledgeSession(connection);
		Destination messagePipe = jms.getQueue(queueName);
		consumer = jms.createMessageConsumer(session, messagePipe);
		try {
			connection.start();
			consumer.setMessageListener(new MessageListenerTwoWayBridge(this, this));
		} catch (JMSException e) {
			throw new RuntimeException("Failed to start JMS Connection", e);
		}
		log("setup complete - consuming %s", queueName);
	}

	public Message createMessage() {
		try {
			return new MessageJmsImpl(session.createMessage());
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS Message %s", queueName);
		}
	}

	public void sendReply(Message request, Message response) {
		javax.jms.Message inMessage = ((MessageJmsImpl) request).getBackingMessage();
		javax.jms.Message outMessage = ((MessageJmsImpl) response).getBackingMessage();
		
		Destination jmsReplyTo = null;
		try {
			jmsReplyTo = inMessage.getJMSReplyTo();
			MessageTracker.setReplyKey(outMessage, MessageTracker.getReplyKey(inMessage));
			MessageProducer producer = session.createProducer(jmsReplyTo);
			producer.send(outMessage);
			producer.close();
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to send response message in %s, out %s", queueName, jmsReplyTo);
		}
	}

}
