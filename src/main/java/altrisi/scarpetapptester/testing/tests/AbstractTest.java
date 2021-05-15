package altrisi.scarpetapptester.testing.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.testing.apps.App;

abstract class AbstractTest implements Test {
	protected final App app;
	private final String name;
	protected TestStage stage = TestStage.WAITING;
	protected List<ScarpetException> exceptions = new ArrayList<>();
	protected Map<String, String> results = new HashMap<>();
	protected boolean failed;
	
	public AbstractTest(App app, String name) {
		this.app = app;
		this.name = name;
	}
	
	@Override
	public App getApp() {
		return app;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setStage(TestStage stage) {
		if (this.stage == TestStage.FINISHED)
			throw new UnsupportedOperationException("Test already finished");
		this.stage = stage; 
	}

	@Override
	public TestStage getTestStage() {
		return stage;
	}
	
	@Override
	public void attachException(ScarpetException exception) {
		exceptions.add(exception);
	}
	
	@Override
	public void addResult(String left, String right) {
		results.put(left, right);
	}
	
	@Override
	public void setFailed() {
		this.failed = true;
	}

}
