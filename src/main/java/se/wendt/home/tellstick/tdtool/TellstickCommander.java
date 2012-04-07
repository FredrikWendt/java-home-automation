package se.wendt.home.tellstick.tdtool;

public interface TellstickCommander {
	/**
	 * Name of the topic to listen to for orders. :)
	 */
	public static final String JMS_TOPIC = "tellstick";

	/**
	 * Property of incoming JMS messages that contain the name of the file to play.
	 */
	public static final String JMS_PROPERTY_WITH_DEVICENAME = "device";

	/**
	 * Property of incoming JMS messages that contain the name of the file to perform (on/off).
	 */
	public static final String JMS_PROPERTY_WITH_ACTION = "action";

	void turnDeviceOn(String deviceName);

	void turnDeviceOff(String deviceName);

}
