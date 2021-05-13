package altrisi.scarpetapptester.mixins;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
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
abstract class ExpressionException_capturerMixin {
	private ScarpetException storedException;
	
	@Inject(
		method = "<init>(Lcarpet/script/Context;Lcarpet/script/Expression;Lcarpet/script/Tokenizer$Token;Ljava/lang/String;Ljava/util/List;)V",
		at = @At("RETURN"),
		remap = false
	) // Not catchable 
	private void startExc(Context c, Expression e, Tokenizer.Token t, String message, List<FunctionValue> stack, CallbackInfo ci) {
		this.storedException = AppTester.INSTANCE.registerException(e, message, (ExpressionException)(Object)this);
	}
	
	@Inject(
			method = "<init>(Lcarpet/script/Context;Lcarpet/script/Expression;Lcarpet/script/Tokenizer$Token;Ljava/util/function/Supplier;Ljava/util/List;)V",
			at = @At("RETURN"),
			remap = false
	) // Catchable
	private void startExc(Context c, Expression e, Tokenizer.Token t, Supplier<String> message, List<FunctionValue> stack, CallbackInfo ci) {
		this.storedException = AppTester.INSTANCE.registerException(e, message.get(), (ExpressionException)(Object)this); //TODO Change way message is gotten?
	}
	
	@Inject(method = "getMessage", at = @At("RETURN"), remap = false)
	private void setUnhandled(CallbackInfoReturnable<String> ci) {
		storedException.setUnhandled();
	}
}
