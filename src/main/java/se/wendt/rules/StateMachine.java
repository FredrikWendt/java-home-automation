package se.wendt.rules;

public interface StateMachine {

	String getCurrentValue(Variable variable);

	void change(Variable variable, String newState);

}
