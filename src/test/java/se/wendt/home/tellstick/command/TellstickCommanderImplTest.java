package se.wendt.home.tellstick.command;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import se.wendt.home.tellstick.tdtool.TellstickCommander;
import se.wendt.home.tellstick.tdtool.TellstickCommanderImpl;
import se.wendt.home.util.CommandExecutor;

public class TellstickCommanderImplTest {

	@Test
	public void testeeIsATellstickImplementation() throws Exception {
		TellstickCommander testee = new TellstickCommanderImpl(mock(CommandExecutor.class));
		assertTrue(testee instanceof TellstickCommander);
	}

	@Test
	public void typicalFlow() throws Exception {
		CommandExecutor executor = mock(CommandExecutor.class);
		TellstickCommander testee = new TellstickCommanderImpl(executor);
		testee.turnDeviceOn("1");
		verify(executor).execute("tdtool", "-n", "1");

		testee.turnDeviceOff("1");
		verify(executor).execute("tdtool", "-f", "1");
	}
}
