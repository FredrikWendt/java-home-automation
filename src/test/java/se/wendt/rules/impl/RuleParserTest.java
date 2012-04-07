package se.wendt.rules.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import se.wendt.rules.Variable;

public class RuleParserTest {

	Variable home = new VariableImpl("home");
	Variable lamp = new VariableImpl("lamp");
	RuleImpl rule = mock(RuleImpl.class);

	@Test
	public void minimalRuleTest() throws Exception {
		String ruleDefinition = "rule X\n" + //
				"when\n" + //
				"home is empty\n" + //
				"then\n" + //
				"set lamp 1 off";
		new RuleParser(ruleDefinition, rule);

		verify(rule).setName("X");
		verify(rule).addCondition(home, "is empty");
		verify(rule).addAction(lamp, "1 off");
	}
}
