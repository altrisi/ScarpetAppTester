package altrisi.scarpetapptester;

import java.util.concurrent.SynchronousQueue;

import org.jetbrains.annotations.NotNull;

import altrisi.scarpetapptester.mixins.Util_threadCrasherMixin;
import altrisi.scarpetapptester.scarpetapi.ScarpetAPIFunctions;
import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.helpers.TickSpeed;
import carpet.script.CarpetExpression;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashException;

public class ScarpetAppTester implements CarpetExtension, ModInitializer
{
	@NotNull
    private static Thread asyncThread;
    public static LogWritter writter;
    private static SynchronousQueue<Runnable> taskQueue = new SynchronousQueue<Runnable>();

    @Override
    public void onInitialize()
    {
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is also handled by Carpet as an extension, since we claim own settings manager at customSettingsManager()
    	// You can remove the events you don't use
    }
    
    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        TickSpeed.setFrozenState(true, true); // TODO Make this backwards-compatible
        writter = new LogWritter();
        asyncThread = new Thread(AppTester.INSTANCE);
        asyncThread.setName("Scarpet App Tester Thread");
        asyncThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				if (!(e instanceof CrashException)) {
					AppTester.LOGGER.fatal("Crashing with an unhandled exception", e);
					e = AppTester.crashThread(e);
				}
				Util_threadCrasherMixin.invokeMethod_18347(t, e);
			}
		});
        asyncThread.start();
    }
    
    @Override
    public void onTick(MinecraftServer server) {
    	if (CarpetServer.scriptServer.events.scheduledCalls.isEmpty()) {
			ThreadingUtils.getSchedulesLatch().countDown();
		}
    	ThreadingUtils.getStepLatch().countDown(); // On purpose, 1 tick later
    	Runnable task = taskQueue.poll();
    	if (task != null) {
    		task.run();
    	}
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
    	writter.close();
    }
    @Override public String version() { return "scarpet-app-tester"; }
    
    @Override
    public void scarpetApi(CarpetExpression expression) {
    	ScarpetAPIFunctions.apply(expression.getExpr());
    }

	
	/**
	 * @return the taskQueue
	 */
	public static SynchronousQueue<Runnable> getTaskQueue() {
		return taskQueue;
	}
}
