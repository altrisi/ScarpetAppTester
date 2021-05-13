package altrisi.scarpetapptester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import altrisi.scarpetapptester.testing.apps.App;
import altrisi.scarpetapptester.testing.apps.ScarpetApp;
import carpet.CarpetServer;
import carpet.script.Expression;
import carpet.script.exception.ExpressionException;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public enum AppTester implements Runnable { INSTANCE;
	@Deprecated
    private final List<ScarpetException> exceptionStorage = new ArrayList<ScarpetException>();
	private App currentApp = null;
	public static Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
	private final List<App> appQueue = new ArrayList<>();
	public final CountDownLatch serverLoadedWorlds = new CountDownLatch(1);

	@Override
	public void run() {
		LOGGER.info("App Tester thread started!");
		//prepareConfigs etc
		try { serverLoadedWorlds.await(); }
		catch (InterruptedException e) { throw crashThread(e); }
		LOGGER.info("Received serverLoadedWorlds confirmation, starting!");
		currentApp = new ScarpetApp("testapp");
		currentApp.load();
		currentApp.runTests();
		currentApp.unload();
		try {
			Thread.sleep(6000);
			LOGGER.info("Yeah, that did nothing...");
		} catch (InterruptedException e) {
			throw crashThread(e);
		}
		ThreadingUtils.runInMainThreadAndWait(() -> {
			CarpetServer.minecraft_server.stop(false);
			ThreadingUtils.getStepLatch().countDown();
		});
	}

	static CrashException crashThread(Throwable e) {
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
	 * @return The current instance of {@link App} being tested
	 */
	public App currentApp() {
		return currentApp;
	}
	
	/**
	 * @return an immutable copy of the current exception storage
	 */
	public List<ScarpetException> getExceptionStorage() {
		return Collections.unmodifiableList(exceptionStorage);
	}
	
	/**
	 * Registers an exception into the current storage and assigns information to it
	 * @return That exception, not sure why
	 */
	public ScarpetException registerException(Expression expr, String msg, ExpressionException e) {
		var exception = new ScarpetException(expr, msg, e);
		exceptionStorage.add(exception);
		LOGGER.info("HELLO I FOUND A BUG");
		currentApp().currentTest().attachException(exception);
		return exception;
	}
	
}
