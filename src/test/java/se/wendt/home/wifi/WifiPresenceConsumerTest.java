package se.wendt.home.wifi;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.jms.JMSException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.bus.Message;
import se.wendt.home.bus.MessageReceiver;
import se.wendt.home.util.JmsUtilsTestBase;
import se.wendt.home.wifi.leases.DhcpLease;
import se.wendt.home.wifi.leases.DhcpLeaseImpl;

public class WifiPresenceConsumerTest extends JmsUtilsTestBase {

	@Mock
	private WifiPresenceListener backend;

	private final DhcpLease lease = new DhcpLeaseImpl("h", "i", "m", "e");

	private WifiPresenceConsumer testee;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
		prepareJmsUtilsMock();
		testee = new WifiPresenceConsumer(jms, backend);
	}

	@Test
	public void consumerIsAWifiPresenceListener() throws Exception {
		assertTrue(testee instanceof WifiPresenceListener);
	}

	@Test
	public void consumerIsAMessageListener() throws Exception {
		assertTrue(testee instanceof MessageReceiver);
	}

	@Test
	public void connectMsgCallsDeviceConnectedOnBackend() throws Exception {
		sendWifiPresenceEvent(true);
		verify(backend).deviceConnected(any(DhcpLease.class));
	}

	@Test
	public void disconnectMsgCallsDeviceDisconnectedOnBackend() throws Exception {
		sendWifiPresenceEvent(false);
		verify(backend).deviceDisconnected(any(DhcpLease.class));
	}

	private void sendWifiPresenceEvent(boolean sourceIsConnectEvent) throws JMSException {
		Message msg = mock(Message.class);
		given(msg.getBooleanProperty(WifiPresenceListener.JMS_PROPERTY_CONNECT_EVENT)).willReturn(sourceIsConnectEvent);
		testee.handle(msg);
	}

}
