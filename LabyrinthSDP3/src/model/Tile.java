/*
 * Author: Murphy Lee
 */
package model;

// This class represents a tile that contains distinct properties for player movement
public class Tile {
	// Instance Variables
	
	private String name;
	private String type;
	private int rotation;
	private boolean moveable;
	private int row;
	private int col;
	
	// A value of 'true' means the path is available: [0] - up, [1] - right, [2] - down, [3] - left
	private boolean[] openPaths = new boolean[4];     
	
	// Constructor Method
	public Tile(String name, String type, int rotation, boolean moveable, int row, int col) {
		super();
		this.name = name;
		this.type = type;
		this.rotation = rotation;
		this.moveable = moveable;
		this.row = row;
		this.col = col;
		
		setOpenPaths(openPaths, type, rotation);
	}

	// Getters and Setters
	
	public boolean[] getOpenPaths() {
		return openPaths;
	}
	
	// Custom Setter: Uses the type of tile and its rotation to determine which directions of movement are allowed
	public void setOpenPaths(boolean[] openPaths, String type, int rotation) {
		// L-shaped - 2 adjacent pathways will be open
		if (type.equals("L")) {
			// Determine up & down pathways based on rotation of the tile
			openPaths[0] = (rotation == 0 || rotation == 3) ? true : false;
			openPaths[2] = (rotation == 0 || rotation == 3) ? false : true;
			
			// Determine left & right pathways based on rotation of the tile
			openPaths[1] = (rotation == 0 || rotation == 1) ? true : false;
			openPaths[3] = (rotation == 0 || rotation == 1) ? false : true;
			
				
		}
		
		// T-shaped - There is only ever one direction that is closed off
		else if (type.equals("T")) {        
			// Determine up & down pathways based on rotation of the tile
			openPaths[0] = (rotation == 0) ? false : true;
			openPaths[2] = (rotation == 2) ? false : true;
			
			// Determine left & right pathways based on rotation of the tile
			openPaths[1] = (rotation == 1) ? false : true;
			openPaths[3] = (rotation == 3) ? false : true;
		}
		
		// I-shaped - 2 opposite pathways will be open
		else {
			// Determine up & down pathways based on rotation of the tile
			openPaths[0] = (rotation == 0 || rotation == 2) ? true : false;
			openPaths[2] = (rotation == 0 || rotation == 2) ? true : false;
			
			// Determine left & right pathways based on rotation of the tile
			openPaths[1] = (rotation == 1 || rotation == 3) ? true : false;
			openPaths[3] = (rotation == 1 || rotation == 3) ? true : false;

		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	// toString() method

	@Override
	public String toString() {
		return "Tile [name=" + name + ", type=" + type + ", rotation=" + rotation + ", moveable=" + moveable + ", row="
				+ row + ", col=" + col + "]";
	}
	
}