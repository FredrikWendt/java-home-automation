package se.wendt.home.tellstick;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.wendt.home.bus.Message;
import se.wendt.home.util.JmsUtilsTestBase;

@RunWith(MockitoJUnitRunner.class)
public class TellstickQueryRequesterTest extends JmsUtilsTestBase {

	private TellstickQueryRequester testee;

	@Mock
	private Message reply;

	@Before
	public void testname() throws Exception {
		prepareJmsUtilsMock();
		testee = new TellstickQueryRequester(jms) {
			@Override
			public Message send(Message message) {
				return reply;
			};
		};

	}

	@Test
	public void twoDevicesReturned() {
		given(reply.getStringProperty(TellstickQuery.JMS_PROPERTY_WITH_ALL_DEVICES)).willReturn("a,b");
		Collection<String> result = testee.listAllDevices();
		assertTrue(result.contains("a"));
		assertTrue(result.contains("b"));
		assertEquals(2, result.size());
	}

	@Test
	public void oneDeviceReturned() {
		given(reply.getStringProperty(TellstickQuery.JMS_PROPERTY_WITH_ALL_DEVICES)).willReturn("b");
		Collection<String> result = testee.listAllDevices();
		assertTrue(result.contains("b"));
		assertEquals(1, result.size());
	}

	@Test
	public void zeroDevicesReturned() {
		given(reply.getStringProperty(TellstickQuery.JMS_PROPERTY_WITH_ALL_DEVICES)).willReturn("");
		Collection<String> result = testee.listAllDevices();
		assertEquals("zero devices should've been returned", 0, result.size());
	}

}
