package se.wendt.home.rules;

import java.util.Calendar;
import java.util.Locale;

import se.wendt.rules.StateMachine;

/**
 * This one's not thread safe! :)
 */
public class WeekdayVariable extends TimeVariable {

	public static final String MONDAY = "monday";
	public static final String TUESDAY = "tuesday";
	public static final String WEDNESDAY = "wednesday";
	public static final String THURSDAY = "thursday";
	public static final String FRIDAY = "friday";
	public static final String SATURDAY = "saturday";
	public static final String SUNDAY = "sunday";

	public static final long MILLISECONDS_IN_A_DAY = 60 * 60 * 24 * 1000;

	private Locale locale = Locale.US;

	public WeekdayVariable(long millisecondsInADay, StateMachine stateMachine) {
		this(stateMachine);
		setNow(millisecondsInADay);
	}

	public WeekdayVariable(StateMachine stateMachine) {
		super(stateMachine, MILLISECONDS_IN_A_DAY, "weekday");
	}

	@Override
	protected String getStringToAnnounce() {
		String day = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
		return day.toLowerCase();
	}

}
