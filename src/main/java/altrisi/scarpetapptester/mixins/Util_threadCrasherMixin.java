package altrisi.scarpetapptester.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.Util;

@Mixin(Util.class)
public interface Util_threadCrasherMixin {
	@Invoker
	static void invokeMethod_18347(Thread t, Throwable e) {
		throw new IllegalAccessError("Untransformed method!");
	}
}
