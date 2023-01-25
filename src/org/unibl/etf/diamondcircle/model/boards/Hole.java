package org.unibl.etf.diamondcircle.model.boards;

import org.unibl.etf.diamondcircle.model.players.*;

public class Hole extends Element {
	private static final String HOLE_FILE_NAME = "resources/img/boards/hole.png";

	private Player owner;

	public Hole(Player owner) {
		super(HOLE_FILE_NAME);
		this.owner = owner;
	}

	public Player getOwner() {
		return owner;
	}
}