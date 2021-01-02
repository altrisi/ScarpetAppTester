package altrisi.scarpetapptester;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import altrisi.scarpetapptester.exceptionhandling.ExceptionStorage;
import altrisi.scarpetapptester.scarpetapi.ScarpetAPIFunctions;
import altrisi.scarpetapptester.tests.TestUtils;
import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.helpers.TickSpeed;
import carpet.script.CarpetEventServer;
import carpet.script.CarpetExpression;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class ScarpetAppTester implements CarpetExtension, ModInitializer
{
    //private static SettingsManager settingsManager;
    public static Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
    private static ExceptionStorage exceptionStorage;
    public static LogWritter writter;
    private static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();

    @Override
    public void onInitialize()
    {
    	//settingsManager = new SettingsManager("1.0", "scarpetapptester", "Scarpet App Tester");
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onGameStarted()
    {
        //settingsManager.parseSettingsClass(ScarpetAppTesterSettings.class);
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
    	if (TestUtils.waitingForSchedules && CarpetServer.scriptServer.events.scheduledCalls.isEmpty()) {
			synchronized (TestUtils.scheduleLock) {
				TestUtils.scheduleLock.notify();
				TestUtils.waitingForSchedules = false;
			}
		}
    	if (TestUtils.waitingForRunnable) {
    		synchronized (TestUtils.stepLock) {
				TestUtils.stepLock.notify();
				TestUtils.waitingForRunnable = false;
			}
    	}
    	while(!taskQueue.isEmpty()){
    		try {
				taskQueue.take().run();
			} catch (InterruptedException ignored) {}
    	}
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
    	writter.close(getExceptionStorage());
    }
    @Override public String version() { return "scarpet-app-tester"; }
    //@Override public SettingsManager customSettingsManager() { return settingsManager; }
    
    @Override
    public void scarpetApi(CarpetExpression expression) {
    	ScarpetAPIFunctions.apply(expression);
    }

	/**
	 * @return the exceptionStorage
	 */
	public static ExceptionStorage getExceptionStorage() {
		return exceptionStorage;
	}
	
	/**
	 * 
	 * @return the taskQueue
	 */
	public static BlockingQueue<Runnable> getTaskQueue() {
		return taskQueue;
	}
}
