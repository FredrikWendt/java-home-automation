package se.wendt.home.people;

import java.util.Arrays;
import java.util.Collection;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.RequestBase;

/**
 * Asks "people present?" questions over the bus.
 */
public class PeoplePresenceQuestioner extends RequestBase implements PeoplePresenceCommunication {

	public PeoplePresenceQuestioner(JmsUtils jms) {
		super(jms, JMS_QUEUE);
	}

	@Override
	public Collection<String> getPeopleHome() {
		Message query = createQuery();
		Message response = send(query);
		String peopleHome = response.getStringProperty(PEOPLE_HOME_PROPERTY_KEY);
		return split(peopleHome);
	}

	@Override
	public Collection<String> getAllPeople() {
		Message query = createQuery();
		Message response = send(query);
		String allPeople = response.getStringProperty(ALL_PEOPLE_PROPERTY_KEY);
		return split(allPeople);
	}

	@Override
	public boolean personIsHome(String personsName) {
		Collection<String> peopleHome = getPeopleHome();
		return peopleHome.contains(personsName);
	}

	private Collection<String> split(String peopleHome) {
		return Arrays.asList(peopleHome.split(","));
	}
}
