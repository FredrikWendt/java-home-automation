package se.wendt.home.bluetooth;

import se.wendt.home.eventlog.EventLog;

/**
 * Monitors Bluetooth link status and logs events.
 */
public class BluetoothDeviceEventLogWire implements BluetoothDevicePresenceListener {

	private final EventLog eventLog;

	public BluetoothDeviceEventLogWire(EventLog eventLog) {
		this.eventLog = eventLog;
	}

	@Override
	public synchronized void deviceConnected(String mac) {
		eventLog.recordEvent("bluetooth", String.format("%s connected", mac));
	}

	@Override
	public void deviceDisconnected(String mac) {
		eventLog.recordEvent("bluetooth", String.format("%s disconnected", mac));
	}

}
