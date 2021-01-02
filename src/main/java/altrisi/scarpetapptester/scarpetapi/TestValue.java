package altrisi.scarpetapptester.scarpetapi;

import altrisi.scarpetapptester.tests.ScarpetGeneratedTest;
import altrisi.scarpetapptester.tests.Test;
import altrisi.scarpetapptester.tests.TestSubject;
import carpet.script.Context;
import carpet.script.value.FunctionValue;
import carpet.script.value.Value;
import net.minecraft.nbt.Tag;

public class TestValue extends Value {
	private Test test;
	
	public TestValue(Test test) {
		this.test = test;
	}
	
	/**
	 * Creates a new Test from Scarpet
	 * @param name The test from Scarpet
	 * @param function The function to test TODO Change this to a proper thing that can test commands, etc
	 * @param prepare What to run a lot before tests
	 * @param pre What to run right before tests start
	 * @param post What to run right after tests start
	 * @param check What to run after tests have concluded
	 * @return The new Test wrapped into a TestValue
	 */
	public static TestValue createNewTest(String name, TestSubject fn, FunctionValue prepare, FunctionValue pre, FunctionValue post, FunctionValue check, Context ctx) {
		return new TestValue(new ScarpetGeneratedTest(name, fn, prepare, pre, post, check, ctx));
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("You can't clone tests!");
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof TestValue))
			return false;
		return this.test == ((TestValue)o).test;
	}
	
	public Test getTest() {
		return test;
	}
	
	@Override
	public String getString() {
		return test.getTestName();
	}

	@Override
	public boolean getBoolean() {
		return test.successfulSoFar();
	}

	@Override
	public Tag toTag(boolean force) {
		return null;
	}

}
