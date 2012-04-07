package se.wendt.home.bus.impl;

import javax.jms.MessageListener;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageResponse;

public class MessageListenerTwoWayBridge implements MessageListener {

	private final MessageResponse backend;
	private final ResponseBase base;

	public MessageListenerTwoWayBridge(MessageResponse backend, ResponseBase base) {
		this.backend = backend;
		this.base = base;
	}

	@Override
	public void onMessage(javax.jms.Message inMessage) {
		Message request = new MessageJmsImpl(inMessage);
		Message response = base.createMessage();
		backend.handle(request, response);
		base.sendReply(request, response);
	}

}
