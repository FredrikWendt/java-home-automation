package se.wendt.home.app;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.tellstick.TellstickQueryResponder;
import se.wendt.home.tellstick.tdtool.TellstickCommandConsumer;
import se.wendt.home.tellstick.tdtool.TellstickCommanderImpl;
import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.CommandExecutorImpl;

/**
 * Run on a computer that has a Tellstick.
 */
public class TellstickStation {

	public TellstickStation(JmsUtils jms) {
		CommandExecutor executor = new CommandExecutorImpl();
		TellstickCommanderImpl realController = new TellstickCommanderImpl(executor);
		TellstickCommandConsumer commandConsumer = new TellstickCommandConsumer(jms);
		TellstickQueryResponder responder = new TellstickQueryResponder(jms, realController);
		commandConsumer.addListener(realController);
	}
	
	public static void main(String[] args) {
		new TellstickStation(new JmsUtils());
	}
}
