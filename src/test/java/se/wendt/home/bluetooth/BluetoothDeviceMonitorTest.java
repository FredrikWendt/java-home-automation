package se.wendt.home.bluetooth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.wendt.home.bluetooth.link.BluetoothLinkQualityProducer;

@RunWith(MockitoJUnitRunner.class)
public class BluetoothDeviceMonitorTest {

	private BluetoothDevicePresenceMonitor testee;

	private String mac = "00:11:22:33:ff:ff";

	@Mock
	private BluetoothLinkQualityProducer qualityProducer;

	@Before
	public void setup() throws Exception {
		testee = new BluetoothDevicePresenceMonitor(qualityProducer);
	}

	@Test
	public void aNewDevicesIsPromoted() throws Exception {
		BluetoothDevicePresenceListener listener = mock(BluetoothDevicePresenceListener.class);
		testee.addListener(listener);

		testee.linkQualityAnnounced(mac, 0);
		verify(listener).deviceConnected(mac);
	}

	@Test
	public void aNewDeviceIsGone() throws Exception {
		BluetoothDevicePresenceListener listener = mock(BluetoothDevicePresenceListener.class);
		testee.addListener(listener);

		testee.linkQualityAnnounced(mac, -1);
		testee.linkQualityAnnounced(mac, -1);
		testee.linkQualityAnnounced(mac, -1);
		verify(listener, never()).deviceConnected(mac);
	}

	@Test
	public void aNewDeviceAppearsAndIsThenGone() throws Exception {
		BluetoothDevicePresenceListener listener = mock(BluetoothDevicePresenceListener.class);
		testee.addListener(listener);

		testee.linkQualityAnnounced(mac, 1);

		// a device must be gone twice to really be gone - bluetooth link quality is a little jumpy
		testee.linkQualityAnnounced(mac, -1);
		verify(listener, never()).deviceDisconnected(mac);

		testee.linkQualityAnnounced(mac, -1);
		verify(listener).deviceDisconnected(mac);
	}

	@Test
	public void aDeviceThatWasLostOneTimeIsNotRediscovered() throws Exception {
		BluetoothDevicePresenceListener listener = mock(BluetoothDevicePresenceListener.class);
		testee.addListener(listener);

		testee.linkQualityAnnounced(mac, 1);
		verify(listener, times(1)).deviceConnected(mac);

		// a device must be gone twice to really be gone - bluetooth link quality is a little jumpy
		testee.linkQualityAnnounced(mac, -1);

		testee.linkQualityAnnounced(mac, 1);
		verify(listener, times(1)).deviceConnected(mac);
	}
}
