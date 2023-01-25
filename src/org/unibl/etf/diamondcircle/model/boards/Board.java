package org.unibl.etf.diamondcircle.model.boards;

import java.util.*;
import org.unibl.etf.diamondcircle.model.game.*;
import org.unibl.etf.diamondcircle.model.players.*;
import javafx.geometry.*;
import javafx.scene.layout.*;

public class Board extends GridPane {

	private Game game;
	private Field[][] matrix;
	private ArrayList<Field> trajectory = new ArrayList<>();
	private Random random = new Random();

	public Board(Game game, int matrixDimensions) {
		this.game = game;

		matrix = new Field[matrixDimensions][matrixDimensions];

		setGridLinesVisible(true);
		setAlignment(Pos.CENTER);

		for (int i = 0; i < matrixDimensions; i++) {
			ColumnConstraints columnConstraint = new ColumnConstraints();
			RowConstraints rowConstraint = new RowConstraints();
			columnConstraint.setHalignment(HPos.CENTER);
			rowConstraint.setValignment(VPos.CENTER);
			columnConstraint.setPercentWidth(100.0 / matrixDimensions);
			rowConstraint.setPercentHeight(100.0 / matrixDimensions);
			getColumnConstraints().add(columnConstraint);
			getRowConstraints().add(rowConstraint);
		}

		for (int i = 0, ordinal = 1; i < matrixDimensions; i++) {
			for (int j = 0; j < matrixDimensions; j++) {
				Field field = new Field(this, ordinal++);
				setField(i, j, field);
				add(field, j, i);
			}
		}
		determineTrajectory();
	}

	public Field getField(int x, int y) {
		return matrix[x][y];
	}

	public void setField(int x, int y, Field tile) {
		matrix[x][y] = tile;
	}

	private void determineTrajectory() {
		int row = matrix.length;
		int col = matrix[0].length;

		if (row % 2 == 0) {
			spiralDiamondView(matrix, 0, 0, row - 2, col - 2, ((row - 1) * (col - 1)) - ((col) / 2) * 4);
		} else {
			spiralDiamondView(matrix, 0, 0, row - 1, col - 1, (row * col) - ((col + 1) / 2) * 4);
		}
	}

	private void spiralDiamondView(Field[][] matrix, int x, int y, int m, int n, int k) {
		int midCol = y + ((n - y) / 2);
		int midRow = midCol;

		for (int i = midCol, j = 0; i < n && k > 0; ++i, k--, j++) {
			trajectory.add(matrix[x + j][i]);
		}

		for (int i = n, j = 0; i >= midCol && k > 0; --i, k--, j++) {
			trajectory.add(matrix[(midRow) + j][i]);
		}

		for (int i = midCol - 1, j = 1; i >= y && k > 0; --i, k--, j++) {
			trajectory.add(matrix[(n) - j][i]);
		}

		for (int i = y + 1, j = 1; i < midCol && k > 0; ++i, k--, j++) {
			trajectory.add(matrix[(midRow) - j][i]);
		}

		if (x + 1 <= m - 1 && k > 0) {
			spiralDiamondView(matrix, x + 1, y + 1, m - 1, n - 1, k);
		}
	}

	public Field[][] getMatrix() {
		return matrix;
	}

	public ArrayList<Field> getTrajectory() {
		return trajectory;
	}

	public Game getGame() {
		return game;
	}

	public void placeDiamonds(int numberOfDiamondsToPlaceOnMap) {
		while (numberOfDiamondsToPlaceOnMap > 0) {
			Field field = trajectory.get(random.nextInt(trajectory.size()));
			synchronized (field) {
				if (field.getElement() != null) {
					continue;
				}
				field.setElement(new Diamond());
			}
			numberOfDiamondsToPlaceOnMap--;
		}
	}

	public void clearDiamonds() {
		for (Field field : trajectory) {
			synchronized (field) {
				if (field.getElement() instanceof Diamond) {
					field.setElement(null);
				}
			}
		}
	}

	public void placeHoles(Player owner, int numberOfHolesToPlaceOnMap) {
		while (numberOfHolesToPlaceOnMap > 0) {
			Field field = trajectory.get(random.nextInt(trajectory.size()));
			synchronized (field) {
				if (field.getElement() instanceof Hole) {
					continue;
				}
				field.setElement(new Hole(owner));
			}
			numberOfHolesToPlaceOnMap--;
		}
	}

	public void clearHoles(Player owner) {
		for (Field field : trajectory) {
			synchronized (field) {
				Element element = field.getElement();
				if (element instanceof Hole) {
					Hole hole = (Hole) element;
					if (hole.getOwner().equals(owner)) {
						field.setElement(null);
					}
				}
			}
		}
	}
}