package se.wendt.home.app;

import se.wendt.home.bluetooth.BluetoothDevicePresenceConsumer;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.eventlog.EventLog;
import se.wendt.home.eventlog.EventLogQueueProducer;
import se.wendt.home.people.HomeMonitor;
import se.wendt.home.people.PeoplePresenceImpl;
import se.wendt.home.tellstick.TellstickQuery;
import se.wendt.home.tellstick.TellstickQueryRequester;
import se.wendt.home.tellstick.tdtool.TellstickCommandPublisher;
import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.home.wifi.WifiPresenceConsumer;

/**
 * Run on a computer that should monitor the home buses and connect events with other stuff.
 */
public class WiringStation {

	public WiringStation(JmsUtils jms, ConfigurationManager configurationManager) {
		// ---- LOGGING ________________________________________________________

		EventLog eventLog = new EventLogQueueProducer(jms);

		// ---- INPUT __________________________________________________________

		PeoplePresenceImpl people = new PeoplePresenceImpl(configurationManager);
		new BluetoothDevicePresenceConsumer(jms, people);
		new WifiPresenceConsumer(jms, people);

		// ---- OUTPUT _________________________________________________________

		TellstickCommander tellstickCommader = new TellstickCommandPublisher(jms);
		HomeMonitor homeMonitor = new HomeMonitor(people);
		TellstickQuery tellstickQuery = new TellstickQueryRequester(jms);
		new TurnAllLampsOffWhenEveryoneHasLeft(tellstickQuery, tellstickCommader, homeMonitor);
	}
}
