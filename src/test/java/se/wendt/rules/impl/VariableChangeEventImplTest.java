package se.wendt.rules.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import se.wendt.rules.Variable;

public class VariableChangeEventImplTest {

	private Variable variable = new VariableImpl("a variable");

	@Test
	public void equality() throws Exception {
		String newState = "on";
		VariableChangeEventImpl eventVarA1 = new VariableChangeEventImpl(variable, newState);
		VariableChangeEventImpl eventVarA2 = new VariableChangeEventImpl(variable, newState);
		VariableChangeEventImpl eventVarB = new VariableChangeEventImpl(variable, "off");

		VariableChangeEventImpl eventVarNullState = new VariableChangeEventImpl(variable, null);

		assertTrue(eventVarA1.equals(eventVarA1));
		assertTrue(eventVarA1.equals(eventVarA2));
		assertTrue(eventVarNullState.equals(eventVarNullState));

		assertFalse(eventVarA1.equals(eventVarNullState));
		assertFalse(eventVarNullState.equals(eventVarA1));
		assertFalse(eventVarA1.equals(eventVarB));
		assertFalse(eventVarB.equals(eventVarA1));
		assertFalse(eventVarA1.equals(new Object()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void eventMustGetProperVariable() throws Exception {
		new VariableChangeEventImpl(null, null);
	}

	@Test
	public void toStringContainsVariableNameAndNewState() throws Exception {
		String state = "xyz";
		VariableChangeEventImpl event = new VariableChangeEventImpl(variable, state);
		assertTrue("state missing", event.toString().contains(state));
		assertTrue("missing variable name", event.toString().contains(variable.getName()));
	}

}
