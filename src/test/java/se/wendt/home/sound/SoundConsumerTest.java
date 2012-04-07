package se.wendt.home.sound;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageReceiver;
import se.wendt.home.util.JmsUtilsTestBase;

public class SoundConsumerTest extends JmsUtilsTestBase {

	@Mock
	private Sound player;

	private MessageReceiver testee;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new SoundConsumer(jms, player);
	}

	@Test
	public void soundCommandConsumerForwardsIncomingRequestsToBackendPlayer() throws Exception {
		final String expected = "hey jude.mp3";

		Message incomingMessage = mock(Message.class);
		given(incomingMessage.getStringProperty(SoundConsumer.JMS_PROPERTY_WITH_FILENAME)).willReturn(expected);

		testee.handle(incomingMessage);
		verify(player).playFile(expected);
	}

}
