package se.wendt.home.sound;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageConsumerBase;

public class SoundConsumer extends MessageConsumerBase implements Sound {

	private final Sound player;

	public SoundConsumer(JmsUtils jms, Sound player) {
		super(jms, JMS_TOPIC);
		this.player = player;
		log("properly setup");
	}

	@Override
	public void handle(Message message) {
		String fileToPlay = message.getStringProperty(JMS_PROPERTY_WITH_FILENAME);
		log("Got message: %s", fileToPlay);
		playFile(fileToPlay);
	}

	@Override
	public void playFile(String fileToPlay) {
		player.playFile(fileToPlay);
	}
}
