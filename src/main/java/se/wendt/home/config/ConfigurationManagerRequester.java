package se.wendt.home.config;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.RequestBase;

public class ConfigurationManagerRequester extends RequestBase implements ConfigurationManager {

	public ConfigurationManagerRequester(JmsUtils jms) {
		super(jms, JMS_TOPIC);
	}

	@Override
	public String getValue(String key) {
		Message request = createQuery();
		request.setStringProperty(JMS_PROPERTY_CONFIGURATION_KEY, key);
		log("Requesting config value %s", key);
		
		Message response = send(request);
		
		String configValue = response.getStringProperty(JMS_PROPERTY_CONFIGURATION_RESPONSE);
		log("Requesting config value %s - got %s", key, configValue);
		return configValue;
	}

}
