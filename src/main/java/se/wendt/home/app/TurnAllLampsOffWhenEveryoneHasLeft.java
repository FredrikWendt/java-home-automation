package se.wendt.home.app;

import java.util.Collection;

import se.wendt.home.people.PeoplePresenceListener;
import se.wendt.home.tellstick.TellstickQuery;
import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.home.util.Observable;

public class TurnAllLampsOffWhenEveryoneHasLeft implements PeoplePresenceListener {

	private final TellstickCommander tellstickCommander;
	private final TellstickQuery tellstickQuery;
	private Collection<String> lastKnownState;

	public TurnAllLampsOffWhenEveryoneHasLeft(TellstickQuery query, TellstickCommander commander, Observable<PeoplePresenceListener> monitor) {
		this.tellstickCommander = commander;
		this.tellstickQuery = query;
		monitor.addListener(this);
	}

	@Override
	public synchronized void nooneIsHere() {
		// get lamps, turn them off remember state
		lastKnownState = tellstickQuery.listDevicesThatAreOn();
		for (String deviceToTurnOff : lastKnownState) {
			tellstickCommander.turnDeviceOff(deviceToTurnOff);
		}
	}

	@Override
	public synchronized void someoneIsHere() {
		Collection<String> devicesToTurnOn = lastKnownState;
		lastKnownState = null;
		
		if (devicesToTurnOn == null) {
			devicesToTurnOn = tellstickQuery.listAllDevices();
		}
		
		for (String deviceToTurnOn : devicesToTurnOn) {
			tellstickCommander.turnDeviceOn(deviceToTurnOn);
		}
	}
}
