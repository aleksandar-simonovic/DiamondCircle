package org.unibl.etf.diamondcircle;

import javafx.stage.*;
import javafx.application.*;
import org.unibl.etf.diamondcircle.view.*;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		ViewFactory viewFactory = new ViewFactory();
		viewFactory.showWelcomeWindow();
	}
}