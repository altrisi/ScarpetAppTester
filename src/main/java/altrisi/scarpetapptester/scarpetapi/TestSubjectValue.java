package altrisi.scarpetapptester.scarpetapi;

import altrisi.scarpetapptester.tests.TestSubject;
import carpet.script.value.FrameworkValue;
import carpet.script.value.Value;

public class TestSubjectValue extends FrameworkValue {

	private final TestSubject subject;
	
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
	
	/**
	 * Gets the test subject associated to this {@link Value}
	 * @return The {@link TestSubject}
	 */
	public TestSubject getSubject() {
		return subject;
	}
}
