package se.wendt.home.tellstick;

import java.util.Collection;

/**
 * Questions that can be asked.
 */
public interface TellstickQuery {
	
	/**
	 * Name of the queue to listen to for questions. :)
	 */
	public static final String JMS_QUEUE = "tellstick";

	/**
	 * Property of incoming JMS messages that contain the name of the file to play.
	 */
	public static final String JMS_PROPERTY_WITH_ALL_DEVICES = "device";

	/**
	 * Property of incoming JMS messages that contain the name of the file to perform (on/off).
	 */
	public static final String JMS_PROPERTY_WITH_ON_DEVICES = "action";

	Collection<String> listDevicesThatAreOn();
	
	Collection<String> listAllDevices();
	
}
