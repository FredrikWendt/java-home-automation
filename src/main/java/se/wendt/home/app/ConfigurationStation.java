package se.wendt.home.app;

import java.io.IOException;

import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.config.ConfigurationManagerImpl;
import se.wendt.home.config.ConfigurationManagerResponser;
import se.wendt.home.util.LogEnabledBase;

public class ConfigurationStation extends LogEnabledBase {

	public ConfigurationStation(JmsUtils jms) throws IOException {
		ConfigurationManagerImpl manager = new ConfigurationManagerImpl("home.properties");
		new ConfigurationManagerResponser(jms, manager);
		log("properly setup");
	}
	
	public static void main(String[] args) throws IOException {
		new ConfigurationStation(new JmsUtils());
	}

}
