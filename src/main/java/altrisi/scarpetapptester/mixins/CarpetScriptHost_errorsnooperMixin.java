package altrisi.scarpetapptester.mixins;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import altrisi.scarpetapptester.AppTester;
import carpet.script.CarpetScriptHost;
import carpet.script.Expression;
import carpet.script.Fluff;
import carpet.script.ScriptHost;
import carpet.script.Tokenizer;
import carpet.script.bundled.Module;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(CarpetScriptHost.class)
public abstract class CarpetScriptHost_errorsnooperMixin extends ScriptHost {
	protected CarpetScriptHost_errorsnooperMixin(Module code, boolean perUser, ScriptHost parent) {
		super(null, false, null);
		throw new IllegalAccessError("Untransformed Mixin!");
	}

	@Shadow(remap = false) ServerCommandSource responsibleSource;
	
	@Inject(method = "setChatErrorSnooper", at = @At("TAIL"))
	private void injectOurSnooper(ServerCommandSource source, CallbackInfo ci) {
		setOurSnooper(errorSnooper);
	}
	
	@Overwrite(remap = false)
	@Override
	public void resetErrorSnooper() {
		responsibleSource = null;
		setOurSnooper(null);
	}
	
	private void setOurSnooper(Fluff.TriFunction<Expression, Tokenizer.Token, String, List<String>> prev) {
		errorSnooper = (expr, token, str) -> {
			AppTester.INSTANCE.registerException(expr, token, str);
			return prev != null ? prev.apply(expr, token, str) : Collections.emptyList();
		};
	}
}
