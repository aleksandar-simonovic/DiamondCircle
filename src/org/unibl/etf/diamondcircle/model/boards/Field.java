package org.unibl.etf.diamondcircle.model.boards;

import org.unibl.etf.diamondcircle.model.figures.*;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class Field extends StackPane {

	private Board map;
	private Element element;
	private Figure figure;
	private int ordinalNumber;

	private ImageView elementImageView;
	private ImageView figureImageView;
	private Label ordinalNumberLabel;

	public Field(Board map, int ordinalNumber) {

		this.ordinalNumber = ordinalNumber;
		this.map = map;

		setWidth(map.getWidth() / map.getGame().getMatrixDimensions());
		setHeight(map.getHeight() / map.getGame().getMatrixDimensions());

		elementImageView = new ImageView();
		figureImageView = new ImageView();
		ordinalNumberLabel = new Label(String.valueOf(ordinalNumber));

		figureImageView.fitWidthProperty().bind(widthProperty());
		figureImageView.fitHeightProperty().bind(heightProperty());
		figureImageView.setPreserveRatio(true);
		elementImageView.fitHeightProperty().bind(heightProperty());
		elementImageView.fitWidthProperty().bind(widthProperty());
		elementImageView.setPreserveRatio(true);

		getChildren().addAll(ordinalNumberLabel, elementImageView, figureImageView);
	}

	public synchronized Element getElement() {
		return element;
	}

	public synchronized void setElement(Element element) {
		this.element = element;
		if (element != null) {
			Platform.runLater(() -> {
				elementImageView.setImage(element);
			});
		} else {
			Platform.runLater(() -> {
				elementImageView.setImage(null);
			});
		}
	}

	public synchronized Figure getFigure() {
		return figure;
	}

	public synchronized void setFigure(Figure figure) {
		this.figure = figure;
		if (figure != null) {
			Platform.runLater(() -> {
				figureImageView.setImage(figure);
			});
		} else {
			Platform.runLater(() -> {
				figureImageView.setImage(null);
			});
		}
	}

	public int getOrdinalNumber() {
		return ordinalNumber;
	}

	public void setOrdinalNumber(int ordinalNumber) {
		this.ordinalNumber = ordinalNumber;
	}

	public Board getMap() {
		return map;
	}
	
	@Override
	public String toString() {
		return "" + ordinalNumber;
	}
}