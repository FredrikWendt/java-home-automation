package se.wendt.home.bluetooth.link;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;

/**
 * Sends Bluetooth link quality announcements over JMS bus.
 */
public class BluetoothLinkQualityPublisher extends MessageProducerBase implements BluetoothLinkQualityListener {

	public BluetoothLinkQualityPublisher(JmsUtils jms, BluetoothLinkQualityProducer linkQualityProducer) {
		super(jms, BluetoothLinkQualityListener.JMS_TOPIC);
		linkQualityProducer.addListener(this);
		log("properly setup");
	}

	@Override
	public void linkQualityAnnounced(String mac, int linkQuality) {
		Message msg = super.createMessage();
		msg.setStringProperty(BluetoothLinkQualityListener.JMS_PROPERTY_MAC, mac);
		msg.setIntProperty(BluetoothLinkQualityListener.JMS_PROPERTY_LINK_QUALITY, linkQuality);
		log("Sending bluetooth link quality %s %d", mac, linkQuality);
		send(msg);
	}
}
