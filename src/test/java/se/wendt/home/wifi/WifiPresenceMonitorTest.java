package se.wendt.home.wifi;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.wendt.home.util.Thread;
import se.wendt.home.wifi.leases.DhcpClients;
import se.wendt.home.wifi.leases.DhcpLease;
import se.wendt.home.wifi.leases.DhcpLeaseImpl;

@RunWith(MockitoJUnitRunner.class)
public class WifiPresenceMonitorTest {

	private WifiPresenceMonitor testee;

	private String macAddress = "aa:bb:cc:...";
	private DhcpLease lease = new DhcpLeaseImpl(macAddress, macAddress, macAddress, macAddress);

	@Mock
	private WifiPresenceListener listener;
	@Mock
	private DhcpClients dhcpClients;

	@Before
	public void setUp() {
		testee = new WifiPresenceMonitor(dhcpClients);
		testee.addListener(listener);
		// when(lease.getMacAddress()).thenReturn(macAddress);
	}

	@Test
	public void deviceConnected() throws Exception {
		Set<DhcpLease> dhcpLeases = createSet(lease);
		testee.processActiveDhcpLeases(dhcpLeases);
		verify(listener).deviceConnected(lease);
	}

	private Set<DhcpLease> createSet(DhcpLease lease) {
		Set<DhcpLease> set = new TreeSet<DhcpLease>();
		set.add(lease);
		return set;
	}

	@Test
	public void deviceDisconnected() throws Exception {
		Set<DhcpLease> dhcpLeases = createSet(lease);
		testee.processActiveDhcpLeases(dhcpLeases);
		testee.processActiveDhcpLeases(new TreeSet<DhcpLease>());
		verify(listener).deviceDisconnected(lease);
	}

	@Test
	public void deviceIsOnlyConnectedOnce() throws Exception {
		Set<DhcpLease> dhcpLeases = createSet(lease);
		testee.processActiveDhcpLeases(dhcpLeases);
		testee.processActiveDhcpLeases(dhcpLeases);
		testee.processActiveDhcpLeases(dhcpLeases);
		verify(listener).deviceConnected(lease);
	}

	@Test
	public void deviceIsDisconnectedOnlyOnce() throws Exception {
		Set<DhcpLease> dhcpLeases = createSet(lease);
		testee.processActiveDhcpLeases(dhcpLeases);
		testee.processActiveDhcpLeases(new TreeSet<DhcpLease>());
		testee.processActiveDhcpLeases(new TreeSet<DhcpLease>());
		testee.processActiveDhcpLeases(new TreeSet<DhcpLease>());
		verify(listener).deviceDisconnected(lease);
	}

	@Test
	public void deviceConnectedIsCalledProperly() throws Exception {
		Set<DhcpLease> leases = createSet(lease);
		given(dhcpClients.getDhcpLeases()).willReturn(leases);

		// we expect the thread to get leases
		testee.start();
		Thread.sleep(1000);
		testee.stop();

		verify(listener).deviceConnected(lease);
	}

}
