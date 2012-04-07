package se.wendt.home.bluetooth;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;
import se.wendt.home.util.Observable;

public class BluetoothDevicePresencePublisher extends MessageProducerBase implements BluetoothDevicePresenceListener {

	public BluetoothDevicePresencePublisher(JmsUtils jms, Observable<BluetoothDevicePresenceListener> presenceProducer) {
		super(jms, JMS_TOPIC);
		presenceProducer.addListener(this);
		log("properly setup");
	}

	@Override
	public void deviceConnected(String macAddress) {
		sendMessage(macAddress, true);
	}

	@Override
	public void deviceDisconnected(String macAddress) {
		sendMessage(macAddress, false);
	}

	private void sendMessage(String macAddress, boolean eventSourceWasConnected) {
		Message message = createMessage();
		message.setStringProperty(JMS_PROPERTY_MACADDRESS, macAddress);
		message.setBooleanProperty(JMS_PROPERTY_CONNECT_EVENT, eventSourceWasConnected);
		send(message);
	}
}
