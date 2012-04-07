package se.wendt.home.bus.impl;

import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import se.wendt.home.util.HomeAutomationException;
import se.wendt.home.util.LogEnabledBase;

public class MessageTracker extends LogEnabledBase {

	private static final int TIME_TO_WAIT = 10000;
	public static final String MESSAGE_TRACKER_UUID = "MessageTracker.UUID";
	private final Message message;
	private final String uuid;
	private CountDownLatch responseReceived = new CountDownLatch(1);
	private Message response;

	public MessageTracker(Message message) {
		super();
		this.message = message;
		this.uuid = UUID.randomUUID().toString();
	}

	public Message process(MessageProducer producer) throws JMSException {
		message.setStringProperty(MESSAGE_TRACKER_UUID, uuid);
		message.setJMSExpiration(System.currentTimeMillis() + TIME_TO_WAIT);
		producer.send(message);
		try {
			responseReceived.await(TIME_TO_WAIT, TimeUnit.MILLISECONDS);
			if (response == null) { // timeout
				throw new HomeAutomationException("No response returned in time for correlationId " + uuid );
			}
			return response;
		} catch (InterruptedException e) {
			throw new HomeAutomationException("JMS request response was interrupted " + uuid, e);
		}
	}

	public Message getRequest() {
		return message;
	}
	
	public void setResponse(Message response) {
		log("Saving response to request");
//		logMessageProperties(response);
		if (this.response == null) {
			this.response = response;
		} else {
			log("WHOA, multiple responses to a request!?");
		}
		this.responseReceived.countDown();
	}

	@SuppressWarnings("rawtypes")
	private void logMessageProperties(Message response) {
		try {
			Enumeration propertyNames;
			propertyNames = response.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				log("%s", propertyNames.nextElement());
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public String getKey() {
		return uuid;
	}

	public static String getReplyKey(Message arg0) throws JMSException {
		return arg0.getStringProperty(MESSAGE_TRACKER_UUID);
	}

	public static void setReplyKey(Message reply, String correlationID) throws JMSException {
		reply.setStringProperty(MESSAGE_TRACKER_UUID, correlationID);
	}

}
