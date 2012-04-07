package se.wendt.home.people;

import java.util.Collection;

/**
 * How to ask questions, and where (bus name (JMS queue)).
 */
public interface PeoplePresenceCommunication {
	
	String JMS_QUEUE = "presence";
	String PEOPLE_HOME_PROPERTY_KEY = "ppl.home";
	String ALL_PEOPLE_PROPERTY_KEY = "ppl.all";

	Collection<String> getPeopleHome();

	Collection<String> getAllPeople();
	
	boolean personIsHome(String personsName);

}
