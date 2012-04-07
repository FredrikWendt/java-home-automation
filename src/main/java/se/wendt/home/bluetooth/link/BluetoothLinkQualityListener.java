package se.wendt.home.bluetooth.link;

public interface BluetoothLinkQualityListener {

	String JMS_TOPIC = "bluetooth-link";
	String JMS_PROPERTY_MAC = "MAC";
	String JMS_PROPERTY_LINK_QUALITY = "linkQuality";

	void linkQualityAnnounced(String mac, int linkQuality);

}
