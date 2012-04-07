package se.wendt.home.eventlog;

public interface EventLog {

	final String JMS_QUEUE = "eventLog";

	final String JMS_TOPIC = "eventLog";

	final String JMS_PROPERTY_WITH_SOURCE = "eventSource";

	final String JMS_PROPERTY_WITH_DESCRIPTION = "eventDescription";

	void recordEvent(String eventSource, String eventDescription);

}
