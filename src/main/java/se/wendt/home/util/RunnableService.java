package se.wendt.home.util;

public abstract class RunnableService extends LogEnabledBase implements Runnable {

	protected java.lang.Thread thread;

	public void start() {
		thread = createThread(this);
		thread.start();
		log("thread started");
	}

	public boolean isRunning() {
		return thread != null && thread.isAlive();
	}

	public void stop() {
		if (thread != null) {
			java.lang.Thread t = thread;
			thread = null;
			t.interrupt();
		}
	}

	protected java.lang.Thread createThread(Runnable runnable) {
		return new java.lang.Thread(runnable);
	}
	
	protected boolean continueToRun() {
		return thread != null;
	}

}
