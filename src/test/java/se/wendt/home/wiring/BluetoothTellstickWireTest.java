package se.wendt.home.wiring;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.tellstick.tdtool.TellstickCommander;

public class BluetoothTellstickWireTest {

	private final String mac = "00:11:22:33:44:55";
	private final String onDevice = "on";
	private final String offDevice = "off";

	@Mock
	TellstickCommander tellstick;

	private BluetoothTellstickWire testee;

	@Before
	public void setup() {
		initMocks(this);
		BluetoothTellstickWire wire = new BluetoothTellstickWire(tellstick);
		wire.addDeviceToAutomaticallyTurnOn(onDevice);
		wire.addDeviceToAutomaticallyTurnOff(offDevice);
		testee = wire;
	}

	@Test
	public void turnOnDeviceOnWhenADeviceConnects() throws Exception {
		testee.deviceConnected(mac);
		verify(tellstick).turnDeviceOn(onDevice);
	}
}
