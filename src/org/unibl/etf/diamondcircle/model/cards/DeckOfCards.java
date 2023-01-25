package org.unibl.etf.diamondcircle.model.cards;

import java.util.*;
import javafx.animation.*;
import javafx.scene.image.*;
import javafx.scene.transform.*;
import javafx.util.*;

public class DeckOfCards extends ImageView {

	private static DeckOfCards instance = null;

	private LinkedList<Card> cards = new LinkedList<Card>();

	private DeckOfCards() {
		for (int i = 0; i < 10; i++) {
			cards.add(new OrdinaryCard(1));
			cards.add(new OrdinaryCard(2));
			cards.add(new OrdinaryCard(3));
			cards.add(new OrdinaryCard(4));
		}
		for (int i = 0; i < 12; i++) {
			cards.add(new SpecialCard());
		}
		shuffleDeck();
	}

	public static DeckOfCards getInstance() {
		if (instance == null) {
			instance = new DeckOfCards();
		}
		return instance;
	}

	public void shuffleDeck() {
		setImage(Card.cardBack);
		Collections.shuffle(cards);
	}

	public Card drawCard() {
		Card card = cards.removeFirst();
		RotateTransition rotator = createRotator(card);
		cards.addLast(card);
		rotator.play();
		return card;
	}

	private RotateTransition createRotator(Image newCard) {
		RotateTransition rotatorFirst = new RotateTransition(Duration.millis(250), this);
		RotateTransition rotatorSecond = new RotateTransition(Duration.millis(250), this);
		rotatorFirst.setAxis(Rotate.Y_AXIS);
		rotatorFirst.setFromAngle(0);
		rotatorFirst.setToAngle(90);
		rotatorFirst.setInterpolator(Interpolator.LINEAR);
		rotatorFirst.setCycleCount(1);
		rotatorFirst.setOnFinished((event) -> {
			setImage(newCard);
			rotatorSecond.play();
		});
		rotatorSecond.setAxis(Rotate.Y_AXIS);
		rotatorSecond.setFromAngle(90);
		rotatorSecond.setToAngle(0);
		rotatorSecond.setInterpolator(Interpolator.LINEAR);
		rotatorSecond.setCycleCount(1);
		return rotatorFirst;
	}
}
