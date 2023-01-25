package org.unibl.etf.diamondcircle.controllers;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;
import org.unibl.etf.diamondcircle.util.*;
import org.unibl.etf.diamondcircle.view.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.*;

public class ResultsWindowController extends BaseController implements Initializable {

	private static final String LOG_MESSAGE = "I/O error occurred while reading from the file!";
	private static final String ALERT_MESSAGE = "You have to select item!";
	
	public ResultsWindowController(ViewFactory viewFactory, String fxmlName) {
		super(viewFactory, fxmlName);
	}

	private static final String GAME_RESULTS_DIRECTORY_NAME = "results";

	@FXML
	private ListView<String> resultsListView;

	@FXML
	private TextArea resultsTextArea;

	@FXML
	void backOnAction() {
		viewFactory.closeStage((Stage) resultsListView.getScene().getWindow());
	}

	@FXML
	void showContentOnAction() {
		String selection = resultsListView.getSelectionModel().getSelectedItem();
		if (selection == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(ALERT_MESSAGE);
			alert.show();
		} else {
			Path path = Paths.get(GAME_RESULTS_DIRECTORY_NAME, resultsListView.getSelectionModel().getSelectedItem());
			try {
				List<String> lines = Files.readAllLines(path);
				resultsTextArea.setText(lines.stream().reduce((t, u) -> (t + "\n" + u)).orElseGet(() -> ""));
			} catch (IOException e) {
				SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultsListView.setItems(FXCollections.observableArrayList(listFiles(GAME_RESULTS_DIRECTORY_NAME)));
	}

	private List<String> listFiles(String dir) {
		return Stream.of(new File(dir).listFiles())
				.filter(file -> !file.isDirectory())
				.map(File::getName)
				.collect(Collectors.toList());
	}
}
