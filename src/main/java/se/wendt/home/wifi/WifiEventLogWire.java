package se.wendt.home.wifi;

import se.wendt.home.eventlog.EventLog;
import se.wendt.home.wifi.leases.DhcpLease;

public class WifiEventLogWire implements WifiPresenceListener {

	private final EventLog eventLog;

	public WifiEventLogWire(EventLog eventLog) {
		this.eventLog = eventLog;
	}

	@Override
	public void deviceConnected(DhcpLease lease) {
		eventLog.recordEvent("wifi", String.format("Device connected: %s %s", lease.getClientHostName(), lease.getMacAddress()));
	}

	@Override
	public void deviceDisconnected(DhcpLease lease) {
		eventLog.recordEvent("wifi", String.format("Device disconnected: %s %s", lease.getClientHostName(), lease.getMacAddress()));
	}

}
