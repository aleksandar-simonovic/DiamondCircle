package org.unibl.etf.diamondcircle.model.cards;

public class OrdinaryCard extends Card {

	private static final String[] CARD_FILE_NAMES = {
			"resources/img/cards/card-one.png",
			"resources/img/cards/card-two.png",
			"resources/img/cards/card-three.png",
			"resources/img/cards/card-four.png"
	};

	private int value;

	public OrdinaryCard(int value) {
		super(CARD_FILE_NAMES[value - 1]);
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}