package altrisi.scarpetapptester.testing.tests;

import java.util.Map;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.testing.apps.App;
import carpet.CarpetServer;
import carpet.script.CarpetScriptHost;

public class LoadingTest extends AbstractTest {
	private boolean success;
	public CarpetScriptHost resultHost;
	private long elapsedTime;

	public LoadingTest(App app) {
		super(app, "App Loading");
	}

	@Override
	public void finishTest() {
		if (success) {
			resultHost = (CarpetScriptHost) CarpetServer.scriptServer.getHostByName(app.name());
		} else {
			AppTester.LOGGER.fatal("Failed to load app " + app);
		}
	}

	@Override
	public boolean successfulSoFar() {
		return success;
	}

	@Override
	public void runTests() {
		long start = System.nanoTime();
		success = CarpetServer.scriptServer.addScriptHost(ScarpetAppTester.commandSource, app.name(), null, true, false, false);
		long end = System.nanoTime();
		elapsedTime = (end-start)/1000000;
	}

	@Override
	public void testFinishedChecks() {}

	@Override
	public void prepareTest() {}

	@Override
	public TestResults getResults() {
		return new LoadResult(success, elapsedTime);
	}
	
	public record LoadResult(boolean success, long elapsedTime) implements TestResults {
		@Override
		public Map<String, String> getResultsMap() {
			return Map.of("App load", success ? "Correct" : "Failed", "Elapsed time", elapsedTime + "ms");
		}
		
	}
}
