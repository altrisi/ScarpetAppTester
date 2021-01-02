package altrisi.scarpetapptester.tests;

import java.util.ArrayList;
import java.util.List;

import altrisi.scarpetapptester.AppTesterThread;
import carpet.script.Context;
import carpet.script.LazyValue;
import carpet.script.value.FunctionValue;
import carpet.script.value.StringValue;

public class ScarpetGeneratedTest extends AbstractTest {
	private FunctionValue prepare, pre, post, check;
	private TestSubject testSubject;
	private Context context;
	
	public ScarpetGeneratedTest(String name, TestSubject test, FunctionValue prepare, FunctionValue pre, FunctionValue post, FunctionValue check, Context ctx) {
		super(AppTesterThread.INSTANCE.getCurrentApp(), name);
		this.prepare = prepare;
		this.pre = pre;
		this.post = post;
		this.testSubject = test;
		this.check = check;
		this.context = ctx;
	}

	@Override
	public void runTests() {
		testSubject.call();
	}
	
	@Override
	public void preTesting() {
		callWithArgs(prepare);
	}
	
	@Override
	public void rightBeforeTestingStarts() {
		callWithArgs(pre);
	}
	
	@Override
	public void rightAfterTestingStarts() {
		callWithArgs(post);
	}
	
	@Override
	public void afterTesting() {
		callWithArgs(check);
	}

	private void callWithArgs(FunctionValue function) {
		if (function == null) return;
		List<LazyValue> params = new ArrayList<LazyValue>();
		params.add((c, t) -> new StringValue(getTestName()));
		params.add((c, t) -> new StringValue(getTestStage().toString().toLowerCase()));
		function.callInContext(context, 0, params);
	}

	// We can't use those, since we need to run on main thread
	@Override public boolean successfulSoFar() { return true; }
	@Override public void testFinishedChecks() { }
	@Override public void finishTest() { }
	@Override public void prepareTest() { }

}
