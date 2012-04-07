package se.wendt.home.sound;

public interface Sound {

	/**
	 * Name of the topic to listen to for orders. :)
	 */
	final String JMS_TOPIC = "sound";

	/**
	 * Property of incoming JMS messages that contain the name of the file to play.
	 */
	final String JMS_PROPERTY_WITH_FILENAME = "file";

	void playFile(String string);
}
