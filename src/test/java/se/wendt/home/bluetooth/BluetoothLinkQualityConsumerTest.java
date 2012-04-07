package se.wendt.home.bluetooth;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.bluetooth.link.BluetoothLinkQualityConsumer;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityListener;
import se.wendt.home.bus.Message;
import se.wendt.home.util.JmsUtilsTestBase;

public class BluetoothLinkQualityConsumerTest extends JmsUtilsTestBase {

	@Mock
	private BluetoothLinkQualityListener listener;
	@Mock
	private BluetoothLinkQualityListener listener2;
	@Mock
	private Message incomingMessage;

	private String mac = "00:11:22:33:44:55";
	private int linkQuality = 123;

	private BluetoothLinkQualityConsumer testee;

	@Before
	public void setUp() throws JMSException {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new BluetoothLinkQualityConsumer(jms);
		testee.addListener(listener);
		testee.addListener(listener2);
	}

	@Test
	public void aProperlyCraftedMessageIsPassedOnToListeners() throws Exception {
		given(incomingMessage.getStringProperty(BluetoothLinkQualityListener.JMS_PROPERTY_MAC)).willReturn(mac);
		given(incomingMessage.getIntProperty(BluetoothLinkQualityListener.JMS_PROPERTY_LINK_QUALITY)).willReturn(
				linkQuality);

		testee.handle(incomingMessage);

		verify(listener).linkQualityAnnounced(mac, linkQuality);
		verify(listener2).linkQualityAnnounced(mac, linkQuality);
	}
}
