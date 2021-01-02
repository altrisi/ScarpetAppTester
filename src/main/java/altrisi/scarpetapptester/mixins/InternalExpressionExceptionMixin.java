package altrisi.scarpetapptester.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import altrisi.scarpetapptester.ScarpetAppTester;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.fakes.ActualScarpetException;
import carpet.script.exception.InternalExpressionException;

@Mixin(value = InternalExpressionException.class, remap = false)
public abstract class InternalExpressionExceptionMixin implements ActualScarpetException {
	@Inject(method = "<init>(Ljava/lang/String;)V", at = @At("RETURN"), remap = false)
	private void getExpression(String message, CallbackInfo ci) {
		ScarpetException exception = new ScarpetException(message, this);
		ScarpetAppTester.getExceptionStorage().add(exception);
	}
}
