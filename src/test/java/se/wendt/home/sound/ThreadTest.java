package se.wendt.home.sound;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Test;
import org.mockito.Mock;

import se.wendt.home.util.Thread;

/**
 * java.lang.Thread.isAlive is a final method, hence Mockito can't mock it.
 */
public class ThreadTest {

	@Mock
	Thread mockThread;

	@Test
	public void testIsAlive() throws Exception {
		initMocks(this);
		when(mockThread.isAlive()).thenReturn(true);
		boolean alive = mockThread.isAlive();
		assertTrue(alive);
		verify(mockThread).isAlive();
	}

}
