package altrisi.scarpetapptester.scarpetapi;

import java.util.Collections;

import altrisi.scarpetapptester.testing.subjects.TestSubject;
import altrisi.scarpetapptester.testing.tests.Test;
import carpet.script.CarpetContext;
import carpet.script.CarpetEventServer;
import carpet.script.Context;
import carpet.script.annotation.AnnotationParser;
import carpet.script.annotation.ScarpetFunction;
import carpet.script.annotation.SimpleTypeConverter;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.FunctionValue;
import carpet.script.value.Value;

public class ScarpetAPI {
	public static void registerOurThings() {
    	SimpleTypeConverter.registerType(TestSubjectValue.class, TestSubject.class, TestSubjectValue::getTestSubject, "test subject");
    	SimpleTypeConverter.registerType(TestValue.class, Test.class, TestValue::getTest, "test");
    	AnnotationParser.parseFunctionClass(ScarpetAPI.class);
	}
	
	@ScarpetFunction(maxParams = 6)
	public TestValue create_test(Context context, String name, Value subject, Value... callValues) {
		if (callValues.length != 4) throw new InternalExpressionException("'create_test' requires 4 callbacks");
		CarpetEventServer.ScheduledCall[] calls = new CarpetEventServer.ScheduledCall[4];
		for (int i = 0; i < 4; i++) {
			Value val = callValues[i];
			FunctionValue function;
			if (val instanceof FunctionValue) {
				function = (FunctionValue) val;
			} else {
				if (val == null) {
					function = null;
					continue;
				}
	            String fnName = val.getString();
	            function = context.host.getAssertFunction(context.host.main, fnName);
	        }
			calls[i] = new CarpetEventServer.ScheduledCall((CarpetContext) context, function, Collections.emptyList(), 0);
		}
		return TestValue.createNewTest(name, null, calls[0], calls[1], calls[2], calls[3]);
	}
	
	@ScarpetFunction
	public void add_result(Test test, String left, String right) {
		test.addResult(left, right);
	}
	
	@ScarpetFunction
	public void set_failed(Test test) {
		test.setFailed();
	}
}
