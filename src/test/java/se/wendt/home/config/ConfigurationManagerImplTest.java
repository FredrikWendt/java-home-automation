package se.wendt.home.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import se.wendt.test.StringInputStream;

public class ConfigurationManagerImplTest {

	private ConfigurationManager testee;
	private final String source = "configuration.properties";
	private final String key = "people";
	private final String value = "fredrik,lisa,anton";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		createTestee(key + "=" + value);
	}

	@Test
	public void typicalFlow() throws Exception {
		String result = testee.getValue(key);
		assertEquals(value, result);
	}

	protected void createTestee(final String properties) throws IOException {
		testee = new ConfigurationManagerImpl(source) {
			@Override
			protected InputStream locateResource(String source) {
				return new StringInputStream(properties);
			}
		};
	}

	@Test
	public void unknownSettingsReturnsNull() throws Exception {
		String value = testee.getValue("xyz");
		assertNull("unknown keys should return value null", value);
	}
}
