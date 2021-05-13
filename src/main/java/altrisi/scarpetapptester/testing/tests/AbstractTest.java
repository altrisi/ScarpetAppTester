package altrisi.scarpetapptester.testing.tests;

import altrisi.scarpetapptester.testing.apps.App;

abstract class AbstractTest implements Test {
	protected final App app;
	private final String name;
	protected TestStage stage = TestStage.WAITING;
	
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

}
