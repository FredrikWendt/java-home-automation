package se.wendt.home.rules;

import java.util.Calendar;

import se.wendt.home.util.RunnableService;
import se.wendt.home.util.Thread;
import se.wendt.rules.StateMachine;
import se.wendt.rules.impl.VariableImpl;

public class TimeVariable extends RunnableService {

	private final long defaultDelay;
	private final StateMachine stateMachine;
	private final VariableImpl variable;

	private String lastTimeAnnounced;
	protected Calendar c = Calendar.getInstance();

	public TimeVariable(long now, StateMachine eventHub) {
		this(eventHub);
		setNow(now);
	}

	public TimeVariable(StateMachine eventHub) {
		this(eventHub, 60 * 1000, "time");
	}

	protected TimeVariable(StateMachine eventHub, long defaultDelay, String variableName) {
		this.stateMachine = eventHub;
		this.defaultDelay = defaultDelay;
		variable = new VariableImpl(variableName);
	}

	protected void setNow(long millisecondsInADay) {
		c.setTimeInMillis(millisecondsInADay);
		String stringToAnnounce = getStringToAnnounce();
		if (lastTimeAnnounced == null || !lastTimeAnnounced.equals(stringToAnnounce)) {
			lastTimeAnnounced = stringToAnnounce;
			stateMachine.change(variable, stringToAnnounce);
		}
	}

	protected String getStringToAnnounce() {
		String result = "" + c.get(Calendar.HOUR_OF_DAY) + zeroPad(c.get(Calendar.MINUTE));
		return result;
	}

	private String zeroPad(int i) {
		return i < 10 ? "0" + i : "" + i;
	}

	protected long getNow() {
		return System.currentTimeMillis();
	}

	protected long getMillisecondsTillNextAnnouncement() {
		long now = getNow();
		long millisecondsSincePastAnnouncement = now % defaultDelay;
		return defaultDelay - millisecondsSincePastAnnouncement;
	}

	@Override
	public void run() {
		while (continueToRun()) {
			setNow(getNow());
			try {
				Thread.sleep(getMillisecondsTillNextAnnouncement());
			} catch (InterruptedException e) {
			}
		}
	}

}
