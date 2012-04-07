package se.wendt.home.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import se.wendt.util.ConfigurationUtils;

public class ConfigurationManagerImpl implements ConfigurationManager {

	private final Properties props;

	public ConfigurationManagerImpl(String source) throws IOException {
		props = new Properties();
		InputStream resource = locateResource(source);
		props.load(resource);
	}

	protected InputStream locateResource(String source) throws IOException {
		URL url = ConfigurationUtils.locateConfiguration(source);
		return url.openStream();
	}

	@Override
	public String getValue(String key) {
		return (String) props.getProperty(key);
	}

}
