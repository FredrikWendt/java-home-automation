package se.wendt.home.wifi;

import se.wendt.home.wifi.leases.DhcpLease;

public interface WifiPresenceListener {

	final String JMS_TOPIC = "wifi";

	final String JMS_PROPERTY_HOSTNAME = "hostname";
	final String JMS_PROPERTY_MAC_ADDRESS = "macAddress";
	final String JMS_PROPERTY_IP_ADDRESS = "ipAddress";
	final String JMS_PROPERTY_EXPIRATION = "expiration";

	final String JMS_PROPERTY_CONNECT_EVENT = "sourceEventWasConnect";

	void deviceConnected(DhcpLease lease);

	void deviceDisconnected(DhcpLease lease);

}
