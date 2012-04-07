package se.wendt.home.sound;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.RunnableService;

public class SoundImpl extends RunnableService implements Sound {

	private final CommandExecutor executor;

	private BlockingQueue<String> soundsToPlay = new LinkedBlockingDeque<String>();

	public SoundImpl(CommandExecutor executor) {
		this.executor = executor;
		start();
		log("properly setup");
	}

	@Override
	public void playFile(String string) {
		soundsToPlay.add(string);
	}

	@Override
	public void run() {
		log("started playback thread");
		while (thread != null) {
			String fileToPlay = null;
			try {
				fileToPlay = soundsToPlay.take();
				doPlayFile(fileToPlay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Throwable t) {
				log("Failed to play %s", fileToPlay);
				t.printStackTrace();
			}
		}
	}

	private void doPlayFile(final String fileToPlay) {
		log("Playing sound %s", fileToPlay);
		String[] aplayCommand = { "aplay", fileToPlay };
		executor.execute(aplayCommand);
	}

}
