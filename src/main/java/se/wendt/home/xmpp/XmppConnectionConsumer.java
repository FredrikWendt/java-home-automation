package se.wendt.home.xmpp;

import org.jivesoftware.smack.XMPPConnection;

public interface XmppConnectionConsumer {

	void consume(XMPPConnection connection);
	
	void release();
	
}
