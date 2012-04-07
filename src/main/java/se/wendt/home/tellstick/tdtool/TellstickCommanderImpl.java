package se.wendt.home.tellstick.tdtool;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import se.wendt.home.tellstick.TellstickQuery;
import se.wendt.home.util.CommandExecutor;
import se.wendt.util.ThreadUtils;

public class TellstickCommanderImpl implements TellstickCommander, TellstickQuery {

	private static final Object COMMAND_EXECUTION_LOCK = new Object();
	/** Milliseconds to wait between executing each command. */
	private static final long TDTOOL_DELAY = 500;
	private final CommandExecutor executor;
	private final Set<String> allDevices = new TreeSet<String>();
	private final Set<String> devicesThatAreOn = new TreeSet<String>();

	public TellstickCommanderImpl(CommandExecutor commandExecutor) {
		this.executor = commandExecutor;
	}

	@Override
	public void turnDeviceOff(String deviceName) {
		synchronized (COMMAND_EXECUTION_LOCK) {
			devicesThatAreOn.remove(deviceName);
			allDevices.add(deviceName);
			executor.execute("tdtool", "-f", deviceName);
			ThreadUtils.sleep(TDTOOL_DELAY);
		}
	}

	@Override
	public void turnDeviceOn(String deviceName) {
		synchronized (COMMAND_EXECUTION_LOCK) {
			devicesThatAreOn.add(deviceName);
			allDevices.add(deviceName);
			executor.execute("tdtool", "-n", deviceName);
			ThreadUtils.sleep(TDTOOL_DELAY);
		}
	}

	@Override
	public Collection<String> listDevicesThatAreOn() {
		return devicesThatAreOn;
	}

	@Override
	public Collection<String> listAllDevices() {
		return allDevices;
	}

}
