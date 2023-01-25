package org.unibl.etf.diamondcircle.model.figures;

import org.unibl.etf.diamondcircle.model.players.Player;

public class SuperFastFigure extends Figure {

	private static final String SUPER_FAST_FIGURE_FILE_NAMES[] = {
			"resources/img/figures/superfast-red.png",
			"resources/img/figures/superfast-green.png",
			"resources/img/figures/superfast-blue.png",
			"resources/img/figures/superfast-yellow.png"
	};

	public SuperFastFigure(int index, Color color, Player owner) {
		super(index, SUPER_FAST_FIGURE_FILE_NAMES[color.ordinal()], color, owner);
	}

	@Override
	public int getTotalNumberOfSteps(int numberOfSteps) {
		return 2 * numberOfSteps;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("SuperFastFigure, " + color + ") - predjeni put (");
		sb.append(crossedFields.toString());
		sb.append(") - stigla do cilja: " + (finished ? "da" : "ne"));
		return sb.toString();
	}
	
	@Override
	public String whoAmI() {
		return "Super-brza figura ";
	}
}