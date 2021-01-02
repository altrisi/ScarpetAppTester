package altrisi.scarpetapptester.scarpetapi;

import java.util.Arrays;
import java.util.function.Supplier;

import altrisi.scarpetapptester.tests.Test;
import carpet.CarpetServer;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import static carpet.script.CarpetEventServer.Event;

public abstract class ScarpetEvents extends Event {
	private static final Supplier<ServerCommandSource> SOURCE_SUPPLIER = () -> CarpetServer.minecraft_server.getCommandSource().
            withWorld(CarpetServer.minecraft_server.getWorld(World.OVERWORLD));

	public ScarpetEvents(String name, int reqArgs, boolean globalOnly) {
		super(name, reqArgs, globalOnly);
	}
	
	public static final ScarpetEvents PRE_TEST_STARTED = new ScarpetEvents("pre_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().getName()), new StringValue(test.getTestName()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents RIGHT_BEFORE_TEST_STARTED = new ScarpetEvents("right_before_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().getName()), new StringValue(test.getTestName()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents RIGHT_AFTER_TEST_STARTED = new ScarpetEvents("right_after_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().getName()), new StringValue(test.getTestName()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents TEST_FINISHED = new ScarpetEvents("test_finished", 3, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().getName()), new StringValue(test.getTestName()), new NumericValue(test.successfulSoFar()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	/**
	 * Dispatch the selected event
	 * @param test The test it is included in
	 */
	public abstract void dispatch(Test test);
}
