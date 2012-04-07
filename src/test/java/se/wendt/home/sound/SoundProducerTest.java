package se.wendt.home.sound;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import se.wendt.home.util.JmsUtilsTestBase;

public class SoundProducerTest extends JmsUtilsTestBase {

	private String fileToPlay = "yellow_submarine.ogg";

	private SoundProducer testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new SoundProducer(jms);
	}

	@Test
	public void typicalFlow() throws Exception {
		testee.playFile(fileToPlay);
		verify(jmsMessage).setStringProperty(Sound.JMS_PROPERTY_WITH_FILENAME, fileToPlay);
	}

}
