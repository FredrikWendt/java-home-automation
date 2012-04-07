package se.wendt.home.bluetooth;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageConsumerBase;

/**
 * Reads the bus and publish the same events locally (in-JVM).
 */
public class BluetoothDevicePresenceConsumer extends MessageConsumerBase implements BluetoothDevicePresenceListener {

	private final BluetoothDevicePresenceListener backend;

	public BluetoothDevicePresenceConsumer(JmsUtils jms, BluetoothDevicePresenceListener backend) {
		super(jms, JMS_TOPIC);
		this.backend = backend;
	}

	@Override
	public void deviceConnected(String macAddress) {
		backend.deviceConnected(macAddress);
	}

	@Override
	public void deviceDisconnected(String macAddress) {
		backend.deviceDisconnected(macAddress);
	}

	@Override
	public void handle(Message message) {
		String macAddress = message.getStringProperty(JMS_PROPERTY_MACADDRESS);
		if (message.getBooleanProperty(JMS_PROPERTY_CONNECT_EVENT)) {
			deviceConnected(macAddress);
		} else {
			deviceDisconnected(macAddress);
		}
	}
}
