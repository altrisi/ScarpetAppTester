package altrisi.scarpetapptester.tests;

import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.fakes.Lock;

public class TestUtils {
	public static Lock scheduleLock = new Lock();
	public static Lock stepLock = new Lock();
	public static boolean waitingForSchedules = false;
	public static boolean waitingForRunnable = false; 
	
	public static void waitForSchedules() {
		waitingForSchedules = true;
		synchronized (scheduleLock) {
			try {
				scheduleLock.wait();
			} catch (InterruptedException ignored) {}
		}
	}

	/**
	 * Adds some code to the queue to run on main thread,
	 * and waits for it to finish
	 * @param step The {@link Runnable} to execute next tick
	 */
	public static void waitForStep(Runnable step) {
		waitingForRunnable = true;
		ScarpetAppTester.getTaskQueue().add(step);
		synchronized (stepLock) {
			try {
				stepLock.wait();
			} catch (InterruptedException ignored) {}
		}
	}
}
