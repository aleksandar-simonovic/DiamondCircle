package org.unibl.etf.diamondcircle.util;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class AppProperties {

	private static final String LOG_MESSAGE = "Unable to read property file!";

	private static String PROPERTIES_FILE_NAME = "resources/config.properties";

	private static AppProperties instance = null;

	private final Properties properties = new Properties();

	private AppProperties() {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
			properties.load(inputStream);
		} catch (IOException e) {
			SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
		}
	}

	public static AppProperties getInstance() {
		if (instance == null) {
			instance = new AppProperties();
		}
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public Set<String> getAllPropertyNames() {
		return properties.stringPropertyNames();
	}
}