package se.wendt.home.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static se.wendt.home.rules.WeekdayVariable.FRIDAY;
import static se.wendt.home.rules.WeekdayVariable.MILLISECONDS_IN_A_DAY;
import static se.wendt.home.rules.WeekdayVariable.MONDAY;
import static se.wendt.home.rules.WeekdayVariable.SATURDAY;
import static se.wendt.home.rules.WeekdayVariable.SUNDAY;
import static se.wendt.home.rules.WeekdayVariable.THURSDAY;
import static se.wendt.home.rules.WeekdayVariable.TUESDAY;
import static se.wendt.home.rules.WeekdayVariable.WEDNESDAY;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import se.wendt.rules.StateMachine;
import se.wendt.rules.Variable;
import se.wendt.rules.impl.VariableImpl;

public class WeekdayVariableTest {

	@Mock
	StateMachine eventHub;

	private Variable variable = new VariableImpl("weekday");

	private WeekdayVariable testee;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new WeekdayVariable(eventHub);
	}

	@Test
	public void testDays() throws Exception {
		verifyDay(0 * MILLISECONDS_IN_A_DAY, THURSDAY);
		verifyDay(1 * MILLISECONDS_IN_A_DAY, FRIDAY);
		verifyDay(2 * MILLISECONDS_IN_A_DAY, SATURDAY);
		verifyDay(3 * MILLISECONDS_IN_A_DAY, SUNDAY);
		verifyDay(4 * MILLISECONDS_IN_A_DAY, MONDAY);
		verifyDay(5 * MILLISECONDS_IN_A_DAY, TUESDAY);
		verifyDay(6 * MILLISECONDS_IN_A_DAY, WEDNESDAY);
	}

	@Test
	public void getMillisToNextMidnight() throws Exception {
		verifySleepDelay(0L, MILLISECONDS_IN_A_DAY);
		verifySleepDelay(MILLISECONDS_IN_A_DAY, MILLISECONDS_IN_A_DAY);
		verifySleepDelay(1L, MILLISECONDS_IN_A_DAY - 1L);
		verifySleepDelay(10000, MILLISECONDS_IN_A_DAY - 10000);
	}

	private void verifySleepDelay(final long fakeNow, long expectedDelay) {
		testee = new WeekdayVariable(eventHub) {
			@Override
			protected long getNow() {
				return fakeNow;
			}
		};
		long tillNextMidnight = testee.getMillisecondsTillNextAnnouncement();
		assertEquals(expectedDelay, tillNextMidnight);
	}

	private void verifyDay(long epochOffset, String day) {
		eventHub = mock(StateMachine.class);
		new WeekdayVariable(epochOffset, eventHub);
		verify(eventHub).change(variable, day);
	}

}
