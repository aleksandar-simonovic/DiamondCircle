package org.unibl.etf.diamondcircle.model.figures;

import org.unibl.etf.diamondcircle.model.interfaces.*;
import org.unibl.etf.diamondcircle.model.players.*;

public class LevitatingFigure extends Figure implements Levitate {

	private static final String LEVITATING_FIGURE_FILE_NAMES[] = {
			"resources/img/figures/levitating-red.png",
			"resources/img/figures/levitating-green.png",
			"resources/img/figures/levitating-blue.png",
			"resources/img/figures/levitating-yellow.png"
	};

	public LevitatingFigure(int index, Color color, Player owner) {
		super(index, LEVITATING_FIGURE_FILE_NAMES[color.ordinal()], color, owner);
	}

	@Override
	public int getTotalNumberOfSteps(int numberOfSteps) {
		return numberOfSteps;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("LevitatingFigure, " + color + ") - predjeni put (");
		sb.append(crossedFields.toString());
		sb.append(") - stigla do cilja: " + (finished ? "da" : "ne"));
		return sb.toString();
	}
	
	@Override
	public String whoAmI() {
		return "Lebdeca Figura ";
	}
}
