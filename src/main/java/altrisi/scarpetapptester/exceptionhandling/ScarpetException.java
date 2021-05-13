package altrisi.scarpetapptester.exceptionhandling;

import java.util.List;

import altrisi.scarpetapptester.AppTester;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.ExpressionException;
import carpet.script.exception.ProcessedThrowStatement;
import carpet.script.value.FunctionValue;

public record ScarpetException(Context context, Expression expression, List<FunctionValue> stack, Tokenizer.Token token, String message,
		ExpressionException actualException, boolean couldBeHandled, boolean[] unhandled) {
	
	public ScarpetException(Expression expression, String message, ExpressionException actualException) {
		this(actualException.context, expression, 
				actualException.stack, actualException.token, 
				message, actualException, actualException instanceof ProcessedThrowStatement, 
				new boolean[0]);
	}
	
	/**
	 * Marks that this exception has gotten out of the stack
	 */
	public void setUnhandled() {
		if (unhandled[0]) AppTester.LOGGER.warn("An exception was marked as unhandled twice, please report!");
		unhandled[0] = true;
	}
	
	public boolean wasUnhandled() {
		return unhandled[0];
	}
	
	/**
	 * @deprecated Don't use, just required because of record
	 * @see #wasUnhandled()
	 */
	@Deprecated
	public boolean[] unhandled(){
		throw new UnsupportedOperationException("Ehm, no, don't use this");
	}
}
