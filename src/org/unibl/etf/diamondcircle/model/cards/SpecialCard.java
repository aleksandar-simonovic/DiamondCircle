package org.unibl.etf.diamondcircle.model.cards;

import org.unibl.etf.diamondcircle.util.*;

public class SpecialCard extends Card {
	
	private static final String PROPERTY_NAME = "numberOfHoles";
	public static final int NUMBER_OF_HOLES = Integer.parseInt(AppProperties.getInstance().getProperty(PROPERTY_NAME));
	private static final String SPECIAL_CARD_FILE_NAME = "resources/img/cards/card-joker.png";

	public SpecialCard() {
		super(SPECIAL_CARD_FILE_NAME);
	}
}