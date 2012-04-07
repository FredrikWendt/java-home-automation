package se.wendt.home.bluetooth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.bluetooth.link.BluetoothLinkQualityQueryEndpointImpl;
import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.HomeAutomationException;

public class BluetoothLinkQualityQueryEndpointImplTest {

	@Mock
	private CommandExecutor commandExecutor;
	private BluetoothLinkQualityQueryEndpointImpl testee;
	private String mac = "00:11:22:33:44:55";

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new BluetoothLinkQualityQueryEndpointImpl(commandExecutor);
	}

	@Test
	public void ifNoConnectionIsMadeAnExceptionIsThrown() throws Exception {
		HomeAutomationException exception = new HomeAutomationException();
		doThrow(exception).when(commandExecutor).execute((String[]) any());

		try {
			testee.getLinkQuality(mac);
		} catch (HomeAutomationException e) {
			assertNotSame(exception, e);
		}
	}

	@Test
	public void typicalFlow() throws Exception {
		final int linkQuality = 123;
		final String commandOutput = mac + ": " + linkQuality + "\nApa\nBepa";
		final String[] connectionCommand = new String[] { "hcitool", "cc", mac };
		final String[] linkQualityCommand = new String[] { "hcitool", "lq", mac };

		InputStream commandResult = new CommandResultInputStream(commandOutput);
		given(commandExecutor.executeAndReturnInputStream(linkQualityCommand)).willReturn(commandResult);

		int result = testee.getLinkQuality(mac);

		assertEquals(linkQuality, result);
		verify(commandExecutor).execute(connectionCommand);
		verify(commandExecutor).executeAndReturnInputStream(linkQualityCommand);
	}

	class CommandResultInputStream extends InputStream {
		int currentChar = 0;
		final byte[] bytes;

		public CommandResultInputStream(String output) {
			bytes = output.getBytes();
		}

		@Override
		public int read() throws IOException {
			if (currentChar == bytes.length) {
				return -1;
			}
			return bytes[currentChar++];
		}
	}

}
