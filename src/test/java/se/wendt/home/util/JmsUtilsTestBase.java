package se.wendt.home.util;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.mockito.Mock;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageJmsImpl;

public class JmsUtilsTestBase {

	protected se.wendt.home.bus.Message message1;
	@Mock
	protected JmsUtils jms;
	@Mock
	protected Connection connection;
	@Mock
	protected Session session;
	@Mock
	protected MessageConsumer messageConsumer;
	@Mock
	protected MessageProducer messageProducer;
	@Mock
	protected Message jmsMessage;

	public void prepareJmsUtilsMock() throws JMSException {
		when(jms.createConnection()).thenReturn(connection);
		when(jms.createAutoAcknowledgeSession(connection)).thenReturn(session);
		when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);

		when(jms.createMessageConsumer(any(Session.class), any(Destination.class))).thenReturn(messageConsumer);
		when(jms.createTopicProducer(any(Session.class), any(Destination.class))).thenReturn(messageProducer);

		when(session.createMessage()).thenReturn(jmsMessage);
		message1 = new MessageJmsImpl(jmsMessage);
	}
}
