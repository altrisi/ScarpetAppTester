package altrisi.scarpetapptester.scarpetapi;

import altrisi.scarpetapptester.testing.subjects.TestSubject;
import carpet.script.value.FrameworkValue;

public class TestSubjectValue extends FrameworkValue {

	public final TestSubject subject;
	
	/**
	 * Creates a {@link TestSubjectValue} out of a {@link TestSubject}.<br>
	 * This probably shouldn't be called, since this is only to be used when generating
	 * tests, and those should have their own values
	 * @param subject The {@link TestSubject} associated with this value
	 */
	public TestSubjectValue(TestSubject subject) {
		this.subject = subject;
	}
	
	//TODO Create the test subject
	
	@Override
	public boolean equals(Object o) {
		return o instanceof TestSubjectValue ts && ts.subject == this.subject;
	}
	
	@Override
	public int hashCode() {
		return subject.hashCode();
	}
}
