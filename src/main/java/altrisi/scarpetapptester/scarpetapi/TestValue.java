package altrisi.scarpetapptester.scarpetapi;

import altrisi.scarpetapptester.testing.subjects.TestSubject;
import altrisi.scarpetapptester.testing.tests.ScarpetGeneratedTest;
import altrisi.scarpetapptester.testing.tests.Test;
import carpet.script.CarpetEventServer.ScheduledCall;
import carpet.script.value.FrameworkValue;

class TestValue extends FrameworkValue {
	public final Test test;
	
	/**
	 * Creates a {@link TestValue} out of a {@link Test}
	 * @param test The {@link Test} associated with this {@link TestValue}
	 */
	public TestValue(Test test) {
		this.test = test;
	}
	
	/**
	 * Creates a new {@link Test} from Scarpet
	 * @param name The test from Scarpet
	 * @param function The function to test
	 * @param prepare What to run a lot before tests
	 * @param pre What to run right before tests start
	 * @param post What to run right after tests start
	 * @param check What to run after tests have concluded
	 * @return The new {@link Test} wrapped into a {@link TestValue}
	 */
	public static TestValue createNewTest(String name, TestSubject fn, ScheduledCall prepare, ScheduledCall pre, ScheduledCall post, ScheduledCall check) {
		return new TestValue(new ScarpetGeneratedTest(name, fn, prepare, pre, post, check));
	} //TODO Add it to the current App's test pool
	
	@Override
	public boolean equals(Object o) {
		return o instanceof TestValue t && this.test == t.test;
	}
	
	@Override
	public int hashCode() {
		return test.hashCode();
	}
	
	@Override //TODO Reconsider
	public String getString() {
		return test.getName();
	}

}
