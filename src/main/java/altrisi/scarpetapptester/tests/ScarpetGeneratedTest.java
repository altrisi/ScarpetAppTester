package altrisi.scarpetapptester.tests;

import altrisi.scarpetapptester.AppTesterThread;
import carpet.script.CarpetEventServer.ScheduledCall;

public class ScarpetGeneratedTest extends AbstractTest {
	private ScheduledCall prepare, pre, post, check;
	private TestSubject testSubject;
	
	public ScarpetGeneratedTest(String name, TestSubject test, ScheduledCall prepare, ScheduledCall pre, ScheduledCall post, ScheduledCall check) {
		super(AppTesterThread.INSTANCE.getCurrentApp(), name); // We can't (I don't want to, would need new Value) to allow specifying apps
		this.prepare = prepare;
		this.pre = pre;
		this.post = post;
		this.testSubject = test;
		this.check = check;
	}

	@Override
	public void runTests() {
		testSubject.call();
	}
	
	@Override
	public void preTesting() {
		prepare.execute();;
	}
	
	@Override
	public void rightBeforeTestingStarts() {
		pre.execute();;
	}
	
	@Override
	public void rightAfterTestingStarts() {
		post.execute();
	}
	
	@Override
	public void afterTesting() {
		check.execute();
	}

	// We can't use those, since we need to run on main thread (not really "need", but are probably expected to)
	@Override public boolean successfulSoFar() { return true; }
	@Override public void testFinishedChecks() { }
	@Override public void finishTest() { }
	@Override public void prepareTest() { }

}
