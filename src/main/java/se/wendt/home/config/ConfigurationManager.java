package se.wendt.home.config;

public interface ConfigurationManager {
	
	String JMS_TOPIC = "config";
	String JMS_PROPERTY_CONFIGURATION_KEY = "configurationKey";
	String JMS_PROPERTY_CONFIGURATION_RESPONSE = "configurationValue";

	String getValue(String key);
	
}
