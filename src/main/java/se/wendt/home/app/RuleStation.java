package se.wendt.home.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.rules.HomeVariable;
import se.wendt.home.rules.LampAction;
import se.wendt.home.rules.TimeVariable;
import se.wendt.home.rules.WeekdayVariable;
import se.wendt.home.tellstick.tdtool.TellstickCommandPublisher;
import se.wendt.home.util.BufferedLineReader;
import se.wendt.rules.impl.Environment;
import se.wendt.rules.impl.RulesParser;
import se.wendt.rules.impl.VariableImpl;
import se.wendt.util.ConfigurationUtils;

public class RuleStation {

	public RuleStation(JmsUtils jms) throws IOException {
		Environment environment = new Environment();
		LampAction lampAction = new LampAction(new TellstickCommandPublisher(jms));
		
		new HomeVariable(environment);
		new TimeVariable(environment).start();
		new WeekdayVariable(environment).start();
		environment.addVariableChangeEventListener(new VariableImpl("lamp"), lampAction);
		
		URL rules = ConfigurationUtils.locateConfiguration("home.rules");
		new RulesParser(environment, parseRulesFile(rules));
	}

	private Iterable<String> parseRulesFile(URL locateConfiguration) throws IOException {
		InputStream inputStream = locateConfiguration.openStream();
		BufferedLineReader bufferedLineReader = new BufferedLineReader(inputStream );
		return bufferedLineReader;
	}

}
