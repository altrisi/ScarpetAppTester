package altrisi.scarpetapptester.exceptionhandling;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Sorry. I know this is bad. But I suppose it'll work
 * @author altrisi
 */
@SuppressWarnings("serial")
public class ExceptionStorage extends ArrayList<ScarpetException> {
	@Override
	public boolean add(ScarpetException e) {
		return super.add(processException(e));
	}
	
	/**
	 * The same as calling {@link #get(int)} with {@link #size()} {@code - 1}.
	 * @return The last registered exception
	 */
	public ScarpetException getLast() {
		return get(size() - 1);
	}
	
	@Override
	public void add(int index, ScarpetException element) {
		throw new UnsupportedOperationException("(at least currently) ExceptionStorage doesn't support adding exceptions between other ones");
	}
	
	@Override
	public boolean addAll(Collection<? extends ScarpetException> c) {
		c.forEach(this::add);
		return true;
	}
	
	private ScarpetException processException(ScarpetException e) {
		if (size() == 0) return e;
		ScarpetException last = getLast();
		
		if (last.getType().getParentType() == e.getType()) {
			if (e.getType() == ExceptionTypes.EXPRESSION_EXCEPTION) {
				if (e.getMessage() == last.getMessage()) {
					last.setRethrown(e);
				}
			} else if (e.getType() == ExceptionTypes.CARPET_EXPRESSION_EXCEPTION) {
				if (e.getChatMessage() == last.getChatMessage()) {
					last.setRethrown(e);
					// CarpetExpressionExceptions have almost no context
					// Let's fill it with our child exception's details
					return last.copyAs(e);
				}
			}
		}
		return e;
	}
}
