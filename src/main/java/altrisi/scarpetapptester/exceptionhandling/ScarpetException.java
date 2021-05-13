package altrisi.scarpetapptester.exceptionhandling;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import altrisi.scarpetapptester.AppTester;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.ExpressionException;
import carpet.script.exception.ProcessedThrowStatement;
import carpet.script.value.FunctionValue;

/**
 * @param context This exception's {@link Context}
 * @param expression This exception's {@link Expression}
 * @param stack This exception's stack
 * @param token This exception's tokenizer's token
 * @param message This exception's message
 * @param actualException The actual {@link ExpressionException} this exception holds
 * @param couldBeHandled Whether this exception came from a ThrowStatement
 * @param time When this exception was received
 * @author altrisi
 *
 */
public record ScarpetException(Context context, Expression expression, List<FunctionValue> stack, Tokenizer.Token token, String message,
		ExpressionException actualException, boolean couldBeHandled, String time, boolean[] unhandled) {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
	
	public ScarpetException(Expression expression, String message, ExpressionException actualException) {
		this(actualException.context, expression, 
				actualException.stack, actualException.token, 
				message, actualException, actualException instanceof ProcessedThrowStatement, dateFormat.format(new Date()), 
				new boolean[1]);
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
	@Deprecated public boolean[] unhandled(){ throw new UnsupportedOperationException("Ehm, no, don't use this"); }
}
