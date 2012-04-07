package se.wendt.home.xmpp;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import se.wendt.home.config.ConfigurationManager;


public class XmppLifeCycleManager implements ConnectionListener {

	/** Configuration setting for XMPP username. */
	public static final String XMPP_USERNAME = "xmpp.username";
	/** Configuration setting for XMPP password. */
	public static final String XMPP_PASSWORD = "xmpp.password";
	/** Configuration setting for XMPP server (hostname). */
	public static final String XMPP_SERVER = "xmpp.server";
	
	private XMPPConnection connection;
	private Collection<XmppConnectionConsumer> consumers = new CopyOnWriteArrayList<XmppConnectionConsumer>();
	private final String username;
	private final String password;
	private final String server;

	public XmppLifeCycleManager(ConfigurationManager config) {
		username = config.getValue(XMPP_USERNAME);
		password = config.getValue(XMPP_PASSWORD);
		server = config.getValue(XMPP_SERVER);
	}

	// ---- public _____________________________________________________________

	public void start() {
		log("Starting");
		XMPPConnection.DEBUG_ENABLED = true;

		try {
			login(username, password);
			tellConsumersAboutConnection();
		} catch (XMPPException e) {
			throw new RuntimeException("Failed to start XMPP bot", e);
		}
		log("Started");
	}

	public void stop() {
		log("Stopping");
		disconnect();
		connectionClosed();
		log("Stopped");
	}

	public void addXmppConnectionConsumer(XmppConnectionConsumer xmppBot) {
		consumers.add(xmppBot);
	}

	// ---- ConnectionListener _________________________________________________

	@Override
	public void connectionClosed() {
		log("Connection closed");
		tellConsumersToAbbandonConnection();
	}

	@Override
	public void connectionClosedOnError(Exception arg0) {
		log("Connection closed with error");
		for (XmppConnectionConsumer consumer : consumers) {
			consumer.release();
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
		log("Reconnecting in " + arg0);
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		log("Reconection failed");
	}

	@Override
	public void reconnectionSuccessful() {
		log("Reconnected");
		tellConsumersAboutConnection();
	}

	// ---- helpers ____________________________________________________________

	private void disconnect() {
		if (connection != null) {
			log("Disconnecting");
			connection.disconnect();
		}
		log("Disconnected");
	}

	private void login(String userName, String password) throws XMPPException {
		log("Login complete");
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		ConnectionConfiguration config = new ConnectionConfiguration(server, 5222, server);
		connection = new XMPPConnection(config);
		connection.connect();
		connection.login(userName, password, Long.toHexString(System.currentTimeMillis()));
		connection.addConnectionListener(this);
		log("Login complete");
	}

	private void tellConsumersAboutConnection() {
		for (XmppConnectionConsumer consumer : consumers) {
			consumer.consume(connection);
		}
	}

	private void tellConsumersToAbbandonConnection() {
		for (XmppConnectionConsumer consumer : consumers) {
			consumer.release();
		}
	}

	private void log(String string) {
		System.out.println("XmppLifeCycleManager: " + string);
	}

}
