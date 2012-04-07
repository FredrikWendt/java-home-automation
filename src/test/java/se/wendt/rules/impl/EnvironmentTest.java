package se.wendt.rules.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Test;
import org.mockito.Mock;

import se.wendt.rules.Variable;
import se.wendt.rules.VariableChangeEvent;
import se.wendt.rules.VariableChangeEventListener;

public class EnvironmentTest {

	@Mock
	VariableChangeEventListener listener;
	@Mock
	Variable variable;

	@Test
	public void fireReachesListenerOfSameVariable() throws Exception {
		initMocks(this);
		Environment testee = new Environment();
		testee.addVariableChangeEventListener(variable, listener);
		VariableChangeEvent event = new VariableChangeEventImpl(variable, "state");
		testee.fire(event);
		verify(listener).stateChanged(event);
	}

	@Test
	public void fireSkipsListenerOfOtherVariableType() throws Exception {
		initMocks(this);
		Environment testee = new Environment();
		Variable variable2 = mock(Variable.class);
		testee.addVariableChangeEventListener(variable2, listener);
		VariableChangeEvent event = new VariableChangeEventImpl(variable, "state");
		testee.fire(event);
		verify(listener, never()).stateChanged(event);
	}
}
