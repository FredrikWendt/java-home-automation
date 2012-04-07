package se.wendt.home.tellstick;

import java.io.PrintStream;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.tellstick.tdtool.TellstickCommandConsumer;
import se.wendt.home.tellstick.tdtool.TellstickCommander;

public class TellstickStdoutImpl implements TellstickCommander {

	private final PrintStream out;

	public TellstickStdoutImpl(PrintStream out) {
		this.out = out;
	}

	public static void main(String[] args) {
		TellstickStdoutImpl backend = new TellstickStdoutImpl(System.out);
		new TellstickCommandConsumer(new JmsUtils()).addListener(backend);
	}

	public void turnDeviceOff(String deviceName) {
		out.println("off " + deviceName);
	}

	public void turnDeviceOn(String deviceName) {
		out.println("on " + deviceName);
	}

}
