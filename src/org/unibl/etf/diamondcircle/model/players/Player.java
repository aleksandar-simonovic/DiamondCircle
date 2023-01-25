package org.unibl.etf.diamondcircle.model.players;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import org.unibl.etf.diamondcircle.model.cards.*;
import org.unibl.etf.diamondcircle.model.figures.*;
import org.unibl.etf.diamondcircle.model.game.*;
import org.unibl.etf.diamondcircle.util.*;
import javafx.application.*;

public class Player extends Thread {

	private static final String LOG_MESSAGE = "Thread was interrupted while waiting!";

	private static final int NUMBER_OF_FIGURES = 4;

	private final Random random = new Random();

	private final Game game;

	private final Semaphore playerSemaphore;

	private final String playerName;

	private final Color figureColor;

	private final Figure[] figures = new Figure[NUMBER_OF_FIGURES];

	private volatile int currentFigureIndex = 0;

	private boolean playerFinished = false;

	private int playerIndex;

	public Player(Game game, String playerName, int playerIndex, Color figureColor, Semaphore playerSemaphore) {

		this.game = game;
		this.playerName = playerName;
		this.figureColor = figureColor;
		this.playerSemaphore = playerSemaphore;
		this.playerIndex = playerIndex;

		for (int i = 0; i < figures.length; i++) {
			switch (random.nextInt(3)) {
				case 0:
					figures[i] = new OrdinaryFigure(i, figureColor, this);
					break;
				case 1:
					figures[i] = new LevitatingFigure(i, figureColor, this);
					break;
				case 2:
					figures[i] = new SuperFastFigure(i, figureColor, this);
					break;
			}
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public Color getFigureColor() {
		return figureColor;
	}

	public int getCurrentFigureIndex() {
		return currentFigureIndex;
	}

	public Semaphore getPlayerSemaphore() {
		return playerSemaphore;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Player other = (Player) obj;
		return Objects.equals(playerName, other.playerName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(playerName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Igrac " + playerIndex + "- " + playerName + "\n");
		for (Figure f : figures) {
			sb.append("\t" + f.toString() + "\n");
		}
		return sb.toString();
	}

	@Override
	public void run() {
		while (!playerFinished) {
			try {
				playerSemaphore.acquire();

				while (game.isPaused()) {
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
						}
					}
				}
				if (!game.isPaused()) {
					playMove();
					game.nextPlayerTurn();
				}
			} catch (InterruptedException e) {
				SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
			}
		}
	}

	private void playMove() {
		// Ocisti postavljene rupe koje pripadaju ovom igracu jer rupe traju 1 potez
		game.getMap().clearHoles(this);

		// Dohvati tekucu figuru
		Figure currentFigure = figures[currentFigureIndex];

		// Izvuci kartu iz spila
		Card currentCard = DeckOfCards.getInstance().drawCard();

		// Definisi poruku
		Platform.runLater(() -> {
			game.getCardDescription().set(Game.generateMessage(this, currentFigure, currentCard));
		});

		try {
			sleep(1000);
		} catch (InterruptedException e) {
			SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
		}

		if (currentCard instanceof SpecialCard) {
			game.getMap().placeHoles(this, SpecialCard.NUMBER_OF_HOLES);
		} else {
			OrdinaryCard ordinaryCard = (OrdinaryCard) currentCard;
			currentFigure.move(ordinaryCard.getValue(), game.getMap());
			if (!currentFigure.isAlive() || currentFigure.isFinished()) {
				currentFigureIndex++;
			}
		}

		if (currentFigureIndex == NUMBER_OF_FIGURES) {
			playerFinished = true;
			game.kickPlayerFromGame(this);
		}
	}

	public Figure[] getFigures() {
		return figures;
	}
}
