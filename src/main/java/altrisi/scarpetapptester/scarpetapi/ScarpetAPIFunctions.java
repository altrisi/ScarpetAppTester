package altrisi.scarpetapptester.scarpetapi;

import java.util.ArrayList;
import java.util.List;

import altrisi.scarpetapptester.testing.subjects.TestSubject;
import carpet.script.CarpetContext;
import carpet.script.CarpetEventServer;
import carpet.script.Expression;
import carpet.script.argument.FunctionArgument;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.Value;

public class ScarpetAPIFunctions {

	public static void apply(Expression expression) { //TODO Fix locator to not consume everything
		// TODO Add managing functions for checker apps
		expression.addContextFunction("create_test", 6, (c, t, lv) -> {
			Value evaling = lv.get(0);
			String name = evaling.getString();
			evaling = lv.get(1);
			if (!(evaling instanceof TestSubjectValue))
				throw new InternalExpressionException("Second argument of create_and_get_test must be a TestSubject");
			TestSubject subject = ((TestSubjectValue) evaling).subject;
			List<CarpetEventServer.ScheduledCall> calls = new ArrayList<CarpetEventServer.ScheduledCall>();
			for (int i = 2; i < 6; i++) {
				FunctionArgument function = FunctionArgument.findIn(c, expression.module, lv, i, true, false);
				calls.add(new CarpetEventServer.ScheduledCall((CarpetContext)c, function.function, function.args, 0));
			}
			Value retVal = TestValue.createNewTest(name, subject, calls.get(0), calls.get(1), calls.get(2), calls.get(3));
			return retVal;
		});
	}
}
