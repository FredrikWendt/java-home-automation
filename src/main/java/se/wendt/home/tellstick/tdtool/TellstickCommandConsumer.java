package se.wendt.home.tellstick.tdtool;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.ObservableMessageConsumerBase;

/**
 * Executes commands published on the JMS topic "tellstick".
 */
public class TellstickCommandConsumer extends ObservableMessageConsumerBase<TellstickCommander> implements TellstickCommander {

	public TellstickCommandConsumer(JmsUtils jms) {
		super(jms, TellstickCommander.JMS_TOPIC);
		log("properly setup");
	}

	@Override
	public void handle(Message message) {
		String deviceName = message.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_DEVICENAME);
		String action = message.getStringProperty(TellstickCommander.JMS_PROPERTY_WITH_ACTION);
		log("Got message: %s %s", action, deviceName);
		if ("on".equals(action)) {
			this.turnDeviceOn(deviceName);
		} else {
			this.turnDeviceOff(deviceName);
		}
	}

	@Override
	public void turnDeviceOn(final String deviceName) {
		fireEvent(new EventCallback<TellstickCommander>() {
			@Override
			public void process(TellstickCommander listener) {
				listener.turnDeviceOn(deviceName);
			}
		});
	}

	@Override
	public void turnDeviceOff(final String deviceName) {
		fireEvent(new EventCallback<TellstickCommander>() {
			@Override
			public void process(TellstickCommander listener) {
				listener.turnDeviceOff(deviceName);
			}
		});
	}
}
