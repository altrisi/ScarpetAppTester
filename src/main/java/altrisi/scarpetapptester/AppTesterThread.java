package altrisi.scarpetapptester;

import altrisi.scarpetapptester.testing.apps.App;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;

public enum AppTesterThread implements Runnable { INSTANCE;
	private App currentApp = null;

	@Override
	public void run() {
		//prepareConfigs etc
		try {
			Thread.currentThread().interrupt();
			Thread.sleep(6000);
			ScarpetAppTester.LOGGER.info("What");
		} catch (InterruptedException e) { interrupted(e); }
	}

	public static void interrupted(Throwable e) {
		ScarpetAppTester.LOGGER.fatal("INTERRUPTED", e);
		CrashReport crash = CrashReport.create(e, "Something interrupted the Scarpet App Tester thread");
		CrashReportSection ourSection = crash.addElement("Scarpet App Tester");
		App app = AppTesterThread.INSTANCE.getCurrentApp();
		try {
			ourSection.add("Current app being tested: ", app.getName());
			ourSection.add("App status: ", app.getCurrentStatus().name());
			ourSection.add("Current test: ", app.getCurrentTest().getName());
			ourSection.add("Current test stage", app.getCurrentTest().getTestStage().name());
		} catch (Throwable e2) { }
		throw new CrashException(crash);
	}
	
	/**
	 * @return The current instance of {@link App} being tested
	 */
	public App getCurrentApp() {
		return currentApp;
	}
}
