package se.wendt.home.bus;

public interface Message {

	void setStringProperty(String key, String value);
	
	String getStringProperty(String key);

	int getIntProperty(String key);

	void setIntProperty(String key, int value);

	boolean getBooleanProperty(String key);
	
	void setBooleanProperty(String key, boolean value);
	
}
