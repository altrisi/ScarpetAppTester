package altrisi.scarpetapptester.scarpetapi;

import java.util.Arrays;
import java.util.function.Supplier;

import altrisi.scarpetapptester.testing.apps.App;
import altrisi.scarpetapptester.testing.tests.Test;
import carpet.CarpetServer;
import carpet.script.value.BooleanValue;
import carpet.script.value.StringValue;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import static carpet.script.CarpetEventServer.Event;

/**
 * NOT PUBLIC API
 * INTERNAL USE ONLY
 */
public abstract class ScarpetEvents extends Event {
	private static final Supplier<ServerCommandSource> SOURCE_SUPPLIER = () -> CarpetServer.minecraft_server.getCommandSource().
            withWorld(CarpetServer.minecraft_server.getWorld(World.OVERWORLD));

	public ScarpetEvents(String name, int reqArgs, boolean globalOnly) {
		super(name, reqArgs, globalOnly);
	}
	
	public static final ScarpetEvents PREPARING_TESTS = new ScarpetEvents("preparing_tests", 2, false) {
		public void dispatch(App app) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(app.name()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents PRE_TEST_STARTED = new ScarpetEvents("pre_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().name()), new TestValue(test));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents RIGHT_BEFORE_TEST_STARTED = new ScarpetEvents("right_before_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().name()), new TestValue(test));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents RIGHT_AFTER_TEST_STARTED = new ScarpetEvents("right_after_test_started", 2, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().name()), new TestValue(test));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public static final ScarpetEvents TEST_FINISHED = new ScarpetEvents("test_finished", 3, false) {
		public void dispatch(Test test) {
			this.handler.call(() -> {
				return Arrays.asList(new StringValue(test.getApp().name()), new TestValue(test), BooleanValue.of(test.successfulSoFar()));
			}, SOURCE_SUPPLIER);
		}
	};
	
	public void dispatch(Test test) {};
	public void dispatch(App app) {};
}
