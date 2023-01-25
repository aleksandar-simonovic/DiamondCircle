package org.unibl.etf.diamondcircle.view;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.*;
import java.io.*;
import java.util.logging.*;
import org.unibl.etf.diamondcircle.controllers.*;
import org.unibl.etf.diamondcircle.model.figures.*;
import org.unibl.etf.diamondcircle.util.*;

public class ViewFactory {

	private static final String GAME_WINDOW_FXML = "GameWindow.fxml";
	private static final String WELCOME_WINDOW_FXML = "WelcomeWindow.fxml";
	private static final String RESULTS_WINDOW_FXML = "ResultsWindow.fxml";
	private static final String CONFIGURATION_WINDOW_FXML = "ConfigurationWindow.fxml";
	private static final String FIGURE_MOVEMENT_WINDOW_FXML = "FigureMovementWindow.fxml";
	private static final String APPLICATION_ICON = "/resources/img/icons/icon.png";
	private static final String APPLICATION_TITLE = "Programski jezici 2 - Diamond Circle";
	private static final String LOG_MESSAGE = "I/O error occurred while loading FXML file!";

	public void showWelcomeWindow() {
		BaseController controller = new WelcomeWindowController(this, WELCOME_WINDOW_FXML);
		initializeStage(controller, Modality.NONE, StageStyle.DECORATED, false);
	}

	public void showGameWindow() {
		BaseController controller = new GameWindowController(this, GAME_WINDOW_FXML);
		initializeStage(controller, Modality.NONE, StageStyle.DECORATED, false);
	}

	public void showConfigurationWindow() {
		BaseController controller = new ConfigurationWindowController(this, CONFIGURATION_WINDOW_FXML);
		initializeStage(controller, Modality.APPLICATION_MODAL, StageStyle.UNDECORATED, true);
	}

	public void showResultsWindow() {
		BaseController controller = new ResultsWindowController(this, RESULTS_WINDOW_FXML);
		initializeStage(controller, Modality.APPLICATION_MODAL, StageStyle.DECORATED, false);
	}

	public void showFigureMovementWindow(Figure selectedFigure) {
		BaseController controller = new FigureMovementController(this, selectedFigure, FIGURE_MOVEMENT_WINDOW_FXML);
		initializeStage(controller, Modality.NONE, StageStyle.DECORATED, false);
	}

	private void initializeStage(BaseController controller, Modality modality, StageStyle stageStyle, boolean wait) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
		fxmlLoader.setController(controller);
		Parent parent = null;
		try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
		}
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.getIcons().add(new Image(APPLICATION_ICON));
		stage.setTitle(APPLICATION_TITLE);
		stage.initModality(modality);
		stage.initStyle(stageStyle);
		stage.setResizable(false);
		stage.setScene(scene);
		if (wait) {
			stage.showAndWait();
		} else {
			stage.show();
		}
	}

	public void closeStage(Stage stageToClose) {
		stageToClose.close();
	}
}