package se.wendt.home.bus.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import se.wendt.home.bus.MessageQuery;
import se.wendt.home.util.HomeAutomationException;
import se.wendt.home.util.LogEnabledBase;

public abstract class RequestBase extends LogEnabledBase implements MessageQuery, MessageListener {

	private MessageProducer producer;
	private Connection connection;
	private Session session;
	private Destination replyToQueue;
	private MessageConsumer consumer;
	private Map<String, MessageTracker> trackers = new HashMap<String, MessageTracker>();
	private final String replytoQueueName;
	private final String queueName;

	public RequestBase(JmsUtils jms, String queueName) {
		this.queueName = queueName;
		log("setting up");
		connection = jms.createConnection();
		session = jms.createAutoAcknowledgeSession(connection);
		Destination jmsTopic = jms.getQueue(queueName);
		replytoQueueName = queueName + "_reply_"+UUID.randomUUID().toString();
		replyToQueue = jms.getQueue(replytoQueueName);
		producer = jms.createTopicProducer(session, jmsTopic);
		consumer = jms.createMessageConsumer(session, replyToQueue);
		try {
			connection.start();
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to start JMS Connection /%s, %s)", queueName, replytoQueueName);
		}
		log("setup complete - talking to %s, %s", queueName, replytoQueueName);
	}

	@Override
	public se.wendt.home.bus.Message createQuery() {
		try {
			return new MessageJmsImpl(session.createMessage());
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS Message (%s, %s)", queueName, replytoQueueName);
		}
	}

	@Override
	public se.wendt.home.bus.Message send(se.wendt.home.bus.Message query) {
		Message message = ((MessageJmsImpl) query).getBackingMessage();
		MessageTracker tracker = new MessageTracker(message);
		try {
			message.setStringProperty("producer", getLogName());
			message.setJMSReplyTo(replyToQueue);
			registerTracker(tracker);
			return new MessageJmsImpl(tracker.process(producer));
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to send JMS Message (%s, %s)", queueName, replytoQueueName);
		} finally {
			unregisterTracker(tracker);
		}
	}

	private void unregisterTracker(MessageTracker tracker) {
		trackers.remove(tracker.getKey());
	}

	private void registerTracker(MessageTracker tracker) throws JMSException {
		trackers.put(tracker.getKey(), tracker);
	}

	/**
	 * We're getting a reply, match it with the request.
	 */
	@Override
	public void onMessage(Message message) {
		MessageTracker tracker;
		try {
			String correlationID = MessageTracker.getReplyKey(message);
			tracker = trackers.get(correlationID);
			if (tracker == null) {
				log("Received response which I can't track - unknown correlationId %s!", correlationID);
				return;
			}
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to handle JMS Message (%s, %s)", queueName, replytoQueueName);
		}
		try {
			tracker.setResponse(message);
		} catch (Throwable e) {
			throw new HomeAutomationException(e, "Failed to save response to request (%s, %s)", queueName, replytoQueueName);
		}
	}
}
