package org.unibl.etf.diamondcircle;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.logging.*;
import org.unibl.etf.diamondcircle.model.game.*;
import org.unibl.etf.diamondcircle.model.players.*;
import org.unibl.etf.diamondcircle.util.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.scene.media.*;

public class GameManager {

	private static GameManager instance = null;

	private static final String BACKGROUND_MUSIC_FILE_PATH = "resources/audio/background-music.mp3";
	private static final String GAME_RESULTS_DIRECTORY_NAME = "results";
	private static final File GAME_RESULTS_DIRECTORY = new File(GAME_RESULTS_DIRECTORY_NAME);
	private static final String RESULTS_FILE_NAME_TEMPLATE = "IGRA_*.txt";
	private static final String RESULTS_FILE_NAME_ID = "*";
	private static final String TIME_PATTERN = "dd-MM-yyyy-HH-mm-ss";
	private static final String LOG_MESSAGE = "I/O error occurred while trying to open results file!";
	private static final String SAVE_GAME_MESSAGE = "Ukupno vrijeme trajanja igre: ";

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

	public static final String POKRENI = "POKRENI";
	public static final String ZAUSTAVI = "ZAUSTAVI";

	private Stopwatch stopwatch = new Stopwatch();

	private DirectoryWatcher directoryWatcher;

	private MediaPlayer music = new MediaPlayer(
			new Media(getClass().getClassLoader().getResource(BACKGROUND_MUSIC_FILE_PATH).toString()));

	private StringProperty startStopProperty = new SimpleStringProperty(POKRENI);

	private BooleanProperty gameEndedProperty = new SimpleBooleanProperty(false);

	private Game currentGame = null;

	private GameManager() {
		if (!GAME_RESULTS_DIRECTORY.exists()) {
			GAME_RESULTS_DIRECTORY.mkdir();
		}
		directoryWatcher = new DirectoryWatcher(GAME_RESULTS_DIRECTORY);
		directoryWatcher.setDaemon(true);
		directoryWatcher.start();
		music.setCycleCount(MediaPlayer.INDEFINITE);
		music.play();
	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	public Stopwatch getStopwatch() {
		return stopwatch;
	}

	public DirectoryWatcher getDirectoryWatcher() {
		return directoryWatcher;
	}

	public StringProperty getStartStopProperty() {
		return startStopProperty;
	}

	public BooleanProperty getGameEndedProperty() {
		return gameEndedProperty;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public void newGame() {
		currentGame.newGame();
		stopwatch.start();
		Platform.runLater(() -> {
			startStopProperty.set(ZAUSTAVI);
			gameEndedProperty.set(false);
		});
	}

	public void unpause() {
		stopwatch.start();
		currentGame.unpause();
		Platform.runLater(() -> {
			startStopProperty.set(ZAUSTAVI);
		});
	}

	public void pause() {
		stopwatch.pause();
		currentGame.pause();
		Platform.runLater(() -> {
			startStopProperty.set(POKRENI);
		});
	}

	public void finish() {
		saveGameData();
		currentGame = null;
		Platform.runLater(() -> {
			stopwatch.stop();
			startStopProperty.set(POKRENI);
			gameEndedProperty.set(true);
		});
	}

	private void saveGameData() {
		File file = new File(GAME_RESULTS_DIRECTORY, RESULTS_FILE_NAME_TEMPLATE.replace(
				RESULTS_FILE_NAME_ID, LocalDateTime.now().format(formatter)));
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			for (Player player : currentGame.getPlayers()) {
				pw.println(player);
			}
			pw.print(SAVE_GAME_MESSAGE + stopwatch.getElapsedTimeProperty().getValue());
		} catch (IOException e) {
			SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
		}
	}
}
