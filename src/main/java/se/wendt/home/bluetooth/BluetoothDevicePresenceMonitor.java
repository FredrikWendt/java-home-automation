package se.wendt.home.bluetooth;

import java.util.HashSet;
import java.util.Set;

import se.wendt.home.bluetooth.link.BluetoothLinkQualityListener;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityProducer;
import se.wendt.home.util.ObservableBase;

/**
 * Consumes (low abstraction) link quality and produces (high abstraction) connected/disconnected
 * notificatoins.
 */
public class BluetoothDevicePresenceMonitor extends ObservableBase<BluetoothDevicePresenceListener> implements
		BluetoothLinkQualityListener {

	private Set<String> bluetoothDevices = new HashSet<String>();
	private Set<String> bluetoothDevicesAboutToBeLost = new HashSet<String>();
	private long connectionsSeen = 0;

	public BluetoothDevicePresenceMonitor(BluetoothLinkQualityProducer bluetoothLinkQualityProducer) {
		bluetoothLinkQualityProducer.addListener(this);
		log("properly setup");
	}

	@Override
	public void linkQualityAnnounced(String mac, int linkQuality) {
		if (linkQuality == -1) {
			deviceDisconnected(mac);
		} else {
			deviceConnected(mac);
		}
	}

	private void deviceConnected(String mac) {
		connectionsSeen++;
		if (!bluetoothDevices.contains(mac)) {
			bluetoothDevices.add(mac);
			if (bluetoothDevicesAboutToBeLost.contains(mac)) {
				bluetoothDevicesAboutToBeLost.remove(mac);
			} else {
				fireDeviceConnected(mac);
			}
		}
	}

	private void deviceDisconnected(String mac) {
		if (bluetoothDevicesAboutToBeLost.contains(mac)) {
			bluetoothDevicesAboutToBeLost.remove(mac);
			fireDeviceDisconnected(mac);
		}
		if (bluetoothDevices.contains(mac)) {
			bluetoothDevices.remove(mac);
			bluetoothDevicesAboutToBeLost.add(mac);
		}
	}

	private void fireDeviceConnected(final String mac) {
		log("Bluetooth device %s connected", mac);
		fireEvent(new EventCallback<BluetoothDevicePresenceListener>() {
			@Override
			public void process(BluetoothDevicePresenceListener listener) {
				listener.deviceConnected(mac);
			}
		});
	}

	private void fireDeviceDisconnected(final String mac) {
		log("Bluetooth device %s disconnected", mac);
		fireEvent(new EventCallback<BluetoothDevicePresenceListener>() {
			@Override
			public void process(BluetoothDevicePresenceListener listener) {
				listener.deviceDisconnected(mac);
			}
		});
	}
}
