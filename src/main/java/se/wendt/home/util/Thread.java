package se.wendt.home.util;

public class Thread {

	private final java.lang.Thread thread;

	public Thread(Runnable runnable) {
		thread = new java.lang.Thread(runnable);
	}

	public void start() {
		thread.start();
	}
	
	public void run() {
		thread.run();
	}
	
	public void interrupt() {
		thread.interrupt();
	}
	
	public boolean isAlive() {
		return thread.isAlive();
	}

	public static void sleep(long i) throws InterruptedException {
		java.lang.Thread.sleep(i);
	}

}
