package se.wendt.home.sound;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.impl.JmsUtils;
import se.wendt.home.bus.impl.MessageProducerBase;

public class SoundProducer extends MessageProducerBase implements Sound {

	public SoundProducer(JmsUtils jms) {
		super(jms, JMS_TOPIC);
		log("properly setup");
	}

	@Override
	public void playFile(String fileToPlay) {
		sendMessage(fileToPlay);
	}

	private void sendMessage(String fileToPlay) {
		Message msg = createMessage();
		msg.setStringProperty(JMS_PROPERTY_WITH_FILENAME, fileToPlay);
		log("Sending sound play command %s", fileToPlay);
		send(msg);
	}
}
