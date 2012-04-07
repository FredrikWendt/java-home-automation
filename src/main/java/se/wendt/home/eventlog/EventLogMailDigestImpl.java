package se.wendt.home.eventlog;

import java.util.Date;

import se.wendt.home.util.PlainTextSender;
import se.wendt.home.util.RunnableService;
import se.wendt.home.util.Thread;

public class EventLogMailDigestImpl extends RunnableService implements EventLog {

	public static final String SUBJECT = "e-wendt log";

	private static final long MILLISECONDS_PER_MINUTE = 1000 * 60;
	private static final long MILLISECONDS_PER_HOUR = MILLISECONDS_PER_MINUTE * 60;
	private static final long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * 24;

	public static final long MILLISECONDS_PER_INTERVALL = MILLISECONDS_PER_HOUR;

	private StringBuilder eventlog = new StringBuilder();
	private long nextRun = 0;
	private final PlainTextSender plainTextSender;

	public EventLogMailDigestImpl(PlainTextSender mailBackend) {
		this.plainTextSender = mailBackend;
	}

	@Override
	public void run() {
		log("mail digest thread started");
		while (true) {
			nextRun = getMilliSecondsToNextDigest();
			log("next digest is sent at %s (now is %s)", new Date(nextRun), new Date(now()));
			try {
				long millisecondsToSleep = Math.max(10000, nextRun - now());
				Thread.sleep(millisecondsToSleep);
			} catch (InterruptedException e) {
			}

			if (now() >= nextRun || thread == null) {
				sendLogDigest();
			} else {
				log("need to wait longer: %d >= %d", now(), nextRun);
			}

			if (thread == null) {
				break;
			}
		}
		log("mail digest thread ended");
	}

	@Override
	public void recordEvent(String eventSource, String eventDescription) {
		eventlog.append(String.format("\nLOG: %s %s", eventSource, eventDescription));
	}

	private void sendLogDigest() {
		log("Sending digest");
		plainTextSender.send("e-wendt log", eventlog.toString());
		eventlog = new StringBuilder();
	}

	protected long getMilliSecondsToNextDigest() {
		long now = now();
		long offset = now % MILLISECONDS_PER_INTERVALL;
		return now - offset + MILLISECONDS_PER_INTERVALL;
	}

	protected long now() {
		return System.currentTimeMillis();
	}
}
