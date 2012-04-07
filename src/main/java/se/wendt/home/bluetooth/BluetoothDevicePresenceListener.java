package se.wendt.home.bluetooth;

/**
 * Notifications about bluetooth devices being connected/disconnected.
 */
// FIXME: change String mac to BluetoothAress or similar
public interface BluetoothDevicePresenceListener {
	
	String JMS_TOPIC = "bluetooth_presence";
	String JMS_PROPERTY_MACADDRESS = "mac_address";
	String JMS_PROPERTY_CONNECT_EVENT = "connected";

	void deviceConnected(String mac);

	void deviceDisconnected(String mac);

}
