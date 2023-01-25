package org.unibl.etf.diamondcircle.model.figures;

import java.util.*;
import java.util.logging.*;
import org.unibl.etf.diamondcircle.model.boards.*;
import org.unibl.etf.diamondcircle.model.boards.Board;
import org.unibl.etf.diamondcircle.model.interfaces.*;
import org.unibl.etf.diamondcircle.model.players.*;
import org.unibl.etf.diamondcircle.util.*;
import javafx.scene.image.*;

public abstract class Figure extends Image {

	private static final String LOG_MESSAGE = "Thread was interrupted while waiting!";

	private static final int STEP_PERIOD = 1000;

	protected final Color color;
	protected final LinkedList<Field> crossedFields = new LinkedList<>();
	protected boolean alive = true;
	protected boolean finished = false;
	protected int numberOfDiamonds = 0;
	protected long movementDuration;
	protected int figureIndex;
	protected Player owner;

	public Figure(int figureIndex, String imagePath, Color color, Player owner) {
		super(imagePath);
		this.color = color;
		this.figureIndex = figureIndex;
		this.owner = owner;
	}

	public Color getColor() {
		return color;
	}

	public boolean isAlive() {
		return alive;
	}

	public int getNumberOfDiamonds() {
		return numberOfDiamonds;
	}

	public LinkedList<Field> getCrossedFields() {
		return crossedFields;
	}

	public boolean isFinished() {
		return finished;
	}

	public int getFigureIndex() {
		return figureIndex;
	}

	public abstract int getTotalNumberOfSteps(int numberOfSteps);

	public void move(int numberOfStepsToAdvance, Board map) {

		long start = System.currentTimeMillis();

		int totalSteps;

		totalSteps = getTotalNumberOfSteps(numberOfStepsToAdvance) + numberOfDiamonds;

		numberOfDiamonds = 0;

		Field currentField;
		Field nextField;
		ListIterator<Field> iterator;

		if (crossedFields.isEmpty()) {
			currentField = null;
			iterator = map.getTrajectory().listIterator();

		} else {
			currentField = crossedFields.getLast();
			if (currentField.getElement() instanceof Hole && !(this instanceof Levitate)) {
				fallThroughHole(currentField);
				calculateTime(start);
				return;
			} else if (currentField.getElement() instanceof Diamond) {
				pickUpDiamond(currentField);
			}
			iterator = map.getTrajectory().listIterator(map.getTrajectory().indexOf(currentField));
		}

		while (iterator.hasNext()) {
			if (totalSteps > 0) {
				nextField = iterator.next();
				if (nextField.getFigure() == null) {

					if (currentField != null) {
						currentField.setFigure(null);
					}

					if (nextField.getElement() instanceof Hole && !(this instanceof Levitate)) {
						fallThroughHole(nextField);
						calculateTime(start);
						return;
					} else {
						crossedFields.add(nextField);

						nextField.setFigure(this);

						if (nextField.getElement() instanceof Diamond) {
							pickUpDiamond(nextField);
						}

						currentField = nextField;

						totalSteps--;

						try {
							Thread.sleep(STEP_PERIOD);
						} catch (InterruptedException e) {
							SimpleLogger.getInstance().log(Level.SEVERE, LOG_MESSAGE, e);
						}
					}
				} else {
					crossedFields.add(nextField);
				}
			} else {
				calculateTime(start);
				return;
			}
		}
		finished = true;
		currentField.setFigure(null);
		calculateTime(start);
	}

	private void pickUpDiamond(Field field) {
		field.setElement(null);
		numberOfDiamonds++;
	}

	private void fallThroughHole(Field field) {
		field.setElement(null);
		field.setFigure(null);
		alive = false;
	}

	private void calculateTime(long start) {
		long end = System.currentTimeMillis();
		movementDuration += (end - start);
	}

	@Override
	public String toString() {
		return "Figura " + figureIndex + "(";
	}
	
	public String whoAmI() {
		return "Figura ";
	}

	public Player getOwner() {
		return owner;
	}
}