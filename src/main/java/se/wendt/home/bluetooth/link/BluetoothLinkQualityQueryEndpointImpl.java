package se.wendt.home.bluetooth.link;

import java.io.InputStream;

import se.wendt.home.util.BufferedLineReader;
import se.wendt.home.util.CommandExecutor;
import se.wendt.home.util.HomeAutomationException;

/**
 * Uses the bluez command line tool "hcitool" to establish a connection and then query the link for
 * its quality.
 */
public class BluetoothLinkQualityQueryEndpointImpl {

	private final CommandExecutor commandExecutor;

	public BluetoothLinkQualityQueryEndpointImpl(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	public int getLinkQuality(String mac) {
		tryToEstablishConnection(mac);
		int linkQuality = tryToGetLinkQuality(mac);
		return linkQuality;
	}

	private void tryToEstablishConnection(String mac) {
		String[] connectCommand = { "hcitool", "cc", mac };
		commandExecutor.execute(connectCommand);
	}

	private int tryToGetLinkQuality(String mac) {
		String[] linkQuailityCommand = { "hcitool", "lq", mac };
		InputStream inputStream = commandExecutor.executeAndReturnInputStream(linkQuailityCommand);
		return parseIntegerFromInputStream(inputStream);
	}

	private int parseIntegerFromInputStream(InputStream inputStream) {
		try {
			for (String line : new BufferedLineReader(inputStream)) {
				String integerPart = line.substring(line.lastIndexOf(':') + 2);
				return Integer.parseInt(integerPart);
			}
		} catch (Throwable t) {
			throw new HomeAutomationException("Failed to parse Blueooth link quality command output", t);
		}
		throw new HomeAutomationException("Blueooth link quality command didn't return any output to parse");
	}

}
