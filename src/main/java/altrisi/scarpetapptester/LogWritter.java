package altrisi.scarpetapptester;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import altrisi.scarpetapptester.exceptionhandling.ExceptionStorage;
import altrisi.scarpetapptester.exceptionhandling.ScarpetException;
import net.fabricmc.loader.api.FabricLoader;

import static altrisi.scarpetapptester.ScarpetAppTester.LOGGER;; 

/**
 * Note: This is very much unfinished.
 * It will probably even be completely redone
 * @author altrisi
 *
 */
public class LogWritter {
	private BufferedWriter logWritter;
    LogWritter() {
    	String now = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    	Path logPath = FabricLoader.getInstance().getGameDir()
    					.resolve("results").resolve("result-"+now+".txt").toAbsolutePath();
    	try {
    		Files.createDirectories(logPath.getParent());
			Files.createFile(logPath);
			logWritter = Files.newBufferedWriter(logPath);
			LOGGER.info("Starting testing session at "+now);
		} catch (IOException e) {
			LOGGER.fatal("Couldn't initialize log file, exiting...", e);
			System.exit(-1);
		}
    }
    
    public void writeException(ScarpetException e) {
    	try {
			logWritter.write(e.getMessage()+'\n');
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    public void close(ExceptionStorage storage) {
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
