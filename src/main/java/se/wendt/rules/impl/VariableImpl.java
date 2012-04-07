package se.wendt.rules.impl;

import se.wendt.rules.Variable;

public class VariableImpl implements Variable {

	private final String name;

	public VariableImpl(String name) {
		if (name == null) {
			throw new IllegalArgumentException("A variable name cannot be null");
		}
		this.name = name.trim().toLowerCase();
		if (this.name.length() == 0) {
			throw new IllegalArgumentException("A variable name cannot be an empty string");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VariableImpl) {
			VariableImpl that = (VariableImpl) obj;
			return name.equals(that.getName());
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Variable " + name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
