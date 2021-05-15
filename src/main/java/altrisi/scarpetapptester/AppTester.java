package altrisi.scarpetapptester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;

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
	private App currentApp = null;
	public static Logger LOGGER = LogManager.getLogger("Scarpet App Tester");
	public static Logger RESULT_LOGGER = LogManager.getLogger("Scarpet App Tester | Results");
	static {
		String now = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		Appender ap = FileAppender.newBuilder().withFileName("results/result-" + now + ".txt").withName("Scarpet App Tester Result").build();
		((org.apache.logging.log4j.core.Logger)RESULT_LOGGER).addAppender(ap);
		ap.start();
	}
	private final List<App> appQueue = new ArrayList<>();
	public final CountDownLatch serverLoadedWorlds = new CountDownLatch(1);

	@Override
	public void run() {
		LOGGER.info("App Tester thread started!");
		RESULT_LOGGER.info("Starting testing session at... ");
		//prepareConfigs etc
		try { serverLoadedWorlds.await(); }
		catch (InterruptedException e) { throw crashThread(e); }
		LOGGER.info("Received serverLoadedWorlds confirmation, starting!");
		currentApp = new ScarpetApp("testapp");
		currentApp.load();
		currentApp.runTests();
		currentApp.unload();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw crashThread(e);
		}
		RESULT_LOGGER.info("Finishing testing session...");
		try {
			ScarpetAppTester.getTaskQueue().put(()->CarpetServer.minecraft_server.stop(false));
		} catch (Throwable e) {
			throw crashThread(e);
		}
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
	 * Registers an exception into the current test and assigns information to it
	 * @return The resulting {@link ScarpetException}, in order to be saved in the mixin
	 */
	public ScarpetException registerException(Expression expr, String msg, ExpressionException e) {
		var exception = new ScarpetException(expr, msg, e);
		LOGGER.info("HELLO I FOUND A BUG");
		currentApp().currentTest().attachException(exception);
		return exception;
	}
	
}
