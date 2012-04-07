package se.wendt.home.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import se.wendt.home.people.PeoplePresenceCommunication;
import se.wendt.home.tellstick.tdtool.TellstickCommander;

public class XmppBot implements MessageListener, XmppConnectionConsumer {

	XMPPConnection connection;
	Collection<Chat> chats = new ArrayList<Chat>();
	private final PeoplePresenceCommunication precense;
	private final TellstickCommander tellstick;

	public XmppBot(PeoplePresenceCommunication precense, TellstickCommander tellstick) {
		this.precense = precense;
		this.tellstick = tellstick;
	}

	// ---- MessageListener ____________________________________________________

	@Override
	public void processMessage(Chat chat, Message message) {
		try {
			if (message.getType() == Message.Type.chat) {
				System.out.println(chat.getParticipant() + " says: " + message.getBody());
				String reply = handleMessage(message.getBody());
				if (reply != null) {
					chat.sendMessage(reply);
				} else {
					chat.sendMessage("Jag heter Anna, och du sa till mig: " + message.getBody());
				}
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	private String handleMessage(String body) {
		// TODO Auto-generated method stub
		return null;
	}

	// ---- helpers ____________________________________________________________

	private void sendMessage(String to, String message, Object... args) throws XMPPException {
		Chat chat = connection.getChatManager().createChat(to, this);
		String formattedMessage = message;
		try {
			formattedMessage = String.format(message, args);
		} catch (RuntimeException e) {
			// bugger
		}
		chat.sendMessage(formattedMessage);
	}

	private void startConversations() throws XMPPException {
		Roster roster = connection.getRoster();
		roster.setSubscriptionMode(SubscriptionMode.accept_all);

		Collection<RosterEntry> entries = roster.getEntries();
		Iterator<RosterEntry> i = entries.iterator();

		while (i.hasNext()) {
			RosterEntry r = i.next();
			sendMessage(r.getUser(), "Hej %s, jag heter Anna och jag är en bot. Någon sparkade igång mig.", r.getName());
		}
	}

	@Override
	public void consume(XMPPConnection connection) {
		this.connection = connection;
		try {
			startConversations();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void release() {
		for (Chat chat : chats) {
			chat.removeMessageListener(this);
		}
		this.connection = null;
	}

}
