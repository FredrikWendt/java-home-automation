package se.wendt.home.people;

public interface PeoplePresenceListener {

	/**
	 * Fired when there's no one home (but was previously).
	 */
	void nooneIsHere();
	
	/**
	 * Fired when the home was empty, but is not now.
	 */
	void someoneIsHere();
	
}
