package org.unibl.etf.diamondcircle.controllers;

import org.unibl.etf.diamondcircle.*;
import org.unibl.etf.diamondcircle.model.cards.*;
import org.unibl.etf.diamondcircle.model.figures.*;
import org.unibl.etf.diamondcircle.model.players.*;
import org.unibl.etf.diamondcircle.view.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import java.net.*;
import java.util.*;

public class GameWindowController extends BaseController implements Initializable {

	@FXML
	private ListView<Figure> figureListView;

	@FXML
	private Label opisZnacenjaKarteLabel;

	@FXML
	private Button pokreniZaustaviButton;

	@FXML
	private Button prikazListeFajlovaSaRezultatimaButton;

	@FXML
	private Label trenutniBrojOdigranihIgaraLabel;

	@FXML
	private Label vrijemeTrajanjaIgreLabel;

	@FXML
	private AnchorPane mapContainer;

	@FXML
	private AnchorPane deckContainer;

	@FXML
	private HBox playerNameContainer;
	
	public GameWindowController(ViewFactory viewFactory, String fxmlName) {
		super(viewFactory, fxmlName);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pokreniZaustaviButton.textProperty().bind(GameManager.getInstance().getStartStopProperty());
		vrijemeTrajanjaIgreLabel.textProperty().bind(GameManager.getInstance().getStopwatch().getElapsedTimeProperty());
		trenutniBrojOdigranihIgaraLabel.textProperty().bind(GameManager.getInstance().getDirectoryWatcher().getNumberOfGamesProperty().asString());
		GameManager.getInstance().getGameEndedProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == true) {
				clearEverything();
			}
		});
	}

	@FXML
	void pokreniZaustaviButtonOnAction() {
		if (GameManager.getInstance().getCurrentGame() == null) {
			viewFactory.showConfigurationWindow();
			opisZnacenjaKarteLabel.textProperty().bind(GameManager.getInstance().getCurrentGame().getCardDescription());
			displayEverything();
			GameManager.getInstance().newGame();
		} else {
			if (GameManager.getInstance().getCurrentGame().isPaused()) {
				GameManager.getInstance().unpause();
			} else {
				GameManager.getInstance().pause();
			}
		}
	}

	@FXML
	void prikazListeFajlovaSaRezultatimaButtonOnAction() {
		viewFactory.showResultsWindow();
	}

	private void displayMap() {
		mapContainer.getChildren().clear();
		GameManager.getInstance().getCurrentGame().getMap().setMaxSize(mapContainer.getWidth(), mapContainer.getHeight());
		GameManager.getInstance().getCurrentGame().getMap().setMinSize(mapContainer.getWidth(), mapContainer.getHeight());
		GameManager.getInstance().getCurrentGame().getMap().setPrefSize(mapContainer.getWidth(), mapContainer.getHeight());
		mapContainer.getChildren().add(GameManager.getInstance().getCurrentGame().getMap());
	}

	private void displayDeck() {
		deckContainer.getChildren().clear();
		DeckOfCards.getInstance().setFitHeight(deckContainer.getHeight());
		DeckOfCards.getInstance().setFitWidth(deckContainer.getWidth());
		deckContainer.getChildren().add(DeckOfCards.getInstance());
	}

	private void displayPlayerNames() {
		playerNameContainer.getChildren().clear();
		for (Player player : GameManager.getInstance().getCurrentGame().getPlayers()) {
			Label label = new Label(player.getPlayerName());
			label.setTextFill(Paint.valueOf(player.getFigureColor().getHexColorValue()));
			playerNameContainer.getChildren().add(label);
		}
	}

	private void displayFigureList() {
		ObservableList<Figure> figureList = FXCollections.observableArrayList();
		for (Player player : GameManager.getInstance().getCurrentGame().getPlayers()) {
			figureList.addAll(player.getFigures());
		}
		figureListView.setItems(figureList);
		figureListView.setCellFactory(param -> new ListCell<Figure>() {
			@Override
			protected void updateItem(Figure item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText("Figura " + item.getFigureIndex() + " od " + item.getOwner().getPlayerName());
				}
			}
		});
		figureListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					viewFactory.showFigureMovementWindow(figureListView.getSelectionModel().getSelectedItem());
				}
			}
		});
	}

	private void displayEverything() {
		displayMap();
		displayDeck();
		displayPlayerNames();
		displayFigureList();
	}

	private void clearEverything() {
		mapContainer.getChildren().clear();
		playerNameContainer.getChildren().clear();
		DeckOfCards.getInstance().shuffleDeck();
		figureListView.getItems().clear();
	}
}
