package altrisi.scarpetapptester.testing.apps;

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
	 * Setting it to {@link AppStatus#CRITICAL_FAILURE} will stop testing
	 * @param status The {@link AppStatus} to set for this app.
	 */
	//public void setStatus(AppStatus status);
	
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
	 * Calls the app to load. Throws if already loaded or loading
	 */
	public void load();
	
	/**
	 * Calls the app to run tests. Throws if already ran or running
	 */
	public void runTests();
	
	/**
	 * Calls the app to unload
	 */
	public void unload();
}
