package se.wendt.home.tellstick.tdtool;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;

/**
 * Executes commands published on the JMS topic "tellstick".
 */
public class TellstickCommandPublisher extends MessageProducerBase implements TellstickCommander {

	public TellstickCommandPublisher(JmsUtils jms) {
		super(jms, JMS_TOPIC);
	}

	@Override
	public void turnDeviceOn(String deviceName) {
		sendMessage("on", deviceName);
	}

	@Override
	public void turnDeviceOff(String deviceName) {
		sendMessage("off", deviceName);
	}

	private void sendMessage(String action, String deviceName) {
		Message msg = createMessage();
		msg.setStringProperty(JMS_PROPERTY_WITH_ACTION, action);
		msg.setStringProperty(JMS_PROPERTY_WITH_DEVICENAME, deviceName);
		log("Sending tellstick command %s %s", action, deviceName);
		send(msg);
	}
}
