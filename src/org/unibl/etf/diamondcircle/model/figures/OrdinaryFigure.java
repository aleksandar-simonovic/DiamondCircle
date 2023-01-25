package org.unibl.etf.diamondcircle.model.figures;

import org.unibl.etf.diamondcircle.model.players.Player;

public class OrdinaryFigure extends Figure {

	private static final String ORDINARY_FIGURE_FILE_NAMES[] = {
			"resources/img/figures/ordinary-red.png",
			"resources/img/figures/ordinary-green.png",
			"resources/img/figures/ordinary-blue.png",
			"resources/img/figures/ordinary-yellow.png"
	};

	public OrdinaryFigure(int index, Color color, Player owner) {
		super(index, ORDINARY_FIGURE_FILE_NAMES[color.ordinal()], color, owner);
	}

	@Override
	public int getTotalNumberOfSteps(int numberOfSteps) {
		return numberOfSteps;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("OrdinaryFigure, " + color + ") - predjeni put (");
		sb.append(crossedFields.toString());
		sb.append(") - stigla do cilja: " + (finished ? "da" : "ne"));
		return sb.toString();
	}
	
	@Override
	public String whoAmI() {
		return "Obicna figura ";
	}
}
