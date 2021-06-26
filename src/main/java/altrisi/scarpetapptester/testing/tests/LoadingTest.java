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
	private long functionCount;
	private long globalVars;

	public LoadingTest(App app) {
		super(app, "App Loading");
	}

	@Override
	public void finishTest() {
		if (success) {
			resultHost = (CarpetScriptHost) CarpetServer.scriptServer.getHostByName(app.name());
			functionCount = resultHost.globalFunctionNames(resultHost.main, s -> true).count();
			globalVars = resultHost.globalVariableNames(resultHost.main, s -> true).count();
		} else {
			AppTester.LOGGER.fatal("Failed to load app " + app);
			AppTester.INSTANCE.currentApp().abort();
		}
	}

	@Override
	public boolean successfulSoFar() {
		return success;
	}

	@Override
	public void runTests() {
		long start = System.nanoTime();
		success = CarpetServer.scriptServer.addScriptHost(ScarpetAppTester.commandSource, app.name(), null, true, false, false, null);
		long end = System.nanoTime();
		elapsedTime = (end - start)/1000000;
	}

	@Override
	public void testFinishedChecks() {
		if (success && exception != null) { // Exception in a schedule after correct load
			success = false; // TODO Decide whether to allow continuing with an exception in a schedule
		}
	}

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
		return new LoadResult(success, elapsedTime, functionCount, globalVars, exception == null ? Collections.singletonList(exception) : Collections.emptyList());
	}
	
	public record LoadResult(boolean successful, long elapsedTime, long functionNumber, long globalVars, List<ScarpetException> exceptions) 
		implements TestResults {
		@Override
		public Map<String, String> getResultsMap() {
			return Map.of("App load", successful ? "Correct" : "Failed",
					"Elapsed time", elapsedTime + "ms", 
					"Function count", successful ? Long.toString(functionNumber) : "N/A",
					"Global variable count", successful ? Long.toString(globalVars) : "N/A");
		}
		
	}
}
