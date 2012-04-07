package se.wendt.home.bluetooth;

import static org.mockito.Mockito.verify;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.wendt.home.bluetooth.link.BluetoothLinkQualityListener;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityProducer;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityPublisher;
import se.wendt.home.util.JmsUtilsTestBase;

@RunWith(MockitoJUnitRunner.class)
public class BluetoothLinkQualityPublisherTest extends JmsUtilsTestBase {

	private BluetoothLinkQualityPublisher testee;

	@Mock
	private BluetoothLinkQualityProducer qualityProducer;

	@Before
	public void setUp() throws JMSException {
		prepareJmsUtilsMock();

		testee = new BluetoothLinkQualityPublisher(jms, qualityProducer);
	}

	@Test
	public void linkQualityIsSentOntoJmsTopic() throws Exception {
		final int linkQuality = 101;
		final String mac = "00:11:22:33:44:55";

		testee.linkQualityAnnounced(mac, linkQuality);

		verify(session).createMessage();
		verify(jmsMessage).setStringProperty(BluetoothLinkQualityListener.JMS_PROPERTY_MAC, mac);
		verify(jmsMessage).setIntProperty(BluetoothLinkQualityListener.JMS_PROPERTY_LINK_QUALITY, linkQuality);
	}

}
