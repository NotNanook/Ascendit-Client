package me.ascendit.module;

public enum Category {
	COMBAT("Combat"), MOVEMENT("Movement"), PLAYER("Player"), RENDER("Render"), MISC("Misc");

	public String name;
	public boolean categoryexpanded;
	public int moduleindex;

	Category(final String name) {
		this.name = name;
	}
}