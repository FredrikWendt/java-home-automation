package se.wendt.home.util;

import java.util.Date;

public abstract class LogEnabledBase {

	private String logName;

	public LogEnabledBase() {
		setupLogName();
	}

	private void setupLogName() {
		logName = this.getClass().getName();
		while (logName.contains(".")) {
			logName = logName.substring(logName.lastIndexOf('.') + 1);
		}
	}

	protected void log(String format, Object... args) {
		String time = String.format("%d-%d ", System.currentTimeMillis(), System.nanoTime());
		time = new Date().toString() + " ";
		try {
			System.out.println(time + getLogName() + " " + String.format(format, args));
		} catch (Throwable t) {
			System.out.println(time + getLogName() + " " + format + args);
			t.printStackTrace();
		}
	}

	protected String getLogName() {
		return logName;
	}

}
