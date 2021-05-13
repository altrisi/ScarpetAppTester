package altrisi.scarpetapptester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import net.fabricmc.loader.api.FabricLoader;
 

/**
 * Note: This is very much unfinished.
 * It will probably even be completely redone
 * @author altrisi
 *
 */
@Deprecated
class LogWritter {
	private BufferedWriter logWritter;
    LogWritter() {
    	String now = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    	Path logPath = FabricLoader.getInstance().getGameDir()
    					.resolve("results").resolve("result-" + now + ".txt").toAbsolutePath();
    	try {
    		Files.createDirectories(logPath.getParent());
			Files.createFile(logPath);
			logWritter = Files.newBufferedWriter(logPath);
			AppTester.LOGGER.info("Starting testing session at " + now);
			logWritter.write("Starting testing session at " + now);
			logWritter.newLine();
		} catch (IOException e) {
			AppTester.LOGGER.fatal("Couldn't initialize log file, exiting...", e);
			System.exit(-1);
		}
    }
    
    public void writeException(ScarpetException e) {
    	try {
    		logWritter.write(e.time());
    		logWritter.write(" -> ");
			logWritter.write(e.message());
			logWritter.newLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    public void close() {
    	List<ScarpetException> storage = AppTester.INSTANCE.getExceptionStorage();
    	try {
    		for (ScarpetException e: storage) {
    			writeException(e);
    		}
			logWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
