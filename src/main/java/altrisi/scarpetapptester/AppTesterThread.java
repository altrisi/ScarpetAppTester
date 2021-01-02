package altrisi.scarpetapptester;

import altrisi.scarpetapptester.tests.App;

public enum AppTesterThread {
	INSTANCE;
	
	private App currentApp = null;
	
	
	public App getCurrentApp() {
		return currentApp;
	}
}
