package se.wendt.home.bluetooth;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BluetoothDeviceRegistryTest {

	private BluetoothDevicesToMonitor testee = new BluetoothDevicesToMonitor();

	@Test
	public void makeSureWeGetProperMacAddresses() throws Exception {
		for (String mac : testee.getAllMacs()) {
			assertTrue(mac.contains(":"));
		}
	}

}
