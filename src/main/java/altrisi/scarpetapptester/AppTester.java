package altrisi.scarpetapptester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;

import altrisi.scarpetapptester.config.AppConfig;
import altrisi.scarpetapptester.config.AppTesterConfig;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.testing.apps.App;
import altrisi.scarpetapptester.testing.apps.AppStatus;
import carpet.CarpetServer;
import carpet.script.Expression;
import carpet.script.exception.ExpressionException;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public enum AppTester implements Runnable { INSTANCE;
	private App currentApp = null;
	public static final Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
	public static final Logger RESULT_LOGGER = LogManager.getLogger("Scarpet App Tester | Results");
	private final List<App> appQueue = new ArrayList<>();
	public final CountDownLatch serverLoadedWorlds = new CountDownLatch(1);
	private AppTesterConfig config;

	@Override
	public void run() {
		initializeLogger();
		initializeConfig();
		RESULT_LOGGER.info("************************* Starting testing session at " + new Date() + " *************************");
		//prepareConfigs etc
		try { serverLoadedWorlds.await(); }
		catch (InterruptedException e) { throw crashThread(e); }
		LOGGER.info("Received serverLoadedWorlds confirmation, starting!");
		
		for (App app : appQueue) {
			currentApp = app;
			currentApp.load();
			if (currentApp.currentStatus() != AppStatus.CRITICAL_FAILURE) {
				currentApp.prepareTests();
				currentApp.runTests();
			}
			currentApp.unload();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw crashThread(e);
		}
		RESULT_LOGGER.info("************************* Finished testing session at " + new Date() + " *************************");
		
		CarpetServer.minecraft_server.submit(() -> CarpetServer.minecraft_server.stop(false));
	}

	static CrashException crashThread(Throwable e) {
		if (e instanceof InterruptedException && CarpetServer.minecraft_server == null)
			throw new CompletionException("Exception in main thread", e); //TODO Well, it crashed. DO SOMETHING DUDDEEE
		LOGGER.fatal(e.getMessage() != null ? e.getMessage().toUpperCase() : "THREAD CRASHED", e);
		CrashReport crash = CrashReport.create(e, "Something crashed the Scarpet App Tester thread");
		CrashReportSection ourSection = crash.addElement("Scarpet App Tester");
		App app = INSTANCE.currentApp();
		try {
			ourSection.add("Current app being tested: ", app.name());
			ourSection.add("App status: ", app.currentStatus().name());
			ourSection.add("Current test: ", app.currentTest().getName());
			ourSection.add("Current test stage", app.currentTest().getTestStage().name());
		} catch (Throwable e2) { }
		return new CrashException(crash); // Kill the thread
	}
	
	/**
	 * @return The current {@link App} instance being tested
	 */
	public App currentApp() {
		return currentApp;
	}
	
	/**
	 * Registers an exception into the current test and assigns information to it
	 * @return The resulting {@link ScarpetException}, in order to be saved in the mixin
	 */
	public ScarpetException registerException(Expression expr, String msg, ExpressionException e) {
		var exception = new ScarpetException(expr, msg, e);
		LOGGER.info("HELLO I FOUND A BUG");
		if (currentApp().currentStatus() == AppStatus.RUNNING_TESTS || currentApp().currentStatus() == AppStatus.LOADING) {
			currentApp().currentTest().attachException(exception);
		} else {
			LOGGER.info("Captured exception outside of any running tests! May be a bug in a tester app!");
		}
		return exception;
	}
	
	private void initializeConfig() {
		// TODO Make this load an actual file
		this.config = AppConfig.GSON.fromJson("""
        		{
    			"apps": [
    				{
    					"name": "testapp",
    					"hi": "Yes"
    				},
    				{
    					"name": "App2",
    					"hi": "Nope"
    				}
    			]
    		}
    		""", AppTesterConfig.class);
		config.apps().forEach(config -> appQueue.add(config.createApp()));
	}
	
	private void initializeLogger() {
		// Add appender to result logger
		String now = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		Appender ap = FileAppender.newBuilder().withFileName("results/result-" + now + ".txt").withName("Scarpet App Tester Result").build();
		((org.apache.logging.log4j.core.Logger)RESULT_LOGGER).addAppender(ap);
		ap.start();
	}
	
}
