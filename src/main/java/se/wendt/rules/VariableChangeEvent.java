package se.wendt.rules;

public interface VariableChangeEvent {

	Variable getVariable();
	
	String getNewValue();
	
}
