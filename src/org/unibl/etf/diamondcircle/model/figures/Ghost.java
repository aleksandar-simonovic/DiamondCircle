package org.unibl.etf.diamondcircle.model.figures;

import java.util.*;
import java.util.logging.*;
import org.unibl.etf.diamondcircle.model.game.*;
import org.unibl.etf.diamondcircle.util.*;

public class Ghost extends Thread {

	private static final String LOG_MESSAGE = "Thread was interrupted while waiting!";

	private static final int PERIOD = 5000;

	private Random random = new Random();

	private Game game;

	public Ghost(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		while (!game.isFinished()) {
			if (game.isPaused()) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
					}
				}
			}
			if (!game.isPaused()) {
				game.getMap().clearDiamonds();
				game.getMap().placeDiamonds(2 + random.nextInt(game.getMatrixDimensions() - 1));
				try {
					Thread.sleep(PERIOD);
				} catch (InterruptedException e) {
					SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
				}
			}
		}
	}
}
