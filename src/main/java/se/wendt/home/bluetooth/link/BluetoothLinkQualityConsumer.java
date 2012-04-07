package se.wendt.home.bluetooth.link;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageReceiver;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.ObservableMessageConsumerBase;

/**
 * Listens to link quality messages from JMS and forwards the events to any listener.
 */
public class BluetoothLinkQualityConsumer extends ObservableMessageConsumerBase<BluetoothLinkQualityListener> implements
		MessageReceiver, BluetoothLinkQualityListener {

	public BluetoothLinkQualityConsumer(JmsUtils jms) {
		super(jms, BluetoothLinkQualityListener.JMS_TOPIC);
	}

	@Override
	public void handle(Message incomingMessage) {
		String mac = incomingMessage.getStringProperty(BluetoothLinkQualityListener.JMS_PROPERTY_MAC);
		Integer linkQuality = incomingMessage.getIntProperty(BluetoothLinkQualityListener.JMS_PROPERTY_LINK_QUALITY);
		linkQualityAnnounced(mac, linkQuality);
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
}
