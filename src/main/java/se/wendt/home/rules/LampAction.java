package se.wendt.home.rules;

import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.rules.VariableChangeEvent;
import se.wendt.rules.VariableChangeEventListener;

public class LampAction implements VariableChangeEventListener {

	private final TellstickCommander backend;

	public LampAction(TellstickCommander backend) {
		this.backend = backend;
	}

	@Override
	public void stateChanged(VariableChangeEvent event) {
		String data = event.getNewValue();
		String[] parts = data.split(" ");
		String deviceName = parts[0];
		String action = parts[1];
		
		if ("on".equals(action)) {
			backend.turnDeviceOn(deviceName);
		} else {
			backend.turnDeviceOff(deviceName);
		}
	}
}
