package se.wendt.home.wifi;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import se.wendt.home.util.ObservableRunnableServiceBase;
import se.wendt.home.util.Thread;
import se.wendt.home.wifi.leases.DhcpClients;
import se.wendt.home.wifi.leases.DhcpLease;

public class WifiPresenceMonitor extends ObservableRunnableServiceBase<WifiPresenceListener> {

	private Map<String, DhcpLease> devicesBeingTracked = new TreeMap<String, DhcpLease>();
	private DhcpClients dhcpClients;

	public WifiPresenceMonitor(DhcpClients dhcpClients) {
		super();
		this.dhcpClients = dhcpClients;
	}

	@Override
	public void run() {
		log("wifi monitoring thread started");
		while (thread != null) {
			Set<DhcpLease> dhcpLeases = dhcpClients.getDhcpLeases();
			processActiveDhcpLeases(dhcpLeases);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		log("wifi monitoring thread stopped");
	}

	public void processActiveDhcpLeases(Set<DhcpLease> dhcpLeases) {
		Set<String> devicesPreviouslySeen = getMacAddressesOfMonitoredDevices();
		connectNewDevices(devicesPreviouslySeen, dhcpLeases);
		disconnectLostDevices(devicesPreviouslySeen);
	}

	private Set<String> getMacAddressesOfMonitoredDevices() {
		Set<String> devicesPreviouslySeen = new HashSet<String>();
		devicesPreviouslySeen.addAll(devicesBeingTracked.keySet());
		return devicesPreviouslySeen;
	}

	private void connectNewDevices(Set<String> devicesPreviouslySeen, Set<DhcpLease> dhcpLeases) {
		for (DhcpLease lease : dhcpLeases) {
			String mac = lease.getMacAddress();
			devicesPreviouslySeen.remove(mac);

			if (!devicesBeingTracked.containsKey(mac)) {
				devicesBeingTracked.put(mac, lease);
				fireDeviceConnected(lease);
			}
		}
	}

	private void disconnectLostDevices(Set<String> devicesPreviouslySeen) {
		for (String mac : devicesPreviouslySeen) {
			DhcpLease lease = devicesBeingTracked.remove(mac);
			fireDeviceDisconnected(lease);
		}
	}

	private void fireDeviceConnected(final DhcpLease lease) {
		fireEvent(new EventCallback<WifiPresenceListener>() {
			public void process(WifiPresenceListener listener) {
				listener.deviceConnected(lease);
			}
		});
	}

	private void fireDeviceDisconnected(final DhcpLease lease) {
		fireEvent(new EventCallback<WifiPresenceListener>() {
			public void process(WifiPresenceListener listener) {
				listener.deviceDisconnected(lease);
			}
		});
	}

}
