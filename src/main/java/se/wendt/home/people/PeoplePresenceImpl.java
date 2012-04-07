package se.wendt.home.people;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import se.wendt.home.bluetooth.BluetoothDevicePresenceListener;
import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.util.ObservableBase;
import se.wendt.home.wifi.WifiPresenceListener;
import se.wendt.home.wifi.leases.DhcpLease;

/**
 * Relies on configuration to track peoples devices and think of presence.
<pre>
people=fredrik,lisa
fredrik.devices.bluetooth=00:11:22:33:44:55
lisa.devices.wifi=99:88:77:33:44:55
</pre>
 */
public class PeoplePresenceImpl extends ObservableBase<PersonPresenceListener> implements BluetoothDevicePresenceListener,
		WifiPresenceListener, PeoplePresenceCommunication {

	private final ConfigurationManager config;
	
	/** Key is person's name, value is list of devices present. */
	private Map<String, Set<String>> personsDevicesThatArePresent = new HashMap<String, Set<String>>();

	/** Key is MAC, value is person's name. */
	private Map<String, String> wifiDevices = new HashMap<String, String>();
	
	/** Key is MAC, value is person's name. */
	private Map<String, String> bluetoothDevices = new HashMap<String, String>();

	public PeoplePresenceImpl(ConfigurationManager config) {
		this.config = config;
		
		setupListOfPeopleToTrack();
		setupDevicesToTrack();
	}

	private void setupDevicesToTrack() {
		for (String person : personsDevicesThatArePresent.keySet()) {
			addDevicesToTrack(person, wifiDevices, "/devices/wifi");
			addDevicesToTrack(person, bluetoothDevices, "/devices/bluetooth");
		}
	}

	private void setupListOfPeopleToTrack() {
		List<String> people = getPeopleToTrack();
		for (String person : people) {
			personsDevicesThatArePresent.put(person, new TreeSet<String>());
		}
	}

	private void addDevicesToTrack(String person, Map<String, String> deviceToPerson, String propertySuffix) {
		List<String> devices = getDevicesToTrack(person, propertySuffix);
		for (String device : devices) {
			deviceToPerson.put(device, person);
		}
	}

	protected List<String> getPeopleToTrack() {
		String configKey = "people";
		return getCommaSeparatedListOfValuesFromConfig(configKey);
	}
	
	protected List<String> getDevicesToTrack(String person, String propertySuffix) {
		String configKey = person + propertySuffix;
		return getCommaSeparatedListOfValuesFromConfig(configKey);
	}

	private List<String> getCommaSeparatedListOfValuesFromConfig(String configKey) {
		String configValue = config.getValue(configKey);
		if (configValue == null) {
			log("WHOPS: config for %s returned null", configKey);
		}
		List<String> configValues = asList(configValue.split(","));
		return configValues;
	}

	@Override
	public void deviceConnected(DhcpLease lease) {
		on(lease.getMacAddress(), wifiDevices);
	}

	@Override
	public void deviceDisconnected(DhcpLease lease) {
		off(lease.getMacAddress(), wifiDevices);
	}
	
	@Override
	public void deviceConnected(String mac) {
		on(mac, bluetoothDevices);
	}

	@Override
	public void deviceDisconnected(String mac) {
		off (mac, bluetoothDevices);
	}

	private void firePersonCame(final String personsName) {
		super.fireEvent(new EventCallback<PersonPresenceListener>() {
			@Override
			public void process(PersonPresenceListener listener) {
				listener.personCame(personsName);
			}
		});
	}

	private void firePersonLeft(final String personsName) {
		super.fireEvent(new EventCallback<PersonPresenceListener>() {
			@Override
			public void process(PersonPresenceListener listener) {
				listener.personLeft(personsName);
			}
		});
	}

	protected void on(String deviceKey, Map<String, String> deviceMap) {
		if (deviceMap.containsKey(deviceKey)) {
			String personsName = deviceMap.get(deviceKey);
			boolean personWasGoneBeforeThisDevice = !personIsHome(personsName);
			Set<String> personsDevices = personsDevicesThatArePresent.get(personsName);
			personsDevices.add(deviceKey);
			if (personWasGoneBeforeThisDevice) {
				firePersonCame(personsName);
			}
		}
	}


	protected void off(String deviceKey, Map<String, String> deviceMap) {
		if (deviceMap.containsKey(deviceKey)) {
			String personsName = deviceMap.get(deviceKey);
			boolean personWasHereBeforeThisDevice = personIsHome(personsName);
			Set<String> personsDevices = personsDevicesThatArePresent.get(personsName);
			personsDevices.remove(deviceKey);
			boolean personIsGoneAfterThisDevice = !personIsHome(personsName);
			if (personWasHereBeforeThisDevice && personIsGoneAfterThisDevice) {
				firePersonLeft(personsName);
			}
		}
	}

	@Override
	public boolean personIsHome(String personsName) {
		boolean nameIsRecognized = personsDevicesThatArePresent.containsKey(personsName);
		boolean atLeastOneDeviceIsPresent = personsDevicesThatArePresent.get(personsName).isEmpty() == false;
		return nameIsRecognized && atLeastOneDeviceIsPresent;
	}

	@Override
	public Collection<String> getPeopleHome() {
		Collection<String> result = new ArrayList<String>();
		for (String aPersonsName : getAllPeople()) {
			if (personIsHome(aPersonsName)) {
				result.add(aPersonsName);
			}
		}
		return result ;
	}

	@Override
	public Collection<String> getAllPeople() {
		return personsDevicesThatArePresent.keySet();
	}

}
