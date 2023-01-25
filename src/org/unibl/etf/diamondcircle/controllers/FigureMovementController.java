package org.unibl.etf.diamondcircle.controllers;

import java.net.*;
import java.util.*;
import java.util.logging.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import org.unibl.etf.diamondcircle.view.*;
import org.unibl.etf.diamondcircle.*;
import org.unibl.etf.diamondcircle.model.boards.*;
import org.unibl.etf.diamondcircle.model.figures.*;
import org.unibl.etf.diamondcircle.util.*;

import javafx.scene.paint.Paint;

public class FigureMovementController extends BaseController implements Initializable {

	private static final String LOG_MESSAGE = "Thread was interrupted while waiting!";

	public FigureMovementController(ViewFactory viewFactory, Figure selectedFigure, String fxmlName) {
		super(viewFactory, fxmlName);
		this.selectedFigure = selectedFigure;
	}

	private final Figure selectedFigure;

	@FXML
	private GridPane gridPane;

	@Override
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {
		while (gridPane.getRowConstraints().size() != GameManager.getInstance().getCurrentGame()
				.getMatrixDimensions()) {
			gridPane.getRowConstraints().remove(0);
			gridPane.getColumnConstraints().remove(0);
		}

		gridPane.setAlignment(Pos.CENTER);

		Thread thread = new Thread(() -> {
			while (GameManager.getInstance().getCurrentGame() != null) {
				try {
					for (Field field : (LinkedList<Field>) selectedFigure.getCrossedFields().clone()) {
						Platform.runLater(() -> {
							Rectangle rectangle = new Rectangle(40, 40,
									Paint.valueOf(selectedFigure.getColor().getHexColorValue()));
							GridPane.setHalignment(rectangle, HPos.CENTER);
							GridPane.setValignment(rectangle, VPos.CENTER);
							gridPane.add(rectangle,
									(field.getOrdinalNumber() - 1)
											% GameManager.getInstance().getCurrentGame().getMatrixDimensions(),
									(field.getOrdinalNumber() - 1)
											/ GameManager.getInstance().getCurrentGame().getMatrixDimensions());
						});
					}
					Thread.sleep(500);
				} catch (InterruptedException e) {
					SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
}
