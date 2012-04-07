package se.wendt.home.wiring;

import java.util.Set;
import java.util.TreeSet;

import se.wendt.home.bluetooth.BluetoothDevicePresenceListener;
import se.wendt.home.tellstick.tdtool.TellstickCommander;

/**
 * Monitors Bluetooth link status and turns all lamps off when all bluetooth devices are
 * disconnected.
 */
// FIXME: rewrite to monitor people instead
// TODO: instead of simply turning everything off, remember present state and "pop" state in/out on arrive/leave
public class BluetoothTellstickWire implements BluetoothDevicePresenceListener {

	private final TellstickCommander tellstick;
	private boolean allAreOff = true;
	private Set<String> devicesToTurnOn = new TreeSet<String>();
	private Set<String> devicesToTurnOff = new TreeSet<String>();

	public BluetoothTellstickWire(TellstickCommander tellstick) {
		this.tellstick = tellstick;
	}

	public void addDeviceToAutomaticallyTurnOn(String deviceName) {
		devicesToTurnOn.add(deviceName);
	}

	public void addDeviceToAutomaticallyTurnOff(String deviceName) {
		devicesToTurnOff.add(deviceName);
	}

	@Override
	public synchronized void deviceConnected(String mac) {
		if (allAreOff) {
			for (String deviceName : devicesToTurnOn) {
				tellstick.turnDeviceOn(deviceName);
			}
			allAreOff = false;
		}
	}

	@Override
	public void deviceDisconnected(String mac) {
	}
}
