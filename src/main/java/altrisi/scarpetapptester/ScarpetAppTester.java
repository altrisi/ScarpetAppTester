package altrisi.scarpetapptester;

import java.util.concurrent.SynchronousQueue;

import altrisi.scarpetapptester.mixins.Util_threadCrasherMixin;
import altrisi.scarpetapptester.scarpetapi.ScarpetAPI;
import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.CarpetSettings;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.crash.CrashException;

public class ScarpetAppTester implements CarpetExtension, ModInitializer
{
	private static Thread asyncThread;
    private static SynchronousQueue<Runnable> taskQueue = new SynchronousQueue<Runnable>();
    public static ServerCommandSource commandSource;

    @Override
    public void onInitialize()
    {
        CarpetServer.manageExtension(this);
        CarpetSettings.scriptsAutoload = false;
        asyncThread = new Thread(AppTester.INSTANCE);
        asyncThread.setName("Scarpet App Tester Thread");
        asyncThread.setUncaughtExceptionHandler((thread, exception) -> {
			if (!(exception instanceof CrashException)) {
				AppTester.LOGGER.fatal("Crashing with an unhandled exception", exception);
				exception = AppTester.crashThread(exception);
			}
			Util_threadCrasherMixin.invokeMethod_18347(thread, exception);
		});
        asyncThread.start();
    }

    @Override
    public void onGameStarted() {
    	ScarpetAPI.registerOurThings();
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
    	commandSource = server.getCommandSource();
        //TickSpeed.setFrozenState(true, true);
        AppTester.INSTANCE.serverLoadedWorlds.countDown();
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
    	
    }
    
    @Override public String version() { return "scarpet-app-tester"; }
    

	
	/**
	 * @return the taskQueue
	 */
	public static SynchronousQueue<Runnable> getTaskQueue() {
		return taskQueue;
	}
}
