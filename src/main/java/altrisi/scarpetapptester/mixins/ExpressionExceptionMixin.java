package altrisi.scarpetapptester.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.fakes.ActualScarpetException;
import carpet.script.Context;
import carpet.script.Expression;
import carpet.script.Tokenizer;
import carpet.script.exception.ExpressionException;
import carpet.script.value.FunctionValue;

@Mixin(value = ExpressionException.class, remap = false)
public abstract class ExpressionExceptionMixin implements ActualScarpetException {
	
	@Inject(method = "<init>(Lcarpet/script/Context;Lcarpet/script/Expression;Lcarpet/script/Tokenizer$Token;Ljava/lang/String;Ljava/util/List;)V",
			at = @At("RETURN"))
	private void logException(Context c, Expression e, Tokenizer.Token t, String message, List<FunctionValue> stack, CallbackInfo ci){
		ScarpetException exception = new ScarpetException(c, e, t, message, stack, this);
		ScarpetAppTester.getExceptionStorage().add(exception);
	}
}
