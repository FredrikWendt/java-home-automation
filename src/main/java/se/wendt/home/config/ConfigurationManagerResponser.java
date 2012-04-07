package se.wendt.home.config;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageTracker;
import se.wendt.home.bus.impl.ResponseBase;

public class ConfigurationManagerResponser extends ResponseBase {

	private final ConfigurationManager backend;

	public ConfigurationManagerResponser(JmsUtils jms, ConfigurationManager backend) {
		super(jms, ConfigurationManager.JMS_TOPIC);
		this.backend = backend;
		log("properly setup");
	}

	@Override
	public void handle(Message request, Message response) {
		String key = request.getStringProperty(ConfigurationManager.JMS_PROPERTY_CONFIGURATION_KEY);
		String value = backend.getValue(key);
		log("returning %s=%s for %s", key, value, request.getStringProperty(MessageTracker.MESSAGE_TRACKER_UUID));
		response.setStringProperty(ConfigurationManager.JMS_PROPERTY_CONFIGURATION_RESPONSE, value);
	}
}
