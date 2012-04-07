package se.wendt.home.app;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.sound.SoundConsumer;
import se.wendt.home.sound.SoundImpl;
import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.CommandExecutorImpl;

/**
 * Run on a computer that should produce sound(s) in the home.
 */
public class SoundStation {

	public SoundStation(JmsUtils jms) {
		CommandExecutor executor = new CommandExecutorImpl();
		SoundImpl player = new SoundImpl(executor);
		new SoundConsumer(jms, player);
		player.start();
	}
}
