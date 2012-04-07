package se.wendt.home.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CommandExecutorImplTest {

	@Mock
	private Runtime runtime;
	@Mock
	private Process process;

	private CommandExecutorImpl testee;
	private String[] commandArray = { "a", "-command" };

	@Before
	public void setup() throws Exception {
		initMocks(this);
		testee = new CommandExecutorImpl(runtime);
		when(runtime.exec(any(commandArray.getClass()))).thenReturn(process);
	}

	@Test
	public void interruptsAreRethrown() throws Exception {
		InterruptedException interruptedException = new InterruptedException();
		given(process.waitFor()).willThrow(interruptedException);

		verifyExecuteRethrowsException(interruptedException);
		verifyExecuteWithStreamRethrowsException(interruptedException);
	}

	@Test
	public void ioExceptionsAreRethrown() throws Exception {
		IOException ioException = new IOException();
		given(runtime.exec(any(commandArray.getClass()))).willThrow(ioException);
		verifyExecuteRethrowsException(ioException);
		verifyExecuteWithStreamRethrowsException(ioException);
	}

	@Test
	public void inputStreamIsAvailable() throws Exception {
		InputStream inputStream = mock(InputStream.class);
		given(process.getInputStream()).willReturn(inputStream);
		InputStream result = testee.executeAndReturnInputStream(commandArray);
		assertSame(inputStream, result);
	}

	@Test
	public void nonZeroExitCommandsCauseException() throws Exception {
		given(process.waitFor()).willReturn(Integer.MAX_VALUE);
		try {
			testee.executeAndReturnInputStream(commandArray);
			fail("If command exists with non zero value, an exception should be raised");
		} catch (HomeAutomationException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void typicalFlows() throws Exception {
		testee.execute(commandArray);
		testee.executeAndReturnInputStream(commandArray);
	}

	protected void verifyExecuteWithStreamRethrowsException(Exception exception) {
		try {
			testee.executeAndReturnInputStream(commandArray);
			fail("exec throwing " + exception.getClass() + " should throw " + HomeAutomationException.class);
		} catch (HomeAutomationException hae) {
			assertSame(exception, hae.getCause());
		}
	}

	protected void verifyExecuteRethrowsException(Exception exception) {
		try {
			testee.execute(commandArray);
		} catch (HomeAutomationException e) {
			assertSame(exception, e.getCause());
		}
	}
}
