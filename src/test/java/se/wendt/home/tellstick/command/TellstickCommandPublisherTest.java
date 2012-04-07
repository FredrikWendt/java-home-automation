package se.wendt.home.tellstick.command;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import se.wendt.home.tellstick.tdtool.TellstickCommandPublisher;
import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.home.util.JmsUtilsTestBase;

public class TellstickCommandPublisherTest extends JmsUtilsTestBase {

	private TellstickCommandPublisher testee;
	private final String deviceName = "device";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new TellstickCommandPublisher(jms);
	}

	@Test
	public void turnDeviceOffSendsMessageWithActionOff() throws Exception {
		testee.turnDeviceOff(deviceName);

		verify(jmsMessage).setStringProperty(TellstickCommander.JMS_PROPERTY_WITH_DEVICENAME, deviceName);
		verify(jmsMessage).setStringProperty(TellstickCommander.JMS_PROPERTY_WITH_ACTION, "off");
	}

	@Test
	public void turnDeviceOffSendsMessageWithActionOn() throws Exception {
		testee.turnDeviceOn(deviceName);

		verify(jmsMessage).setStringProperty(TellstickCommander.JMS_PROPERTY_WITH_DEVICENAME, deviceName);
		verify(jmsMessage).setStringProperty(TellstickCommander.JMS_PROPERTY_WITH_ACTION, "on");
	}
}
