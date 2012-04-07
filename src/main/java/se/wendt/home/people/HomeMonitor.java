package se.wendt.home.people;

import java.util.Set;
import java.util.TreeSet;

import se.wendt.home.util.Observable;
import se.wendt.home.util.ObservableBase;

public class HomeMonitor extends ObservableBase<PeoplePresenceListener> implements PersonPresenceListener {

	private Set<String> peopleAtHome = new TreeSet<String>();
	private boolean atLeastOnePersonHasAlreadyBeenSeen = false;

	public HomeMonitor(Observable<PersonPresenceListener> ppl) {
		ppl.addListener(this);
	}

	@Override
	public void personCame(String personsName) {
		boolean homeWasEmpty = peopleAtHome.isEmpty();
		peopleAtHome.add(personsName);
		if (homeWasEmpty && atLeastOnePersonHasAlreadyBeenSeen) {
			fireSomeOneReturned();
		}
		atLeastOnePersonHasAlreadyBeenSeen = true;
	}

	@Override
	public void personLeft(String personsName) {
		boolean houseWasNotEmpty = !peopleAtHome.isEmpty();
		peopleAtHome.remove(personsName);
		boolean houseIsNowEmpty = peopleAtHome.isEmpty();
		if (houseIsNowEmpty && houseWasNotEmpty) {
			fireNoOneIsHere();
		}
	}

	private void fireSomeOneReturned() {
		super.fireEvent(new EventCallback<PeoplePresenceListener>() {
			@Override
			public void process(PeoplePresenceListener listener) {
				listener.someoneIsHere();
			}
		});
	}

	private void fireNoOneIsHere() {
		super.fireEvent(new EventCallback<PeoplePresenceListener>() {
			@Override
			public void process(PeoplePresenceListener listener) {
				listener.nooneIsHere();
			}
		});
	}
}
