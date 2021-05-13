package altrisi.scarpetapptester.testing.tests;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.testing.apps.App;
import carpet.CarpetServer;
import carpet.script.CarpetScriptHost;

public class LoadingTest extends AbstractTest {
	private boolean loaded;
	public CarpetScriptHost resultHost;

	public LoadingTest(App app) {
		super(app, "App Loading");
	}

	@Override
	public void finishTest() {
		if (loaded) {
			resultHost = (CarpetScriptHost) CarpetServer.scriptServer.getHostByName(app.name());
		} else {
			AppTester.LOGGER.fatal("Failed to load app " + app);
		}
	}

	@Override
	public boolean successfulSoFar() {
		return loaded;
	}

	@Override
	public void runTests() {
		loaded = CarpetServer.scriptServer.addScriptHost(ScarpetAppTester.commandSource, app.name(), null, true, false, false);
	}

	@Override
	public void testFinishedChecks() {}

	@Override
	public void prepareTest() {}

	@Override
	public TestResults getResults() {
		return () -> !loaded ? "App failed to load" : "App got loaded in ";//+todo time;
	}
}
