package se.wendt.home.wiring;

import se.wendt.home.bluetooth.BluetoothDevicePresenceListener;
import se.wendt.home.sound.Sound;
import se.wendt.home.util.LogEnabledBase;

/**
 * Monitors Bluetooth link status and plays connect/disconnect sounds when new devices are
 * discovered or "lost".
 */
// FIXME: not sure we need this, easily get's chatty
public class BluetoothSoundWire extends LogEnabledBase implements BluetoothDevicePresenceListener {

	private static final String SOUND_DIR = "/srv/wendt/sound/";
	private static final String CONNECT_SOUND = SOUND_DIR + "bt-connect.wav";
	private static final String DISCONNECT_SOUND = SOUND_DIR + "bt-disconnect.wav";

	private final Sound player;

	public BluetoothSoundWire(Sound player) {
		this.player = player;
		log("properly setup");
	}

	@Override
	public void deviceConnected(String mac) {
		player.playFile(CONNECT_SOUND);
	}

	@Override
	public void deviceDisconnected(String mac) {
		player.playFile(DISCONNECT_SOUND);
	}

}
