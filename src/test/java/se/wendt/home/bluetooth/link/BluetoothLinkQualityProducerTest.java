package se.wendt.home.bluetooth.link;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class BluetoothLinkQualityProducerTest {

	private final String mac = "00:11:22:33:44:55";

	@Mock
	private BluetoothLinkQualityQueryEndpointImpl bluetoothLinkQualityQueryEndpoint;
	@Mock
	private BluetoothLinkQualityListener listener;

	private BluetoothLinkQualityProducer testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new BluetoothLinkQualityProducer(bluetoothLinkQualityQueryEndpoint);
		testee.addListener(listener);
	}

	@After
	public void after() {
		testee.stop();
	}

	@Test
	public void linkQualityAnnouncementsArePassedOnToListeners() throws Exception {
		BluetoothLinkQualityListener listener2 = mock(BluetoothLinkQualityListener.class);
		testee.addListener(listener2);
		int linkQuality = 100;

		testee.linkQualityAnnounced(mac, linkQuality);

		verify(listener).linkQualityAnnounced(mac, linkQuality);
		verify(listener2).linkQualityAnnounced(mac, linkQuality);
	}

	@Test
	public void typicalFlow() throws Exception {
		int linkQuality = 101;

		given(bluetoothLinkQualityQueryEndpoint.getLinkQuality(mac)).willReturn(linkQuality);
		testee.addMacToMonitor(mac);

		testee.makeARun();

		verify(bluetoothLinkQualityQueryEndpoint).getLinkQuality(mac);
		verify(listener).linkQualityAnnounced(mac, linkQuality);
	}
}
