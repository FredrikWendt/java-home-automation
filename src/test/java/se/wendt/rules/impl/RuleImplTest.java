package se.wendt.rules.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import se.wendt.rules.ActionMachine;
import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;

public class RuleImplTest {

	@Mock
	private ActionMachine actionMachine;
	private String variableName = "something";
	private String conditionState = "is in effect";
	private String actionState = "to a new value";
	private String ruleDefinition = "rule test1 \n when \n " + variableName + " " + conditionState + " \n then \n set "
			+ variableName + " " + actionState;
	private RuleImpl testee;
	private Variable variable = new VariableImpl(variableName);

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new RuleImpl(actionMachine);
	}

	@Test
	public void actionsAreFired() throws Exception {
		testee.addAction(variable, actionState);

		testee.fireEvents();

		verifyActionInvocation();
	}

	@Test
	public void actionsAreFiredFromRuleDescription() throws Exception {
		createTesteeFromRuleDescription();

		testee.fireEvents();

		verifyActionInvocation();
	}

	@Test
	public void nothingHappensWithZeroActions() throws Exception {
		testee.fireEvents();

		verifyNoActionInvocation();
	}

	@Test
	public void processingStatesWhichAppliesFiresActions() throws Exception {
		createTesteeFromRuleDescription();
		StateMachine states = prepareStatesMatchingConditions();

		testee.process(states);

		verifyActionInvocation();
	}

	@Test
	public void rulesOnlyFiresActionsOnce() throws Exception {
		createTesteeFromRuleDescription();
		StateMachine states = prepareStatesMatchingConditions();

		testee.process(states);
		testee.process(states);
		testee.process(states);

		// only once
		verifyActionInvocation();
	}

	@Test
	public void processingStatesWhichDoesNotAppliesDoesntFireActions() throws Exception {
		createTesteeFromRuleDescription();
		StateMachine states = prepareStatesThatDoesntMatchConditions();

		testee.process(states);

		verifyNoActionInvocation();
	}

	@Test
	public void testname() throws Exception {
		createTesteeFromRuleDescription();
		StateMachine matchingStates = prepareStatesMatchingConditions();
		StateMachine nonMatchingStates = prepareStatesThatDoesntMatchConditions();

		testee.process(nonMatchingStates);
		testee.process(nonMatchingStates);
		testee.process(nonMatchingStates);
		verifyNoActionInvocation();

		testee.process(matchingStates);
		testee.process(matchingStates);
		testee.process(nonMatchingStates);
		verifyActionInvocation();
	}

	protected StateMachine prepareStatesThatDoesntMatchConditions() {
		StateMachine states = mock(StateMachine.class);
		given(states.getCurrentValue(variable)).willReturn("some other state");
		return states;
	}

	private void createTesteeFromRuleDescription() {
		testee = new RuleImpl(ruleDefinition, actionMachine);
	}

	private void verifyActionInvocation() {
		ArgumentCaptor<VariableChangeEvent> eventCaptor = ArgumentCaptor.forClass(VariableChangeEvent.class);

		verify(actionMachine).fire(eventCaptor.capture());
		assertEquals(variable, eventCaptor.getValue().getVariable());
		assertEquals(actionState, eventCaptor.getValue().getNewValue());
	}

	private void verifyNoActionInvocation() {
		verify(actionMachine, never()).fire(any(VariableChangeEvent.class));
	}

	private StateMachine prepareStatesMatchingConditions() {
		StateMachine states = mock(StateMachine.class);
		given(states.getCurrentValue(variable)).willReturn(conditionState);
		return states;
	}

}
