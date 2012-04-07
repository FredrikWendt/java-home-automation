package se.wendt.rules.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VariableImplTest {

	VariableImpl varA = new VariableImpl("a");
	VariableImpl varB = new VariableImpl("b");
	VariableImpl varA2 = new VariableImpl(" a ");
	VariableImpl varUpperA = new VariableImpl("A");

	@Test(expected = IllegalArgumentException.class)
	public void nullIsAnIllegalName() throws Exception {
		new VariableImpl(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void anEmptyNameIsIllegal() throws Exception {
		new VariableImpl("  \n\t");
	}

	@Test
	public void variableObjectsWithSameNameAreEqual() throws Exception {
		Object alien = new Object();

		assertTrue(varA.equals(varA));
		assertTrue(varA.equals(varA2));
		assertTrue(varA.equals(varUpperA));
		assertTrue(varB.equals(varB));

		assertFalse(varA.equals(varB));
		assertFalse(varA.equals(alien));
	}

	@Test
	public void toStringContainsName() throws Exception {
		assertTrue(varUpperA.toString().contains("a"));
		assertTrue(varA.toString().contains("a"));
		assertTrue(varA2.toString().contains("a"));
		assertTrue(varB.toString().contains("b"));
	}
}
