package se.wendt.home.xmpp;

import se.wendt.home.bluetooth.BluetoothDevicePresenceListener;
import se.wendt.home.tellstick.tdtool.TellstickCommander;

public class CommandHandler {

	private final TellstickCommander tellstick;
	private final BluetoothDevicePresenceListener bluetooth;

	public CommandHandler(TellstickCommander tellstick, BluetoothDevicePresenceListener bluetooth) {
		this.tellstick = tellstick;
		this.bluetooth = bluetooth;
	}

	public void handle(String body) {
		if (body.startsWith("b ")) {
			handleBluetoothCommand(body);
		}
		if (body.startsWith("t ")) {
			handleTellstickCommand(body);
		}
	}

	private void handleTellstickCommand(String body) {
		String[] parts = body.split(" ");
		String deviceName = parts[2];
		if ("on".equals(parts[1])) {
			tellstick.turnDeviceOn(deviceName);
		} else {
			tellstick.turnDeviceOff(deviceName);
		}
	}

	protected void handleBluetoothCommand(String body) {
		String[] parts = body.split(" ");
		String mac = parts[2];
		if ("on".equals(parts[1])) {
			bluetooth.deviceConnected(mac);
		} else {
			bluetooth.deviceDisconnected(mac);
		}
	}
}
