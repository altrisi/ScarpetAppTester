package altrisi.scarpetapptester;

import java.util.concurrent.SynchronousQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import altrisi.scarpetapptester.exceptionhandling.ExceptionStorage;
import altrisi.scarpetapptester.scarpetapi.ScarpetAPIFunctions;
import altrisi.scarpetapptester.tests.TestUtils;
import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.helpers.TickSpeed;
import carpet.script.CarpetExpression;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;

public class ScarpetAppTester implements CarpetExtension, ModInitializer
{
    //private static SettingsManager settingsManager;
    public static Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
    private static ExceptionStorage exceptionStorage;
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
    }
    
    @Override
    public void onTick(MinecraftServer server) {
    	if (CarpetServer.scriptServer.events.scheduledCalls.isEmpty()) {
			TestUtils.getSchedulesLatch().countDown();
		}
    	TestUtils.getStepLatch().countDown(); // On purpose, 1 tick later
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
