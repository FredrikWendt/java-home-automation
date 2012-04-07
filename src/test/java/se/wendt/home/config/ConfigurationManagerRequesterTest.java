package se.wendt.home.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.util.JmsUtilsTestBase;

public class ConfigurationManagerRequesterTest extends JmsUtilsTestBase {

	private ConfigurationManagerRequester testee;
	private Message reply;

	public static void main(String[] args) {
		ConfigurationManagerRequester configuration = new ConfigurationManagerRequester(new JmsUtils());
		for (;;) {
			try {
				String[] people = configuration.getValue("people").split(",");
				for (String person : people) {
					System.out.println(person);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new ConfigurationManagerRequester(jms) {
			@Override
			public Message send(Message message) {
				return reply;
			};
		};
	}

	@Test
	public void typicalFlow() throws Exception {
		String value = "value";
		String key = "key";
		reply = createMessageWith(key, value);
		String result = testee.getValue(key);
		assertEquals(value, result);
		verify(jmsMessage).setStringProperty(ConfigurationManager.JMS_PROPERTY_CONFIGURATION_KEY, key);
	}

	private Message createMessageWith(String key, String value) throws JMSException {
		Message msg = mock(Message.class);
		when(msg.getStringProperty(ConfigurationManager.JMS_PROPERTY_CONFIGURATION_KEY)).thenReturn(key);
		when(msg.getStringProperty(ConfigurationManager.JMS_PROPERTY_CONFIGURATION_RESPONSE)).thenReturn(value);
		return msg;
	}
}
