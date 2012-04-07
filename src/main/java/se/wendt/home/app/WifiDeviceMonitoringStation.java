package se.wendt.home.app;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.wifi.WifiDevicePresencePublisher;
import se.wendt.home.wifi.WifiPresenceMonitor;
import se.wendt.home.wifi.leases.DhcpClients;
import se.wendt.home.wifi.leases.LinksysReader;

/**
 * Run from command line.
 */
public class WifiDeviceMonitoringStation {

	public WifiDeviceMonitoringStation(JmsUtils jms, ConfigurationManager configurationManager) {
		DhcpClients dhcpClients = new LinksysReader(configurationManager);
		WifiPresenceMonitor monitor = new WifiPresenceMonitor(dhcpClients);
		
		WifiDevicePresencePublisher publisher = new WifiDevicePresencePublisher(jms);
		monitor.addListener(publisher);
		
		monitor.start();
	}
}
