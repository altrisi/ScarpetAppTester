package altrisi.scarpetapptester;

import altrisi.scarpetapptester.tests.App;

public enum AppTesterThread {
	INSTANCE;
	
	private App currentApp = null;
	
	/**
	 * @return The current instance of {@link App} being tested
	 */
	public App getCurrentApp() {
		return currentApp;
	}
}
