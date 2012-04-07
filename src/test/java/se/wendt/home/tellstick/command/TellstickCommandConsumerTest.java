package se.wendt.home.tellstick.command;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.tellstick.tdtool.TellstickCommandConsumer;
import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.home.util.JmsUtilsTestBase;

public class TellstickCommandConsumerTest extends JmsUtilsTestBase {

	@Mock
	private TellstickCommandConsumer testee;
	@Mock
	private TellstickCommander listener1;
	@Mock
	private TellstickCommander listener2;

	private final String deviceName = "deviceName";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new TellstickCommandConsumer(jms);
		testee.addListener(listener1);
		testee.addListener(listener2);
	}

	@Test
	public void messagesWithActionOnCallsTurnDeviceOn() throws Exception {
		given(message1.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_ACTION)).willReturn("on");
		given(message1.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_DEVICENAME)).willReturn(deviceName);

		testee.handle(message1);

		verify(listener1).turnDeviceOn(deviceName);
	}

	@Test
	public void messagesWithActionOffCallsTurnDeviceOff() throws Exception {
		given(message1.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_ACTION)).willReturn("off");
		given(message1.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_DEVICENAME)).willReturn(deviceName);

		testee.handle(message1);

		verify(listener1).turnDeviceOff(deviceName);
	}

	@Test
	public void turnOnEventIsDispatchedToListeners() throws Exception {
		testee.turnDeviceOn(deviceName);
		verify(listener1).turnDeviceOn(deviceName);
		verify(listener2).turnDeviceOn(deviceName);
	}

	@Test
	public void turnOffEventIsDispatchedToListeners() throws Exception {
		testee.turnDeviceOff(deviceName);
		verify(listener1).turnDeviceOff(deviceName);
		verify(listener2).turnDeviceOff(deviceName);
	}

}
