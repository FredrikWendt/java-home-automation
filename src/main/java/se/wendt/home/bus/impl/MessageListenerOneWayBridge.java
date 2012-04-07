package se.wendt.home.bus.impl;

import javax.jms.Message;
import javax.jms.MessageListener;

import se.wendt.home.bus.MessageReceiver;

public class MessageListenerOneWayBridge implements MessageListener {

	private final MessageReceiver backend;

	public MessageListenerOneWayBridge(MessageReceiver backend) {
		this.backend = backend;
	}

	@Override
	public void onMessage(Message jmsMessage) {
		MessageJmsImpl message = new MessageJmsImpl(jmsMessage);
		backend.handle(message);
	}

}
