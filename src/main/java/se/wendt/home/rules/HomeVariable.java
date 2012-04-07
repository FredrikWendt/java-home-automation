package se.wendt.home.rules;

import java.util.Collection;
import java.util.HashSet;

import se.wendt.home.people.PersonPresenceListener;
import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.impl.VariableImpl;

public class HomeVariable implements PersonPresenceListener {

	public static final String IS_NOT_EMPTY = "is not empty";
	public static final String IS_EMPTY = "is empty";
	
	private final StateMachine stateMachine;
	private final Collection<String> peopleHome = new HashSet<String>();
	private final Variable variable = new VariableImpl("home");
	private Boolean lastStateAnnounced;

	public HomeVariable(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	@Override
	public void personCame(String personsName) {
		peopleHome.add(personsName);
		update();
	}

	@Override
	public void personLeft(String personsName) {
		peopleHome.remove(personsName);
		update();
	}

	private void update() {
		boolean empty = peopleHome.isEmpty();
		if (lastStateAnnounced == null || lastStateAnnounced != empty) {
			announce(empty);
		}
	}

	private void announce(boolean empty) {
		lastStateAnnounced = empty;
		stateMachine.change(variable, empty ? IS_EMPTY : IS_NOT_EMPTY);
	}
}
