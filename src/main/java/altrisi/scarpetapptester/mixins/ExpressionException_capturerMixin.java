package altrisi.scarpetapptester.mixins;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import altrisi.scarpetapptester.AppTester;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.ExpressionException;
import carpet.script.value.FunctionValue;

@Mixin(ExpressionException.class)
public class ExpressionException_capturerMixin {
	private ScarpetException storedException;
	@Shadow(remap = false) private Supplier<String> lazyMessage;
	
	@Inject(
		method = "<init>(Lcarpet/script/Context;Lcarpet/script/Expression;Lcarpet/script/Tokenizer$Token;Ljava/lang/String;Ljava/util/List;)V",
		at = @At("RETURN"),
		remap = false
	)
	private void startExc(Context c, Expression e, Tokenizer.Token t, String message, List<FunctionValue> stack, CallbackInfo ci) {
		this.storedException = AppTester.INSTANCE.registerException(e, message, (ExpressionException)(Object)this);
	}
	
	@Inject(method = "getMessage", at = @At("RETURN"), remap = false)
	private void setUnhandled(CallbackInfoReturnable<String> cir) {
		if (storedException == null)
		{
			AppTester.LOGGER.error("Unknown exception got out of the stack", new RuntimeException("dummy"));
			storedException = AppTester.INSTANCE.registerException(hackilyGetExpression(), cir.getReturnValue(), (ExpressionException)(Object)this);
		}
		storedException.setUnhandled();
	}
	
	private Expression hackilyGetExpression() {
		RuntimeException e = new RuntimeException();
		try {
			for (Field f : lazyMessage.getClass().getDeclaredFields())
				if (f.getType() == Expression.class) {
					f.setAccessible(true);
					return (Expression)f.get(lazyMessage);
				}
		} catch (ReflectiveOperationException exc) {
			e.initCause(exc);
		}
		AppTester.LOGGER.error("Failed to hackily get Expression of unknown exception", e);
		return null;
	}
}
