package org.unibl.etf.diamondcircle.controllers;

import javafx.fxml.*;
import javafx.stage.*;
import javafx.scene.control.*;
import org.unibl.etf.diamondcircle.view.*;

public class WelcomeWindowController extends BaseController {

	@FXML
	private Button enterButton;

	public WelcomeWindowController(ViewFactory viewFactory, String fxmlName) {
		super(viewFactory, fxmlName);
	}

	@FXML
	void enterOnAction() {
		viewFactory.showGameWindow();
		viewFactory.closeStage((Stage) enterButton.getScene().getWindow());
	}
}