package se.wendt.home.rules;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.impl.VariableImpl;

public class HomeVariableTest {

	@Mock
	private StateMachine stateMachine;
	private HomeVariable testee;
	private String personOne = "some_one";
	private String personTwo = "a_person";
	private Variable variable = new VariableImpl("home");

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new HomeVariable(stateMachine);
	}

	@Test
	public void personLeft() throws Exception {
		testee.personLeft(personOne);

		verifyHomeStateChangedToEmpty();
	}

	@Test
	public void personCame() throws Exception {
		testee.personCame(personOne);
		verifyHomeStateChangedToNotEmpty();
	}

	@Test
	public void twoPersonsAtHomeOneLeaves() throws Exception {
		testee.personCame(personOne);
		testee.personCame(personTwo);
		verifyHomeStateChangedToNotEmpty();

		testee.personLeft(personTwo);
		verifyNoMoreInteractions(stateMachine);
	}

	@Test
	public void twoPersonsAtHomeBothLeaves() throws Exception {
		testee.personCame(personOne);
		testee.personCame(personTwo);
		verifyHomeStateChangedToNotEmpty();

		testee.personLeft(personTwo);
		testee.personLeft(personOne);
		verifyHomeStateChangedToEmpty();
	}

	@Test
	public void typicalFlow() throws Exception {
		testee.personCame(personOne);
		testee.personLeft(personOne);
		testee.personCame(personOne);
		testee.personLeft(personOne);
		verifyHomeStateChangedToNotEmpty(2);
		verifyHomeStateChangedToEmpty(2);
	}

	private void verifyHomeStateChangedToEmpty() {
		verifyHomeStateChangedToEmpty(1);
	}

	private void verifyHomeStateChangedToEmpty(int wantedNumberOfInvocations) {
		verify(stateMachine, times(wantedNumberOfInvocations)).change(variable, HomeVariable.IS_EMPTY);
	}

	private void verifyHomeStateChangedToNotEmpty() {
		verifyHomeStateChangedToNotEmpty(1);
	}

	private void verifyHomeStateChangedToNotEmpty(int wantedNumberOfInvocations) {
		verify(stateMachine, times(wantedNumberOfInvocations)).change(variable, HomeVariable.IS_NOT_EMPTY);
	}
}
