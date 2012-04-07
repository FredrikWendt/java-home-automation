package se.wendt.home.app;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.config.ConfigurationManagerRequester;
import se.wendt.home.eventlog.EventLogStdoutImpl;
import se.wendt.home.eventlog.EventLogTopicConsumer;

/**
 * Run from command line.
 */
public class MainApplication {

	public static void main(String... args) throws Exception {
		new MainApplication(args);
	}

	public MainApplication(String... args) throws Exception {
		if (args == null || args.length == 0) {
			System.out.println("-b bluetooth monitoring station");
			System.out.println("-c configuration station");
			System.out.println("-d stdout debug event log");
			System.out.println("-l logging station");
			System.out.println("-r rule station");
			System.out.println("-s sound station");
			System.out.println("-t Tellstick station");
			System.out.println("-w wifi monitoring station");
			System.out.println("-x wiring station");
			System.exit(1);
		}
		JmsUtils jms = new JmsUtils();
		ConfigurationManager configurationManager = new ConfigurationManagerRequester(jms);
		for (String arg : args) {
			char character = arg.charAt(1);
			switch (character) {
				case 'b':
					new BluetoothDevicePresenceStation(jms, configurationManager);
					break;
				case 'c':
					new ConfigurationStation(jms);
					break;
				case 'd':
					new EventLogTopicConsumer(jms).addListener(new EventLogStdoutImpl());
					break;
				case 'l':
					new LoggingStation(jms);
					break;
				case 'r':
					new RuleStation(jms);
					break;
				case 's':
					new SoundStation(jms);
					break;
				case 'w':
					new WifiDeviceMonitoringStation(jms, configurationManager);
					break;
				case 'x':
					new WiringStation(jms, configurationManager);
					break;
				case 't':
					new TellstickStation(jms);
					break;
				default:
					break;
			}
		}
	}
}
