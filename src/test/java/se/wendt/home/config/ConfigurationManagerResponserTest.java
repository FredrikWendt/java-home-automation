package se.wendt.home.config;

import se.wendt.home.bus.impl.JmsUtils;

public class ConfigurationManagerResponserTest {

	public static void main(String[] args) throws Exception {
		new ConfigurationManagerResponser(new JmsUtils(), new ConfigurationManagerImpl("dummy.properties"));
	}

}
