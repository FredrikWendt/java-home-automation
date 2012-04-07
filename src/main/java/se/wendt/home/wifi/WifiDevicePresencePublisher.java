package se.wendt.home.wifi;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;
import se.wendt.home.wifi.leases.DhcpLease;

public class WifiDevicePresencePublisher extends MessageProducerBase implements WifiPresenceListener {

	public WifiDevicePresencePublisher(JmsUtils jms) {
		super(jms, JMS_TOPIC);
	}

	@Override
	public void deviceConnected(DhcpLease lease) {
		sendMessage(lease, true);
	}

	@Override
	public void deviceDisconnected(DhcpLease lease) {
		sendMessage(lease, false);
	}

	private void sendMessage(DhcpLease lease, boolean eventSourceWasConnected) {
		Message message = createMessage();
		message.setStringProperty(JMS_PROPERTY_HOSTNAME, lease.getClientHostName());
		message.setStringProperty(JMS_PROPERTY_MAC_ADDRESS, lease.getMacAddress());
		message.setStringProperty(JMS_PROPERTY_IP_ADDRESS, lease.getIpAddress());
		message.setStringProperty(JMS_PROPERTY_EXPIRATION, lease.getExpiration());
		message.setBooleanProperty(JMS_PROPERTY_CONNECT_EVENT, eventSourceWasConnected);
		send(message);
	}
}
