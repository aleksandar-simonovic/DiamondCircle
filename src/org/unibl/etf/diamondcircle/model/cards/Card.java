package org.unibl.etf.diamondcircle.model.cards;

import javafx.scene.image.*;

public abstract class Card extends Image {
	private static final String CARD_BACK_FILE_NAME = "resources/img/cards/card-back.png";
	public static final Image cardBack = new Image(CARD_BACK_FILE_NAME);

	public Card(String imagePath) {
		super(imagePath);
	}
}