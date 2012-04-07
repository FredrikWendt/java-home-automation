package se.wendt.home.rules;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;
import se.wendt.rules.impl.VariableChangeEventImpl;
import se.wendt.rules.impl.VariableImpl;

public class LampActionTest {

	@Mock
	private TellstickCommander backend;
	private LampAction testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new LampAction(backend);
	}

	@Test
	public void on() throws Exception {
		String newState = "2 on";
		Variable variable = new VariableImpl("lamp");
		VariableChangeEvent event = new VariableChangeEventImpl(variable, newState);
		testee.stateChanged(event);
		verify(backend).turnDeviceOn("2");
	}

	@Test
	public void off() throws Exception {
		String newState = "2 off";
		Variable variable = new VariableImpl("lamp");
		VariableChangeEvent event = new VariableChangeEventImpl(variable, newState);
		testee.stateChanged(event);
		verify(backend).turnDeviceOff("2");
	}
}
