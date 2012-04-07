package se.wendt.home.state;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.people.PeoplePresenceImpl;
import se.wendt.home.people.PersonPresenceListener;
import se.wendt.home.wifi.leases.DhcpLease;
import se.wendt.home.wifi.leases.DhcpLeaseImpl;

@RunWith(MockitoJUnitRunner.class)
public class PeoplePresenceImplTest {

	private PeoplePresenceImpl testee;
	private String fredriksBluetoothMac = "00:11:...";
	private String fredriksWifiMac = "99:88:...";
	private String fredriksName = "fredrik";

	@Mock
	private ConfigurationManager configuration;
	@Mock
	private PersonPresenceListener listener;

	@Before
	public void setup() throws Exception {
		testee = new PeoplePresenceImpl(configuration) {
			protected List<String> getPeopleToTrack() {
				return asList(fredriksName);
			};

			protected List<String> getDevicesToTrack(String person, String propertySuffix) {
				if (propertySuffix.contains("wifi")) {
					return asList(fredriksWifiMac);
				}
				return asList(fredriksBluetoothMac);
			};
		};
		testee.addListener(listener);
	}

	@Test
	public void personOnlyLeavesOnce() throws Exception {
		verifyNoMoreInteractions(listener);

		testee.deviceConnected(fredriksBluetoothMac);
		verify(listener).personCame(fredriksName);
		verifyNoMoreInteractions(listener);

		testee.deviceDisconnected(fredriksBluetoothMac);
		verify(listener).personLeft(fredriksName);
		verifyNoMoreInteractions(listener);

		testee.deviceDisconnected(fredriksBluetoothMac);
		verifyNoMoreInteractions(listener);
	}

	@Test
	public void aPersonArrivesOnlyOnce() throws Exception {
		testee.deviceConnected(newDhcpLease(fredriksWifiMac));
		verify(listener).personCame(fredriksName);

		testee.deviceConnected(fredriksBluetoothMac);
		verifyNoMoreInteractions(listener);

		// double messages
		testee.deviceConnected(newDhcpLease(fredriksWifiMac));
		testee.deviceConnected(fredriksBluetoothMac);
		verifyNoMoreInteractions(listener);
	}

	@Test
	public void wifiAndBluetooth() throws Exception {
		DhcpLease lease = newDhcpLease(fredriksWifiMac);
		testee.deviceConnected(lease);
		testee.deviceConnected(fredriksBluetoothMac);

		PersonPresenceListener secondListener = mock(PersonPresenceListener.class);
		testee.addListener(secondListener);

		testee.deviceDisconnected(lease);
		verifyZeroInteractions(secondListener);

		testee.deviceDisconnected(lease);
		verifyZeroInteractions(secondListener);

		testee.deviceDisconnected(fredriksBluetoothMac);
		verify(secondListener).personLeft(fredriksName);
		verify(listener).personLeft(fredriksName);
	}

	private DhcpLease newDhcpLease(String mac) {
		DhcpLease lease = new DhcpLeaseImpl("hostname", "ipaddress", mac, "expiration");
		return lease;
	}

	@Test
	public void personIsPresentAfterOneDevicesIsConnected() throws Exception {
		testee.deviceConnected(fredriksBluetoothMac);
		boolean result = testee.personIsHome(fredriksName);
		assertTrue(result);
	}

	@Test
	public void personIsDisconnectedFromStart() throws Exception {
		boolean result = testee.personIsHome(fredriksName);
		assertFalse(result);
	}

}
