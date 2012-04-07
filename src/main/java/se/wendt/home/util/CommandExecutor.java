package se.wendt.home.util;

import java.io.InputStream;

public interface CommandExecutor {

	/**
	 * Executes the command.
	 * 
	 * @param commandArray array of command and arguments
	 */
	void execute(String... commandArray);

	InputStream executeAndReturnInputStream(String... commandArray);

}
