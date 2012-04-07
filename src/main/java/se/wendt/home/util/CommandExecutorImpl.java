package se.wendt.home.util;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;

public class CommandExecutorImpl extends LogEnabledBase implements CommandExecutor {

	private final Runtime runtime;

	public CommandExecutorImpl() {
		this(Runtime.getRuntime());
	}

	CommandExecutorImpl(Runtime runtime) {
		this.runtime = runtime;
	}

	@Override
	public void execute(String... commandArray) {
		log("executing low level command: %s", asList(commandArray));
		try {
			Process exec = doExecute(commandArray);
			exec.waitFor();
		} catch (InterruptedException e) {
			throw new HomeAutomationException("Command was interrupted", e);
		}
	}

	@Override
	public InputStream executeAndReturnInputStream(String... commandArray) {
		log("executing low level command: %s", asList(commandArray));
		try {
			Process exec = doExecute(commandArray);
			int exit = exec.waitFor();
			if (exit != 0) {
				throw new HomeAutomationException("Command '%s' ended with exit status %d\n", join(commandArray), exit);
			}
			return exec.getInputStream();
		} catch (InterruptedException e) {
			throw new HomeAutomationException("Command was interrupted", e);
		}
	}

	private String join(String[] commandArray) {
		StringBuilder sb = new StringBuilder();
		for (String bit : commandArray) {
			sb.append(" " + bit);
		}
		return sb.toString();
	}

	private Process doExecute(String... commandArray) {
		try {
			Process exec = runtime.exec(commandArray);
			return exec;
		} catch (IOException e) {
			throw new HomeAutomationException(e, "Command %s ended with IO exception", join(commandArray));
		}
	}

}
