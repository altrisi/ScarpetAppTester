package altrisi.scarpetapptester.exceptionhandling;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import altrisi.scarpetapptester.AppTester;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.ExpressionException;
import carpet.script.value.FunctionValue;

public class ScarpetException {
    private final Context context;
    private final Expression expression;
    private List<FunctionValue> stack;
	private final Tokenizer.Token token;
	private final String message;
	private final ExpressionException actualException;
	private boolean unhandled = false; 
	//private final String chatMessage;
	
	public ScarpetException(Expression expression, String message, ExpressionException actualException) {
		this.context = actualException.context;
		this.expression = expression;
		this.token = actualException.token;
		this.message = message;
		this.stack = actualException.stack;
		this.actualException = actualException;
	}
	
	/**
	 * Returns whether this exception has gotten out of the stack
	 */
	public void setUnhandled() {
		if (unhandled) AppTester.LOGGER.warn("An exception was marked as unhandled twice, please report!");
		unhandled = true;
	}
	
	public boolean wasUnhandled() {
		return unhandled;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Context getContext() {
		return context;
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	@Nullable
	public Tokenizer.Token getToken() {
		return token;
	}
	
	public List<FunctionValue> getStack() {
		return stack;
	}
}
