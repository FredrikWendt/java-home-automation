package se.wendt.rules.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;
import se.wendt.rules.VariableChangeEventListener;

public class LightsOff {

	private Variable home = new VariableImpl("home");
	private Variable lamp = new VariableImpl("lamp");

	@Mock
	private VariableChangeEventListener listener;

	private Environment testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new Environment();
		testee.addVariableChangeEventListener(home, listener);
	}

	@Test
	public void listenersWork() throws Exception {
		VariableChangeEvent someoneComesHome = new VariableChangeEventImpl(home, "is empty");
		testee.change(home, "is empty");
		verify(listener).stateChanged(someoneComesHome);
	}

	@Test
	public void turnLightOn() throws Exception {
		VariableChangeEvent turnLampOneOn = new VariableChangeEventImpl(lamp, "1 on");

		// GIVEN
		testee.addVariableChangeEventListener(lamp, listener);
		String rule = "rule x\n" + //
				"when \n home is not empty\n" + //
				"then \n set lamp 1 on";
		testee.addRule(rule);

		// WHEN
		testee.change(home, "is not empty");

		// THEN
		verify(listener).stateChanged(turnLampOneOn);
	}

	@Test
	public void turnLightsOff() throws Exception {
		// GIVEN
		testee.addVariableChangeEventListener(lamp, listener);
		String rule = "rule x\n" + //
				"when \n home is not empty\n" + //
				"then \n " + //
				"turn lamp 1 off\n" + "turn lamp 2 off\n" + "turn lamp 3 off\n" + "turn lamp 4 off\n";
		testee.addRule(rule);

		// WHEN
		testee.change(home, "is not empty");

		// THEN
		verify(listener).stateChanged(new VariableChangeEventImpl(lamp, "1 off"));
		verify(listener).stateChanged(new VariableChangeEventImpl(lamp, "2 off"));
		verify(listener).stateChanged(new VariableChangeEventImpl(lamp, "3 off"));
		verify(listener).stateChanged(new VariableChangeEventImpl(lamp, "4 off"));
	}

}
