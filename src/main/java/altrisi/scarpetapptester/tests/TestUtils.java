package altrisi.scarpetapptester.tests;

import java.util.concurrent.CountDownLatch;

import altrisi.scarpetapptester.ScarpetAppTester;

public class TestUtils {
	private static CountDownLatch stepLatch = new CountDownLatch(0);
	private static CountDownLatch schedulesLatch = new CountDownLatch(0);
	
	public static void waitForSchedules() {
		try {
			schedulesLatch = new CountDownLatch(1);
			schedulesLatch.await();
		} catch (InterruptedException ignored) {}
	}

	/**
	 * Adds some code to the queue to run on main thread,
	 * and waits for it to finish
	 * @param step The {@link Runnable} to execute next tick
	 */
	public static void waitForStep(Runnable step) {
		try {
			ScarpetAppTester.getTaskQueue().put(step);
			stepLatch = new CountDownLatch(1);
			stepLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static CountDownLatch getStepLatch() {
		return stepLatch;
	}
	
	public static CountDownLatch getSchedulesLatch() {
		return schedulesLatch;
	}
}
