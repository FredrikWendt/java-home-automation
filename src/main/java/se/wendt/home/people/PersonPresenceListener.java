package se.wendt.home.people;

/**
 * Receives notifications about people coming/leaving.
 */
public interface PersonPresenceListener {

	void personCame(String personsName);
	
	void personLeft(String personsName);
	
}
