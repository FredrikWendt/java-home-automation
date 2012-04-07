package se.wendt.home.bus.impl;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import se.wendt.home.util.HomeAutomationException;

/**
 * Utility class that wraps all JMS related base methods.
 */
public class JmsUtils {

	private InitialContext ctx;

	public JmsUtils() {
		bootstrapActiveMQConnection("tcp://hemma.wendt.se:61616");
	}

	public MessageProducer createTopicProducer(Session session, Destination destination) {
		try {
			MessageProducer producer = session.createProducer(destination);
			return producer;
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS producer to %s (on %s)", destination, session);
		}
	}

	public Destination getTopic(String topicName) {
		try {
			Destination jmsTopic = (Destination) ctx.lookup("dynamicTopics/" + topicName);
			return jmsTopic;
		} catch (NamingException e) {
			throw new HomeAutomationException(e, "Failed to lookup JMS topic %s", topicName);
		}
	}

	public Destination getQueue(String queueName) {
		try {
			Destination jmsQueue = (Destination) ctx.lookup("dynamicQueues/" + queueName);
			return jmsQueue;
		} catch (NamingException e) {
			throw new HomeAutomationException(e, "Failed to lookup JMS queue %s", queueName);
		}
	}

	public Session createAutoAcknowledgeSession(Connection connection) {
		try {
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			return session;
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS session");
		}
	}

	public Connection createConnection() {
		Connection connection;
		try {
			connection = getConnectionFactory().createConnection();
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS connection");
		}
		return connection;
	}

	public ConnectionFactory getConnectionFactory() {
		try {
			return (ConnectionFactory) ctx.lookup("ConnectionFactory");
		} catch (NamingException e) {
			throw new HomeAutomationException(e, "Failed to get JNDI handle to JMS connection factory");
		}
	}

	public void bootstrapActiveMQConnection(String urlToProvider) {
		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, urlToProvider);
			ctx = new InitialContext(props);
		} catch (NamingException e) {
			throw new HomeAutomationException(e, "Failed to setup JMS connection factory in JNDI");
		}
	}

	public MessageConsumer createMessageConsumer(Session session, Destination destination) {
		try {
			MessageConsumer consumer = session.createConsumer(destination);
			return consumer;
		} catch (JMSException e) {
			throw new HomeAutomationException(e, "Failed to create JMS consumer, listening to topic %s (on %s)", destination,
					session);
		}
	}

}
