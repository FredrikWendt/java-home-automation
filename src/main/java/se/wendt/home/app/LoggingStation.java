package se.wendt.home.app;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.eventlog.EventLog;
import se.wendt.home.eventlog.EventLogMailDigestImpl;
import se.wendt.home.eventlog.EventLogQueueToTopic;
import se.wendt.home.eventlog.EventLogStdoutImpl;
import se.wendt.home.eventlog.EventLogTopicConsumer;
import se.wendt.home.util.PlainTextSender;

/**
 * Takes events of the event queue, saves them but also puts a copy of each on the event topic.
 */
public class LoggingStation {

	public LoggingStation(JmsUtils jms) {
		// log events to stdout
		EventLog stdout = new EventLogStdoutImpl();
		EventLogTopicConsumer topicConsumer = new EventLogTopicConsumer(jms);
		topicConsumer.addListener(stdout);

		// log events to mail
		EventLogMailDigestImpl mailDigest = new EventLogMailDigestImpl(new PlainTextSender());
		topicConsumer.addListener(mailDigest);

		// TODO: log events to couch
		// TODO: log events to jdbc

		// pull events from queue and republish on topic
		new EventLogQueueToTopic(jms);
	}
	
	public static void main(String[] args) {
		new LoggingStation(new JmsUtils());
	}
}
