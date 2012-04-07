package se.wendt.home.people;

import static se.wendt.home.people.PeoplePresenceCommunication.ALL_PEOPLE_PROPERTY_KEY;
import static se.wendt.home.people.PeoplePresenceCommunication.JMS_QUEUE;
import static se.wendt.home.people.PeoplePresenceCommunication.PEOPLE_HOME_PROPERTY_KEY;

import java.util.Collection;
import java.util.Iterator;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.ResponseBase;

/**
 * Answers to "people present" questions.
 */
public class PeoplePresenceAnswerer extends ResponseBase {

	private final PeoplePresenceCommunication backend;

	public PeoplePresenceAnswerer(JmsUtils jms, PeoplePresenceCommunication backend) {
		super(jms, JMS_QUEUE);
		this.backend = backend;
	}

	@Override
	public void handle(Message request, Message response) {
		response.setStringProperty(PEOPLE_HOME_PROPERTY_KEY, join(backend.getPeopleHome()));
		response.setStringProperty(ALL_PEOPLE_PROPERTY_KEY, join(backend.getAllPeople()));
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
