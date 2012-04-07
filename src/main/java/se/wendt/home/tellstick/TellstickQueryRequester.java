package se.wendt.home.tellstick;

import java.util.ArrayList;
import java.util.Collection;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.RequestBase;

public class TellstickQueryRequester extends RequestBase implements TellstickQuery {

	public TellstickQueryRequester(JmsUtils jms) {
		super(jms, JMS_QUEUE);
		log("properly setup");
	}

	@Override
	public Collection<String> listDevicesThatAreOn() {
		return sendQueryAndReturnCommaSeparatedValues(JMS_PROPERTY_WITH_ON_DEVICES);
	}

	@Override
	public Collection<String> listAllDevices() {
		return sendQueryAndReturnCommaSeparatedValues(JMS_PROPERTY_WITH_ALL_DEVICES);
	}

	private Collection<String> sendQueryAndReturnCommaSeparatedValues(String propertyToWorkWith) {
		Message response = send(createQuery());
		String raw = response.getStringProperty(propertyToWorkWith);
		String[] devices = raw.split(",");
		Collection<String> result = new ArrayList<String>();
		for (String device : devices) {
			if (device.trim().length() > 0) {
				result.add(device.trim());
			}
		}
		return result;
	}

}
