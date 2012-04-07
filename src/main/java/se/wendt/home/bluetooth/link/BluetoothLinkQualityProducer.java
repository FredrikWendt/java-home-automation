package se.wendt.home.bluetooth.link;

import java.util.HashMap;
import java.util.Map;

import se.wendt.home.util.ObservableRunnableServiceBase;
import se.wendt.home.util.Thread;

/**
 * Periodically requests link quality from the query endpoint and forwards the quality queried to
 * any listener.
 */
public class BluetoothLinkQualityProducer extends ObservableRunnableServiceBase<BluetoothLinkQualityListener> implements Runnable,
		BluetoothLinkQualityListener {

	private static final int ONE_MINUTE = 60 * 1000;
	private Map<String, Integer> lastStatus = new HashMap<String, Integer>();
	private Map<String, Long> sendLog = new HashMap<String, Long>();
	private final BluetoothLinkQualityQueryEndpointImpl bluetoothLinkQualityQueryEndpoint;

	public BluetoothLinkQualityProducer(BluetoothLinkQualityQueryEndpointImpl bluetoothLinkQualityQueryEndpoint) {
		this.bluetoothLinkQualityQueryEndpoint = bluetoothLinkQualityQueryEndpoint;
		log("properly setup");
	}

	public void addMacToMonitor(String mac) {
		lastStatus.put(mac, -1);
	}

	@Override
	public void linkQualityAnnounced(final String mac, final int linkQuality) {
		super.fireEvent(new EventCallback<BluetoothLinkQualityListener>() {
			@Override
			public void process(BluetoothLinkQualityListener listener) {
				listener.linkQualityAnnounced(mac, linkQuality);
			}
		});
	}

	@Override
	public void run() {
		while (thread != null) {
			waitForNextRun();
			makeARun();
		}
	}

	protected void makeARun() {
		for (String mac : lastStatus.keySet()) {
			try {
				int linkQuality = bluetoothLinkQualityQueryEndpoint.getLinkQuality(mac);
				int previousQuality = lastStatus.get(mac);
				if (linkQualityHasChanged(linkQuality, previousQuality) || itsTimeToSendSameQualityAgain(mac)) {
					lastStatus.put(mac, linkQuality);
					this.linkQualityAnnounced(mac, linkQuality);
				}
			} catch (Throwable t) {
				log("Exception caught - %s", t.getMessage());
				t.printStackTrace();
			}
		}
	}

	private boolean itsTimeToSendSameQualityAgain(String mac) {
		if (sendLog.containsKey(mac)) {
			long now = System.currentTimeMillis();
			long timeOfLastAnnouncement = sendLog.get(mac);
			long timeSinceLastAnnouncement = now - timeOfLastAnnouncement;
			return timeSinceLastAnnouncement > ONE_MINUTE;
		}
		return true;
	}

	private boolean linkQualityHasChanged(int linkQuality, int previousQuality) {
		return linkQuality != previousQuality;
	}

	private void waitForNextRun() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
	}

}
