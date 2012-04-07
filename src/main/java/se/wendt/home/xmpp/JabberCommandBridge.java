package se.wendt.home.xmpp;

import java.io.IOException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import se.wendt.home.config.ConfigurationManager;
import se.wendt.home.config.ConfigurationManagerImpl;
import se.wendt.home.util.HomeAutomationException;

public class JabberCommandBridge implements MessageListener {

	private XMPPConnection connection;
	private final CommandHandler commandHandler;

	public static void main(String... args) throws IOException {
		ConfigurationManager config = new ConfigurationManagerImpl("home.properties");
		JabberCommandBridge publisher = new JabberCommandBridge(null, 
				config.getValue(XmppLifeCycleManager.XMPP_USERNAME),
				config.getValue(XmppLifeCycleManager.XMPP_PASSWORD),
				config.getValue(XmppLifeCycleManager.XMPP_SERVER));
		publisher.distributeMessage("testing 123");
	}

	public JabberCommandBridge(CommandHandler handler, String username, String password, String server) {
		commandHandler = handler;
		setupConnection(username, password, server);
		connection.addConnectionListener(new ConnectionListener() {
			@Override
			public void reconnectionSuccessful() {
				System.out.println("reconnect success");
			}

			@Override
			public void reconnectionFailed(Exception arg0) {
				System.out.println("reconnect failed");
			}

			@Override
			public void reconnectingIn(int arg0) {
				System.out.println("reconnect in " + arg0);
			}

			@Override
			public void connectionClosedOnError(Exception arg0) {
			}

			@Override
			public void connectionClosed() {
				System.out.println("closed");
			}
		});
	}

	public void stop() {
		connection.disconnect();
	}

	@Override
	public void processMessage(Chat arg0, Message arg1) {
		commandHandler.handle(arg1.getBody());
	}

	// TODO: move to Notification or EventLog
	public void distributeMessage(String message) {
		try {
			Chat chat = getChatFor("fredrik@wendt.se");
			chat.sendMessage(message);
		} catch (XMPPException e) {
			System.out.println("Error Delivering block");
		}
	}

	protected Chat getChatFor(String receiver) {
		Chat chat = connection.getChatManager().createChat(receiver, this);
		return chat;
	}

	protected XMPPConnection setupConnection(String username, String password, String server) {
		XMPPConnection connection = new XMPPConnection(server);
		try {
			connection.connect();
			connection.login(username, password, "HomeAutomationBot");
			return connection;
		} catch (XMPPException e) {
			e.printStackTrace();
			throw new HomeAutomationException(e, "Failed to setup %s", this);
		}
	}

}
