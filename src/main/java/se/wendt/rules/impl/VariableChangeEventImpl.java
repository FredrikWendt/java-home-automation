package se.wendt.rules.impl;

import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;

public class VariableChangeEventImpl implements VariableChangeEvent {

	private final Variable variable;
	private final String newValue;

	public VariableChangeEventImpl(Variable variable, String newState) {
		if (variable == null) {
			throw new IllegalArgumentException("A null variabel can't change state");
		}
		this.variable = variable;
		this.newValue = newState;
	}

	@Override
	public Variable getVariable() {
		return variable;
	}

	@Override
	public String getNewValue() {
		return newValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VariableChangeEvent) {
			VariableChangeEvent that = (VariableChangeEvent) obj;
			return equal(this.getVariable(), that.getVariable()) && equal(this.newValue, that.getNewValue());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "VCEvent: " + variable.getName() + "=" + newValue;
	}

	private boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else if (o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}

}
