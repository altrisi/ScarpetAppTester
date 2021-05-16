package altrisi.scarpetapptester.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import altrisi.scarpetapptester.AppTester;
import carpet.script.ScriptHost;

@Mixin(ScriptHost.class)
public class ScriptHost_catchDeprecationsMixin {
	@Inject(method = "issueDeprecation(Ljava/lang/String;)V", at = @At("INVOKE"), remap = false)
	private void catchDeprecations(String feature, CallbackInfoReturnable<Boolean> ci) {
		// TODO me
		AppTester.LOGGER.warn("App is using deprecated feature: " + feature);
	}
}
