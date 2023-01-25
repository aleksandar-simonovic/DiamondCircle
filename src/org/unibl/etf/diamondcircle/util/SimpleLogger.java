package org.unibl.etf.diamondcircle.util;

import java.io.IOException;
import java.util.logging.*;

public class SimpleLogger {

	private static SimpleLogger instance = null;

	private static final String LOG_FILE_NAME = "diamond-circle.log";

	private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private SimpleLogger() {
		initializeLogger();
	}

	public static SimpleLogger getInstance() {
		if (instance == null) {
			instance = new SimpleLogger();
		}
		return instance;
	}

	public synchronized void log(Level level, String message, Exception exception) {
		logger.log(level, message, exception);
	}

	private void initializeLogger() {
		try {
			logger.addHandler(new FileHandler(LOG_FILE_NAME, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}