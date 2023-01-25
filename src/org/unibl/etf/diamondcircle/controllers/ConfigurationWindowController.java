package org.unibl.etf.diamondcircle.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.unibl.etf.diamondcircle.*;
import org.unibl.etf.diamondcircle.model.game.*;
import org.unibl.etf.diamondcircle.view.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.*;

public class ConfigurationWindowController extends BaseController implements Initializable {

	public ConfigurationWindowController(ViewFactory viewFactory, String fxmlName) {
		super(viewFactory, fxmlName);
	}

	private static final String ALERT_MESSAGE = "Morate odabrati dimenzije matrice i broj igraca";

	@FXML
	private Button okButton;

	@FXML
	private RadioButton radioButton2Players;

	@FXML
	private RadioButton radioButton3Players;

	@FXML
	private RadioButton radioButton4Players;

	@FXML
	private RadioButton radioButton7x7;

	@FXML
	private RadioButton radioButton8x8;

	@FXML
	private RadioButton radioButton9x9;

	@FXML
	private RadioButton radioButton10x10;

	private final ToggleGroup players = new ToggleGroup();
	private final ToggleGroup dimensions = new ToggleGroup();

	@FXML
	void okButtonOnAction() {

		RadioButton selectedPlayers = (RadioButton) players.getSelectedToggle();
		RadioButton selectedDimensions = (RadioButton) dimensions.getSelectedToggle();

		if (selectedPlayers == null || selectedDimensions == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(ALERT_MESSAGE);
			alert.showAndWait();
		} else {
			int numberOfPlayers;
			int matrixDimensions;
			switch (selectedPlayers.getText()) {
				case "2":
					numberOfPlayers = 2;
					break;
				case "3":
					numberOfPlayers = 3;
					break;
				case "4":
					numberOfPlayers = 4;
					break;
				default:
					numberOfPlayers = 2;
					break;
			}

			switch (selectedDimensions.getText()) {
				case "7x7":
					matrixDimensions = 7;
					break;
				case "8x8":
					matrixDimensions = 8;
					break;
				case "9x9":
					matrixDimensions = 9;
					break;
				case "10x10":
					matrixDimensions = 10;
					break;
				default:
					matrixDimensions = 7;
					break;
			}
			GameManager.getInstance().setCurrentGame(new Game(numberOfPlayers, matrixDimensions));
			viewFactory.closeStage((Stage) okButton.getScene().getWindow());
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		radioButton2Players.setToggleGroup(players);
		radioButton3Players.setToggleGroup(players);
		radioButton4Players.setToggleGroup(players);
		radioButton7x7.setToggleGroup(dimensions);
		radioButton8x8.setToggleGroup(dimensions);
		radioButton9x9.setToggleGroup(dimensions);
		radioButton10x10.setToggleGroup(dimensions);
	}
}