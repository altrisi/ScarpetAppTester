package altrisi.scarpetapptester.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.fakes.ActualScarpetException;
import carpet.script.exception.CarpetExpressionException;
import carpet.script.value.FunctionValue;

@Mixin(value = CarpetExpressionException.class, remap = false)
public abstract class CarpetExpressionExceptionMixin implements ActualScarpetException {
	
	@Inject(method = "<init>(Ljava/lang/String;Ljava/util/List;)V", at = @At("RETURN"))
	private void logException(String message, List<FunctionValue> stack, CallbackInfo ci) {
		ScarpetException exception = new ScarpetException(message, stack, this);
		ScarpetAppTester.getExceptionStorage().add(exception);
	}
}
