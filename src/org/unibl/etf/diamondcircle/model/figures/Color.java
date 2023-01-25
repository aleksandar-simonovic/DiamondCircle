package org.unibl.etf.diamondcircle.model.figures;

public enum Color {
	RED("#FF0000"), GREEN("#00FF00"), BLUE("#0000FF"), YELLOW("#8B8000");

	private String hexColorValue;

	private Color(String hexColorValue) {
		this.hexColorValue = hexColorValue;
	}

	public String getHexColorValue() {
		return hexColorValue;
	}
}
