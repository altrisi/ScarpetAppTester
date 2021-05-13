package altrisi.scarpetapptester.testing.tests;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.testing.apps.App;
import carpet.CarpetServer;
import carpet.script.CarpetScriptHost;

public class LoadingTest extends AbstractTest {
	private boolean success;
	public CarpetScriptHost resultHost;
	private long elapsedTime;
	private ScarpetException exception;

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
	public void attachException(ScarpetException exception) {
		this.exception = exception;
	}
	
	@Override
	public TestResults getResults() {
		// Cleanup
		resultHost = null;
		return new LoadResult(success, elapsedTime, exception == null ? Collections.singletonList(exception) : Collections.emptyList());
	}
	
	public record LoadResult(boolean successful, long elapsedTime, List<ScarpetException> exceptions) implements TestResults {
		@Override
		public Map<String, String> getResultsMap() {
			return Map.of("App load", successful ? "Correct" : "Failed", "Elapsed time", elapsedTime + "ms");
		}
		
	}
}
