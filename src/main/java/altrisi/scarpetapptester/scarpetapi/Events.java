package altrisi.scarpetapptester.scarpetapi;


import java.util.Arrays;
import java.util.function.Supplier;
import carpet.CarpetServer;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import static carpet.script.CarpetEventServer.Event;

public class Events {
	private static final Supplier<ServerCommandSource> SOURCE_SUPPLIER = () -> CarpetServer.minecraft_server.getCommandSource().
            withWorld(CarpetServer.minecraft_server.getWorld(World.OVERWORLD));

	// TODO Change to proper classes instead of String appName, String test
	public static Event PRE_TEST_START = new Event("pre_test_start", 2, false) {
		public void dispatch(String appName, String test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(appName), new StringValue(test));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static Event TEST_START = new Event("test_start", 2, false) {
		public void dispatch(String appName, String test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(appName), new StringValue(test));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static Event TEST_FINISHED = new Event("test_finished", 3, false) {
		public void dispatch(String appName, String test, boolean successfully) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(appName), new StringValue(test), new NumericValue(successfully));
			}, SOURCE_SUPPLIER);
		}
	};
	
}
