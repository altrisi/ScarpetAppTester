package altrisi.scarpetapptester.exceptionhandling;

import java.util.List;
import altrisi.scarpetapptester.fakes.ActualScarpetException;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.CarpetExpressionException;
import carpet.script.exception.ExpressionException;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.FunctionValue;

import static altrisi.scarpetapptester.exceptionhandling.ExceptionTypes.*;

public class ScarpetException {
    private final Context context;
    private final Expression expression;
    private final List<FunctionValue> stack;
	private final Tokenizer.Token token;
	private final String message;
	private final String chatMessage;
	private final ActualScarpetException exception;
	private final ExceptionTypes type;
	private ScarpetException parent;
	private boolean rethrown = false;
	
	// EXPRESSION_EXCEPTION and general for parents
	public ScarpetException(Context context, Expression expression, Tokenizer.Token token, String message, List<FunctionValue> stack, ActualScarpetException e) {
		this.context = context;
		this.expression = expression;
		this.token = token;
		this.message = message;
		this.stack = stack;
		this.type = ExceptionTypes.ofCarpetException(e);
		this.exception = e;
		chatMessage = e instanceof InternalExpressionException ? null : ((RuntimeException) e).getMessage();
	}
	
	// INTERNAL_EXPRESSION_EXCEPTION
	public ScarpetException(String message, ActualScarpetException e) {
		this(null, null, null, message, ((InternalExpressionException)e).stack, e);
	}
	
	// CARPET_EXPRESSION_EXCEPTION
	public ScarpetException(String message, List<FunctionValue> stack, ActualScarpetException e) {
		this(null, null, null, message, stack, e);
	}
	
	/**
	 * Moves this exception's contents to a copy of parentException,
	 * which should be a {@link CarpetExpressionException}, and this a
	 * {@link ExpressionException}
	 * @param childException The parent to populate
	 * @return A new {@link ScarpetException} with the new data
	 * @throws UnsupportedOperationException If the above conditions aren't met
	 */
	public ScarpetException copyAs(ScarpetException newParent) {
		if (newParent.type != CARPET_EXPRESSION_EXCEPTION || type != EXPRESSION_EXCEPTION)
			throw new UnsupportedOperationException("Parent type must be CARPET_EXPRESSION_EXCEPTION and this an EXPRESSION_EXCEPTION");
		ScarpetException newExc = new ScarpetException(context, expression, token, message, newParent.stack, newParent.exception);
		this.parent = newParent;
		return newExc;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public ScarpetException getParentException() {
		if (!hasParent())
			throw new UnsupportedOperationException("This ScarpetException has no parent!");
		return parent;
	}
	
	void setRethrown(ScarpetException parent){
		if (type == CARPET_EXPRESSION_EXCEPTION)
			throw new UnsupportedOperationException("A CarpetExpressionException can't have been rethrown!");
		if (hasParent())
			throw new UnsupportedOperationException("This ScarpetExpression already has a parent!");
		this.parent = parent;
	}
	
	public String getMessage() {
		return message;
	}
	
	public ExceptionTypes getType() {
		return type;
	}
	
	public boolean wasRethrown() {
		return rethrown;
	}
	
	public ActualScarpetException getException() {
		return exception;
	}
	
	public Context getContext() {
		checkDataAvailability();
		return context;
	}
	
	public Expression getExpression() {
		checkDataAvailability();
		return expression;
	}
	
	public Tokenizer.Token getToken() {
		checkDataAvailability();
		return token;
	}
	
	public List<FunctionValue> getStack() {
		return stack;
	}
	
	public String getChatMessage() {
		if (type == INTERNAL_EXPRESSION_EXCEPTION)
			throw new UnsupportedOperationException("Internal exceptions don't provide that data!");
		return chatMessage;
	}
	
	private void checkDataAvailability() {
		if (type == INTERNAL_EXPRESSION_EXCEPTION)
			throw new UnsupportedOperationException("Internal exceptions don't provide that data!");
		if (type == CARPET_EXPRESSION_EXCEPTION && token != null)
			throw new UnsupportedOperationException("This CarpetExpressionException hasn't been upgraded to have that data!");
	}
}
