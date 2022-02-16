package model;

/*
 * Coded by: Rowel
 */

public class Card {
	
	//The card stores the tile that it represents 	
	private Tile tile;
	
	//name of the card
	private String name;
	
	//variable to track if the treasure in the card has been found
	private boolean found = false;

	public Card(String name , Tile tile) {
		super();
		this.name = name;
		this.tile = tile; 
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public boolean isFound() {
		return found;
	}

	@Override
	public String toString() {
		return "\n\n\tCard:\n" + "Tile: " + tile + "\n\tname: " + name + "\n\tfound: " + found;
	}

	public String getName() {
		return name;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
