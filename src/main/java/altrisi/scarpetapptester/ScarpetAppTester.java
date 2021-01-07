package altrisi.scarpetapptester;

import java.util.concurrent.SynchronousQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import altrisi.scarpetapptester.exceptionhandling.ExceptionStorage;
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
    public static Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
    private static Thread asyncThread;
    private static ExceptionStorage exceptionStorage;
    //I'm going to regret this:
    private CrashException rethrowMe = null;
    public static LogWritter writter;
    private static SynchronousQueue<Runnable> taskQueue = new SynchronousQueue<Runnable>();

    @Override
    public void onInitialize()
    {
    	//settingsManager = new SettingsManager("1.0", "scarpetapptester", "Scarpet App Tester");
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
        TickSpeed.is_paused = true; // TODO Make this forwards-compatible
        writter = new LogWritter();
        exceptionStorage = new ExceptionStorage();
        asyncThread = new Thread(AppTesterThread.INSTANCE);
        asyncThread.setName("Scarpet App Tester Thread");
        asyncThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				if (e instanceof CrashException)
					rethrowMe = (CrashException)e;
				try{
					AppTesterThread.interrupted(e);
				}catch (CrashException e2) {
					rethrowMe = e2;
				}
			}
		});
        asyncThread.start();
    }
    
    @Override
    public void onTick(MinecraftServer server) {
    	if (rethrowMe != null)
    		throw rethrowMe;
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
    	writter.close(getExceptionStorage());
    }
    @Override public String version() { return "scarpet-app-tester"; }
    
    @Override
    public void scarpetApi(CarpetExpression expression) {
    	ScarpetAPIFunctions.apply(expression.getExpr());
    }

	/**
	 * @return the exceptionStorage
	 */
	public static ExceptionStorage getExceptionStorage() {
		return exceptionStorage;
	}
	
	/**
	 * @return the taskQueue
	 */
	public static SynchronousQueue<Runnable> getTaskQueue() {
		return taskQueue;
	}
}
