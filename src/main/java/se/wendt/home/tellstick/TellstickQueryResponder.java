package se.wendt.home.tellstick;

import static se.wendt.home.tellstick.TellstickQuery.JMS_PROPERTY_WITH_ALL_DEVICES;
import static se.wendt.home.tellstick.TellstickQuery.JMS_PROPERTY_WITH_ON_DEVICES;
import static se.wendt.home.tellstick.TellstickQuery.JMS_QUEUE;

import java.util.Collection;
import java.util.Iterator;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.ResponseBase;

public class TellstickQueryResponder extends ResponseBase {

	private final TellstickQuery backend;

	public TellstickQueryResponder(JmsUtils jms, TellstickQuery backend) {
		super(jms, JMS_QUEUE);
		this.backend = backend;
		log("properly setup");
	}

	@Override
	public void handle(Message request, Message response) {
		response.setStringProperty(JMS_PROPERTY_WITH_ALL_DEVICES, join(backend.listAllDevices()));
		response.setStringProperty(JMS_PROPERTY_WITH_ON_DEVICES, join(backend.listDevicesThatAreOn()));
	}

	private String join(Collection<String> allPeople) {
		if (allPeople.size() == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = allPeople.iterator();
		sb.append(iterator.next());
		while (iterator.hasNext()) {
			sb.append(",");
			sb.append(iterator.next());
		}
		return sb.toString();
	}

}
