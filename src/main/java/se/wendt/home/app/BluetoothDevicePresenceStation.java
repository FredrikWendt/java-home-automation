package se.wendt.home.app;

import se.wendt.home.bluetooth.BluetoothDevicePresenceMonitor;
import se.wendt.home.bluetooth.BluetoothDevicePresencePublisher;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityProducer;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityPublisher;
import se.wendt.home.bluetooth.link.BluetoothLinkQualityQueryEndpointImpl;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.util.CommandExecutorImpl;

/**
 * Monitors bluetooth devices for presence announcing devices coming and going.
 */
public class BluetoothDevicePresenceStation {

	public BluetoothDevicePresenceStation(JmsUtils jms, ConfigurationManager configurationManager) {
		BluetoothLinkQualityProducer lowLevelEngine = setupLowLevelEngine();

		new BluetoothLinkQualityPublisher(jms, lowLevelEngine);
		
		setupDeviceMonitoring(jms, lowLevelEngine);
		
		configureLowLevelEngine(configurationManager, lowLevelEngine);
		lowLevelEngine.start();
	}

	private void setupDeviceMonitoring(JmsUtils jms, BluetoothLinkQualityProducer linkQualityProducer) {
		BluetoothDevicePresenceMonitor deviceMonitor = new BluetoothDevicePresenceMonitor(linkQualityProducer);
		new BluetoothDevicePresencePublisher(jms, deviceMonitor);
	}

	private void configureLowLevelEngine(ConfigurationManager configurationManager,
			BluetoothLinkQualityProducer linkQualityProducer) {
		String people = configurationManager.getValue("people");
		for (String person : people.split(",")) {
			String devices = configurationManager.getValue(person + "/devices/bluetooth");
			if (devices != null) {
				for (String mac : devices.split(",")) {
					linkQualityProducer.addMacToMonitor(mac);
				}
			}
		}
	}

	private BluetoothLinkQualityProducer setupLowLevelEngine() {
		CommandExecutorImpl commandExecutor = new CommandExecutorImpl();
		BluetoothLinkQualityQueryEndpointImpl linkQualityEndpoint = new BluetoothLinkQualityQueryEndpointImpl(commandExecutor);
		BluetoothLinkQualityProducer engine = new BluetoothLinkQualityProducer(linkQualityEndpoint);
		return engine;
	}

}
