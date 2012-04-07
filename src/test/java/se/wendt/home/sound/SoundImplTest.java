package se.wendt.home.sound;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.CommandExecutorImpl;
import se.wendt.home.util.Thread;

public class SoundImplTest {

	public static void main(String[] args) {
		SoundImpl soundPlayer = new SoundImpl(new CommandExecutorImpl());
		soundPlayer.playFile("/srv/wendt/sound/bt-connect.wav");
		soundPlayer.playFile("/srv/wendt/sound/bt-disconnect.wav");
		soundPlayer.playFile("/srv/wendt/sound/bt-all-gone.wav");
		soundPlayer.start();
	}

	@Mock
	private CommandExecutor executor;
	final String heyJude = "hey jude.mp3";
	private SoundImpl testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new SoundImpl(executor);
	}

	@After
	public void killThreadIfAny() {
		testee.stop();
	}

	@Test
	public void playFileUsesAplayToPlaySounds() throws Exception {
		testee.playFile(heyJude);
		Thread.sleep(100);

		verify(executor).execute("aplay", heyJude);
	}

	@Test
	public void commandExecutionProblemsDoesnKillTheThread() throws Exception {
		// not very strong test :]

		doThrow(new RuntimeException("bleh")).when(executor).execute("aplay", heyJude);

		testee.playFile(heyJude);
		Thread.sleep(100);

		assertTrue(testee.isRunning());
	}

}
