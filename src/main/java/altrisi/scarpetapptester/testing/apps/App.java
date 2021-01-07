package altrisi.scarpetapptester.testing.apps;

import altrisi.scarpetapptester.testing.tests.Test;

public interface App {
	/**
	 * @return The app's name
	 */
	public String getName();
	
	/**
	 * @return The current test
	 * @throws UnsupportedOperationException if there are no tests running
	 */
	public Test getCurrentTest() throws UnsupportedOperationException;
	
	/**
	 * @return The current testing status of the app
	 */
	public AppStatus getCurrentStatus();
	
	void setStatus(AppStatus status);
	
}
