package se.wendt.home.bus.impl;

import javax.jms.JMSException;

import se.wendt.home.bus.Message;
import se.wendt.home.util.HomeAutomationException;

public class MessageJmsImpl implements Message {

	private final javax.jms.Message backingMessage;

	public MessageJmsImpl(javax.jms.Message backingMessage) {
		this.backingMessage = backingMessage;
	}

	@Override
	public void setStringProperty(String key, String value) {
		try {
			backingMessage.setStringProperty(key, value);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to set string property on bus message %s=%s", key, value);
		}
	}

	@Override
	public String getStringProperty(String key) {
		try {
			return backingMessage.getStringProperty(key);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to get string property on bus message %s", key);
		}
	}

	javax.jms.Message getBackingMessage() {
		return backingMessage;
	}

	@Override
	public int getIntProperty(String key) {
		try {
			return backingMessage.getIntProperty(key);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to get int property on bus message %s", key);
		}
	}

	@Override
	public void setIntProperty(String key, int value) {
		try {
			backingMessage.setIntProperty(key, value);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to set int property on bus message %s=%d", key, value);
		}
	}

	@Override
	public boolean getBooleanProperty(String key) {
		try {
			return backingMessage.getBooleanProperty(key);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to get boolean property on bus message %s", key);
		}
	}

	@Override
	public void setBooleanProperty(String key, boolean value) {
		try {
			backingMessage.setBooleanProperty(key, value);
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to set boolean property on bus message %s=%s", key, value);
		}
	}
}
