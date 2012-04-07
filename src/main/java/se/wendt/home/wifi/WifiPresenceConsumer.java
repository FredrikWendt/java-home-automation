package se.wendt.home.wifi;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageConsumerBase;
import se.wendt.home.wifi.leases.DhcpLease;
import se.wendt.home.wifi.leases.DhcpLeaseImpl;

public class WifiPresenceConsumer extends MessageConsumerBase implements WifiPresenceListener {

	private final WifiPresenceListener backend;

	public WifiPresenceConsumer(JmsUtils jms, WifiPresenceListener backend) {
		super(jms, JMS_TOPIC);
		this.backend = backend;
	}

	@Override
	public void deviceConnected(DhcpLease lease) {
		backend.deviceConnected(lease);
	}

	@Override
	public void deviceDisconnected(DhcpLease lease) {
		backend.deviceDisconnected(lease);
	}

	@Override
	public void handle(Message message) {
		DhcpLeaseImpl lease = new DhcpLeaseImpl(message.getStringProperty(JMS_PROPERTY_HOSTNAME),
				message.getStringProperty(JMS_PROPERTY_IP_ADDRESS), message.getStringProperty(JMS_PROPERTY_MAC_ADDRESS),
				message.getStringProperty(JMS_PROPERTY_EXPIRATION));
		if (message.getBooleanProperty(JMS_PROPERTY_CONNECT_EVENT)) {
			deviceConnected(lease);
		} else {
			deviceDisconnected(lease);
		}
	}
}
