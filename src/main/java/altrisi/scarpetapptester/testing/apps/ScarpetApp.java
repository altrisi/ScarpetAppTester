package altrisi.scarpetapptester.testing.apps;

import java.util.ArrayList;
import java.util.List;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.ThreadingUtils;
import altrisi.scarpetapptester.config.AppConfig;
import altrisi.scarpetapptester.scarpetapi.ScarpetEvents;
import altrisi.scarpetapptester.testing.tests.LoadingTest;
import altrisi.scarpetapptester.testing.tests.Test;
import carpet.CarpetServer;
import carpet.script.CarpetScriptHost;

import static altrisi.scarpetapptester.testing.apps.AppStatus.*;

public class ScarpetApp implements App {
	private final String name;
	private Test currentTest;
	private AppStatus status = QUEUED;
	private List<Test> tests = new ArrayList<>();
	private CarpetScriptHost host;
	
	public ScarpetApp(String name) {
		this.name = name;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public Test currentTest() {
		if (currentTest == null)
			throw new IllegalStateException("No tests running");
		return currentTest;
	}

	@Override
	public AppStatus currentStatus() {
		return status;
	}
	
	@Override
	public void load() {
		if (status != QUEUED)
			throw new IllegalStateException("App has already started");
		status = LOADING;
		currentTest = new LoadingTest(this);
		currentTest.run();
		host = ((LoadingTest)currentTest).resultHost;
		currentTest = null;
		status = JUST_LOADED;
	}
	
	@Override
	public void prepareTests(AppConfig config) {
		status = PREPARING_TESTS;
		//...
		ScarpetEvents.PREPARING_TESTS.dispatch(this);
		status = TESTS_READY;
	}
	
	@Override
	public void runTests() {
		if (status != TESTS_READY)
			throw new IllegalStateException();
		status = RUNNING_TESTS;
		for (Test test : tests) {
			if (status == CRITICAL_FAILURE) {
				break;
			}
			currentTest = test;
			currentTest.run();
		}
		currentTest = null;
		if (status != CRITICAL_FAILURE) {
			status = FINISHING_TESTS;
		}
	}
	
	@Override
	public void unload() {
		status = UNLOADING;
		ThreadingUtils.runInMainThreadAndWait(() -> {
			CarpetServer.scriptServer.removeScriptHost(ScarpetAppTester.commandSource, name, false, false);
		});
		AppTester.LOGGER.info("Unloaded app " + name);
		status = UNLOADED;
	}
	
	@Override
	public CarpetScriptHost getHost() {
		if (host == null)
			throw new IllegalStateException("App doesn't currently have a host attached");
		return host;
	}

	@Override
	public void abort() {
		// TODO Auto-generated method stub
		status = CRITICAL_FAILURE;
		AppTester.LOGGER.info("Critical failure during testing, aborting");
	}
	
	@Override
	public String toString() {
		return name;
	}

}
