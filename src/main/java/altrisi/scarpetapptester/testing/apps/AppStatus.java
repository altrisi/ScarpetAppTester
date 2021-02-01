package altrisi.scarpetapptester.testing.apps;

/**
 * The status of an {@link App} that has been scheduled
 * to be tested 
 * @author altrisi
 *
 */
public enum AppStatus {
	/**
	 * The app has been queued to be tested, but it still
	 * hasn't started.
	 */
	QUEUED,
	/**
	 * The app is not yet loaded, but it's in the first
	 * position of the queue
	 */
	NOT_YET_LOADED,
	/**
	 * The app is being loaded
	 */
	LOADING,
	/**
	 * The app has just been loaded, and it's waiting
	 * for tests to start
	 */
	JUST_LOADED,
	/**
	 * The tests for the app are being prepared
	 */
	PREPARING_TESTS,
	/**
	 * App's tests are being ran
	 */
	RUNNING_TESTS,
	/**
	 * App's tests are finishing
	 */
	FINISHING_TESTS,
	/**
	 * App is being cleaned up
	 */
	CLEANING_UP,
	/**
	 * App is unloading
	 */
	UNLOADING,
	/**
	 * App has been already unloaded
	 */
	ALREADY_UNLOADED,
	/**
	 * App testing has finished, and other apps can be tested now
	 */
	FINISHED;
}