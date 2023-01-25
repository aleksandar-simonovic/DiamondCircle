package org.unibl.etf.diamondcircle.model.game;

import java.util.*;
import java.util.concurrent.*;
import org.unibl.etf.diamondcircle.*;
import org.unibl.etf.diamondcircle.model.boards.Board;
import org.unibl.etf.diamondcircle.model.cards.*;
import org.unibl.etf.diamondcircle.model.figures.*;
import org.unibl.etf.diamondcircle.model.players.*;
import javafx.application.*;
import javafx.beans.property.*;

import static org.unibl.etf.diamondcircle.model.figures.Color.*;

public class Game {

	private final int numberOfPlayers;
	private final int matrixDimensions;

	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Color> playerColors = new ArrayList<Color>();
	private LinkedList<Semaphore> playerSemaphores = new LinkedList<Semaphore>();

	private Random random = new Random();

	private volatile boolean paused = false;
	private volatile boolean finished = false;

	private Board map;

	private Ghost ghost = new Ghost(this);

	private StringProperty cardDescription = new SimpleStringProperty();

	public Game(int numberOfPlayers, int matrixDimensions) {
		this.numberOfPlayers = numberOfPlayers;
		this.matrixDimensions = matrixDimensions;

		playerColors.add(RED);
		playerColors.add(BLUE);
		playerColors.add(GREEN);
		playerColors.add(YELLOW);

		map = new Board(this, matrixDimensions);

		for (int i = 0; i < numberOfPlayers; i++) {
			playerSemaphores.add(new Semaphore(0));
			players.add(new Player(this, "Player#" + i, i, playerColors.remove(random.nextInt(playerColors.size())),
					playerSemaphores.get(i)));
		}

		Collections.shuffle(playerSemaphores);
	}

	public void newGame() {
		DeckOfCards.getInstance().shuffleDeck();
		for (Player player : players) {
			player.setDaemon(true);
			player.start();
		}
		nextPlayerTurn();
		ghost.start();
	}

	public void pause() {
		paused = true;
	}

	public void unpause() {
		paused = false;
		synchronized (ghost) {
			ghost.notify();
		}
		for (Player player : players) {
			synchronized (player) {
				player.notify();
			}
		}
	}

	public void nextPlayerTurn() {
		if (!playerSemaphores.isEmpty()) {
			Semaphore semaphore = playerSemaphores.removeFirst();
			playerSemaphores.addLast(semaphore);
			semaphore.release();
		}
	}

	public Board getMap() {
		return map;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public int getMatrixDimensions() {
		return matrixDimensions;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isFinished() {
		return finished;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public StringProperty getCardDescription() {
		return cardDescription;
	}

	public static String generateMessage(Player currentPlayer, Figure currentFigure, Card currentCard) {
		StringBuilder sb = new StringBuilder();
		sb.append("Na potezu je igrac: ");
		sb.append(currentPlayer.getPlayerName() + ".\n");
		sb.append(currentFigure.whoAmI());
		sb.append(currentPlayer.getCurrentFigureIndex());
		if (currentCard instanceof SpecialCard) {
			sb.append(" postavlja rupe na mapu");
		} else {
			OrdinaryCard ordinaryCard = (OrdinaryCard) currentCard;
			sb.append(" se krece za ");
			sb.append(currentFigure.getTotalNumberOfSteps(ordinaryCard.getValue()) + currentFigure.getNumberOfDiamonds() + " polja.");
		}
		return sb.toString();
	}

	public void kickPlayerFromGame(Player player) {
		playerSemaphores.remove(player.getPlayerSemaphore());

		if (playerSemaphores.isEmpty()) {
			finished = true;
			Platform.runLater(() -> {
				cardDescription.set("");
			});
			GameManager.getInstance().finish();
		}
	}
}