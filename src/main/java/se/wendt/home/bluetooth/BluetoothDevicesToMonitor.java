package se.wendt.home.bluetooth;

import java.util.Iterator;

public class BluetoothDevicesToMonitor {

	private static final String[][] devicesToTrack = { 
		{ "Fredrik", "mobil", "30:17:C8:AE:C5:0F" },
		{ "Lisa", "mobil", "90:21:55:A3:B:40" }, 
		{ "Anton", "mobil", "6C:23:B9:CA:64:9A" } };
	private static final String[][] otherDevices = {
		{ "Fredrik", "HBH-DS970", "00:18:13:DB:F4:0C" },
		{ "Fredrik", "HBH-DS980", "00:1C:A4:4A:DD:24" },
		{ "Fredrik", "Laptop", "00:1C:26:FF:19:50" } };

	public String getDescriptionFromMac(String mac) {
		for (String[] pair : devicesToTrack) {
			if (mac.equals(pair[1])) {
				return pair[0];
			}
		}
		return mac;
	}

	public Iterable<String> getAllMacs() {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {

					int index = 0;

					@Override
					public boolean hasNext() {
						return index < devicesToTrack.length;
					}

					@Override
					public String next() {
						return devicesToTrack[index++][2];
					}

					@Override
					public void remove() {
					}
				};
			}
		};
	}
}
