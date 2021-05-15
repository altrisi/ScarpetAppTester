package altrisi.scarpetapptester.testing.apps;

import altrisi.scarpetapptester.config.AppConfig;
import altrisi.scarpetapptester.testing.tests.Test;
import carpet.script.CarpetScriptHost;

public interface App {
	/**
	 * @return The app's name. Must match app's loadable name 
	 */
	public String name();
	
	/**
	 * @return The current test
	 * @throws UnsupportedOperationException if there are no tests running
	 */
	public Test currentTest();
	
	/**
	 * @return The current testing status of the app
	 */
	public AppStatus currentStatus();
	
	/**
	 * Sets the {@link AppStatus} to {@link AppStatus#CRITICAL_FAILURE} and cancels
	 * further testing of the app
	 */
	public void abort();
	
	/**
	 * @return The {@link CarpetScriptHost} of this app once loaded. Throws if not loaded
	 */
	public CarpetScriptHost getHost();
	
	/**
	 * Calls the app to load. Throws if already loaded or loading. Sets status to {@link AppStatus#JUST_LOADED} when loaded, to {@link AppStatus#LOADING} 
	 * when loading
	 */
	public void load();
	
	/**
	 * Calls the app to prepare tests. Must be called when app is loaded, and must call the Scarpet event handler. 
	 * Sets status to {@link AppStatus#TESTS_READY} when done, {@link AppStatus#PREPARING_TESTS} while preparing tests
	 */
	public void prepareTests(AppConfig config);
	
	/**
	 * Calls the app to run tests. Throws if already ran or running
	 */
	public void runTests();
	
	/**
	 * Calls the app to unload
	 */
	public void unload();
}
