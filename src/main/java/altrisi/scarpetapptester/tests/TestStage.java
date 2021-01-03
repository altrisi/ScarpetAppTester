package altrisi.scarpetapptester.tests;

public enum TestStage {
	/**
	 * The test hasn't started yet, and
	 * it's waiting in its queue
	 */
	WAITING,
	/**
	 * The test is undergoing preparation in the
	 * async thread
	 */
	ASYNC_PREPARING,
	/**
	 * The test is undergoing preparation in the 
	 * server thread<br>
	 * This includes preparation from the test
	 * Scarpet apps
	 */
	SYNC_PREPARING,
	/**
	 * The test is running, or running right before
	 * or right after sync operations
	 */
	RUNNING,
	/**
	 * The test results are being checked async
	 */
	ASYNC_CHECKING,
	/**
	 * The test is being checked in the
	 * server thread<br>
	 * This includes checking from the test 
	 * Scarpet apps
	 */
	SYNC_CHECKING,
	/**
	 * The test is being concluded in the async thread
	 */
	FINISHING,
	/**
	 * The test is finished
	 */
	FINISHED;
}
