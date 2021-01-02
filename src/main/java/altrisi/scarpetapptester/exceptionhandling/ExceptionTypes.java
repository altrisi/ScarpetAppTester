package altrisi.scarpetapptester.exceptionhandling;

import altrisi.scarpetapptester.fakes.ActualScarpetException;
import carpet.script.exception.ExpressionException;
import carpet.script.exception.InternalExpressionException;

public enum ExceptionTypes {
	/**
	 * Everything throws this.
	 */
	INTERNAL_EXPRESSION_EXCEPTION,
	/**
	 * Upper level, contains internals and math, etc<br>
	 * It still can be kinda handled
	 */
	EXPRESSION_EXCEPTION,
	/**
	 * Highest level, when already throwing to chat
	 */
	CARPET_EXPRESSION_EXCEPTION;
	
	/**
	 * Gets the type of a true Carpet exception
	 * @param e The exception to check
	 * @return The type of the exception
	 */
	public static ExceptionTypes ofCarpetException(ActualScarpetException e) {
		if (e instanceof InternalExpressionException)
			return INTERNAL_EXPRESSION_EXCEPTION;
		else if (e instanceof ExpressionException)
			return EXPRESSION_EXCEPTION;
		else
			return CARPET_EXPRESSION_EXCEPTION;
	}
	
	/**
	 * Gets the parent type of an Exception type
	 * @return the parent exception type, or null if highest
	 */
	public ExceptionTypes getParentType() {
		switch(this) {
			case INTERNAL_EXPRESSION_EXCEPTION:
				return EXPRESSION_EXCEPTION;
			case EXPRESSION_EXCEPTION:
				return CARPET_EXPRESSION_EXCEPTION;
			default: // CARPET_EXPRESSION_EXCEPTION
				return null;
		}
	}
}
