package altrisi.scarpetapptester.tests;

import altrisi.scarpetapptester.scarpetapi.ScarpetEvents;

public interface Test {
	String getAppName();
	
	String getTestName();
	
	/**
	 * Runs before testing starts, waiting for schedules to
	 * finish before actually starting the tests<br>
	 * By default, calls Scarpet test handlers<br>
	 * Gets queued into main thread, on next tick	
	 */
	default void preTesting() {
		ScarpetEvents.PRE_TEST_STARTED.dispatch(this);
	}
	
	/**
	 * Runs the test and all its precendents and antecedents
	 * Should NOT be overriden, since it should contain all the required code
	 */
	default public void run() {
		TestUtils.waitForStep(() -> {
			preTesting();
		});
		TestUtils.waitForSchedules();
		
		TestUtils.waitForStep(() -> {
			rightBeforeTestingStarts();
			runTests();
			rightAfterTestingStarts();
		});
		TestUtils.waitForSchedules();
		
		
		
		TestUtils.waitForStep(() -> {
			afterTesting();
		});
		TestUtils.waitForSchedules();
		
		finishTest();
	}
	
	/**
	 * Runs right before testing starts, without waiting
	 * for schedules.
	 * It is executed in main thread.
	 * By default, contains the Scarpet event
	 */
	default void rightBeforeTestingStarts() {
		ScarpetEvents.RIGHT_BEFORE_TEST_STARTED.dispatch(this);
	}
	
	/**
	 * Runs right after testing starts, without waiting
	 * for schedules.
	 * It is executed in main thread.
	 * By default, contains the Scarpet event
	 */
	default void rightAfterTestingStarts() {
		ScarpetEvents.RIGHT_AFTER_TEST_STARTED.dispatch(this);
	}
	
	/**
	 * Runs after the test ends, waiting for schedules to
	 * finish before returning execution<br>
	 * By default, calls Scarpet test handlers<br>
	 * Gets queued into main thread, on next tick	
	 */
	default void afterTesting() {
		ScarpetEvents.TEST_FINISHED.dispatch(this);
	}
	
	/**
	 * Runs after testing ends.
	 * Is independent from main thread	
	 */
	void finishTest();
	
	/**
	 * Is called by the test_finished Scarpet event, to 
	 * provide data to the testing assist app
	 * @return Whether or not tests are good so far
	 */
	boolean successfulSoFar();
	
	/**
	 * This function defines what should be run for a test<br>
	 * This shouldn't include pre-testing or post-testing content,
	 * and shouldn't be called directly.
	 * @see #run()
	 */
	void runTests();
	
	/**
	 * Is called when the test has been executed, but before {@link #afterTesting()}
	 * This should run whatever checks can be done at this point in order to
	 * communicate the result to the testing assist app<br>
	 * Basically this is after all schedules have finished.<br>
	 * Runs on secondary thread
	 */
	void testFinishedChecks();
	
	public TestResults getResults();
}
