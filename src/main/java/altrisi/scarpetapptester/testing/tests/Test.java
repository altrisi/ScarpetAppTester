package altrisi.scarpetapptester.testing.tests;

import static altrisi.scarpetapptester.testing.tests.TestStage.*;

import altrisi.scarpetapptester.ThreadingUtils;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.scarpetapi.ScarpetEvents;
import altrisi.scarpetapptester.testing.apps.App;

public interface Test {
	/**
	 * @return the {@link App} that is being tested
	 */
	public App getApp();
	
	/**
	 * @return the name of the test
	 */
	public String getName();
	
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
	 * Should NOT be overridden, since it should contain all the required code
	 */
	default public void run() {
		if (getTestStage() != WAITING)
			throw new UnsupportedOperationException("The test has already started!");
		setStage(ASYNC_PREPARING);
		prepareTest();
		
		setStage(SYNC_PREPARING);
		ThreadingUtils.runInMainThreadAndWait(this::preTesting);
		ThreadingUtils.waitForSchedules();
		
		setStage(RUNNING);
		ThreadingUtils.runInMainThreadAndWait(() -> {
			rightBeforeTestingStarts();
			runTests();
			rightAfterTestingStarts();
		});
		ThreadingUtils.waitForSchedules();
		
		setStage(ASYNC_CHECKING);
		testFinishedChecks();
		
		setStage(SYNC_CHECKING);
		ThreadingUtils.runInMainThreadAndWait(this::afterTesting);
		ThreadingUtils.waitForSchedules();
		
		setStage(FINISHING);
		finishTest();
		
		setStage(FINISHED);
	}
	
	/**
	 * Runs right before testing starts, without waiting
	 * for schedules.
	 * It is executed in main thread.
	 * By default, dispatches the Scarpet event
	 */
	default void rightBeforeTestingStarts() {
		ScarpetEvents.RIGHT_BEFORE_TEST_STARTED.dispatch(this);
	}
	
	/**
	 * Runs right after testing starts, without waiting
	 * for schedules.
	 * It is executed in main thread.
	 * By default, dispatches the Scarpet event
	 */
	default void rightAfterTestingStarts() {
		ScarpetEvents.RIGHT_AFTER_TEST_STARTED.dispatch(this);
	}
	
	/**
	 * Runs after the test ends, waiting for schedules to
	 * finish before returning execution<br>
	 * By default, dispatches Scarpet test handlers<br>
	 * Gets queued into main thread, on next tick	
	 */
	default void afterTesting() {
		ScarpetEvents.TEST_FINISHED.dispatch(this);
	}
	
	/**
	 * Runs after testing ends.<br>
	 * Is independent from main thread
	 */
	void finishTest();
	
	/**
	 * Is called by the test_finished Scarpet event dispatcher, to 
	 * provide data to the testing assist app
	 * @return Whether or not tests are good so far
	 */
	public boolean successfulSoFar();
	
	/**
	 * This function defines what should be run for a test<br>
	 * This is NOT the function to start the test.<br>
	 * Runs in main thread
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
	
	/**
	 * Is called the first of all methods in run,
	 * in secondary thread, in order to prepare the environment to be ready.
	 * Runs before {@link #preTesting()}, which is in main thread
	 */
	void prepareTest();
	
	/**
	 * Sets the test stage to the specified one
	 * @param stage The stage to set
	 * @throws UnsupportedOperationException if test is already FINISHED
	 */
	void setStage(TestStage stage);
	
	/**
	 * @return Current {@link TestStage}
	 */
	public TestStage getTestStage();
	
	public TestResults getResults();
	
	/**
	 * Attaches an exception that occurred during the test
	 * @param exception The exception
	 */
	public void attachException(ScarpetException exception);
	
}
