package altrisi.scarpetapptester.testing.tests;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.testing.subjects.TestSubject;
import carpet.script.CarpetEventServer.ScheduledCall;

/**
 * NOT PUBLIC API.
 * INTERNAL USE ONLY
 */
public class ScarpetGeneratedTest extends AbstractTest {
	private ScheduledCall prepare, pre, post, check;
	private TestSubject testSubject;
	
	public ScarpetGeneratedTest(String name, TestSubject test, ScheduledCall prepare, ScheduledCall pre, ScheduledCall post, ScheduledCall check) {
		super(AppTester.INSTANCE.getCurrentApp(), name); // We can't (I don't want to, would need new Value) to allow specifying app
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
		prepare.execute();
	}
	
	@Override
	public void rightBeforeTestingStarts() {
		pre.execute();
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

	@Override
	public TestResults getResults() {
		// TODO Auto-generated method stub
		return null;
	}

}
