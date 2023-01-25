package org.unibl.etf.diamondcircle.util;

import java.time.LocalTime;
import java.time.format.*;
import java.time.temporal.*;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.util.*;

public class Stopwatch {

	private static final String TIME_START = "00:00:00";
	private static final String TIME_PATTERN = "HH:mm:ss";
	private static final int RESOLUTION = 1000;
	private static final ChronoUnit UNIT = ChronoUnit.MILLIS;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
	private LocalTime elapsedTime = LocalTime.parse(TIME_START);
	private StringProperty elapsedTimeProperty = new SimpleStringProperty(TIME_START);
	private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(RESOLUTION), e -> incrementTime()));

	public Stopwatch() {
		timeline.setCycleCount(Animation.INDEFINITE);
	}

	public void start() {
		timeline.play();
	}

	public void pause() {
		timeline.pause();
	}

	public void stop() {
		timeline.stop();
		elapsedTime = LocalTime.parse(TIME_START);
		elapsedTimeProperty.set(elapsedTime.format(formatter));
	}

	public StringProperty getElapsedTimeProperty() {
		return elapsedTimeProperty;
	}

	private void incrementTime() {
		elapsedTime = elapsedTime.plus(RESOLUTION, UNIT);
		elapsedTimeProperty.set(elapsedTime.format(formatter));
	}
}
