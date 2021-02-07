package altrisi.scarpetapptester.exceptionhandling;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.value.FunctionValue;

public class ScarpetException {
    private Context context;
    private final Expression expression;
    private List<FunctionValue> stack;
	private final Tokenizer.Token token;
	private final String message;
	private boolean dataComplete = false;
	//private final String chatMessage;
	
	public ScarpetException(Expression expression, Tokenizer.Token token, String message) {
		this.expression = expression;
		this.token = token;
		this.message = message;
	}
	
	/**
	 * Populate a yet-unfinished exception with its context and stack
	 * @param ctx The {@link Context} of where the exception occurred
	 * @param stack The exception's stack. Should be the same object as the exception's
	 *              to allow populating it lazily  
	 * @throws UnsupportedOperationException if the exception is already populated
	 */
	public void populate(Context ctx, List<FunctionValue> stack) {
		if (dataComplete) throw new UnsupportedOperationException("Exception already populated!");
		this.context = Objects.requireNonNull(ctx);
		this.stack = Objects.requireNonNull(stack);
		dataComplete = true;
	}
	
	/**
	 * @return Whether this exception's context and stack object have been populated
	 */
	public boolean isDataComplete() {
		return dataComplete;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Context getContext() {
		if (!dataComplete) throw new UnsupportedOperationException("Exception has not been populated yet!");
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
		if (!dataComplete) throw new UnsupportedOperationException("Exception has not been populated yet!");
		return stack;
	}
}
