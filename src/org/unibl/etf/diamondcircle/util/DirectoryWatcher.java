package org.unibl.etf.diamondcircle.util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.*;
import java.util.logging.*;
import javafx.application.*;
import javafx.beans.property.*;
import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher extends Thread {

	private static final String interruptedLogMessage = "Thread was interrupted while waiting!";
	private static final String ioLogMessage = "I/O exception of some kind occurred";

	private IntegerProperty numberOfGamesProperty;
	private WatchService watchService;
	private WatchKey watchKey;
	private Path directory;

	public DirectoryWatcher(File folder) {
		try {
			numberOfGamesProperty = new SimpleIntegerProperty();
			watchService = FileSystems.getDefault().newWatchService();
			directory = Paths.get(folder.getCanonicalPath());
			watchKey = directory.register(watchService, ENTRY_CREATE, ENTRY_DELETE);
			numberOfGamesProperty.set(folder.list().length);
		} catch (IOException e) {
			SimpleLogger.getInstance().log(Level.SEVERE, ioLogMessage, e);
		}
	}

	public IntegerProperty getNumberOfGamesProperty() {
		return numberOfGamesProperty;
	}

	@Override
	public void run() {
		while (true) {
			try {
				watchKey = watchService.take();
				for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
					Kind<?> kind = watchEvent.kind();
					if (ENTRY_CREATE == kind) {
						Platform.runLater(() -> {
							numberOfGamesProperty.set(numberOfGamesProperty.get() + 1);
						});
					} else if (ENTRY_DELETE == kind) {
						Platform.runLater(() -> {
							numberOfGamesProperty.set(numberOfGamesProperty.get() - 1);
						});
					}
				}
				watchKey.reset();
			} catch (InterruptedException e) {
				SimpleLogger.getInstance().log(Level.SEVERE, interruptedLogMessage, e);
			}
		}
	}
}