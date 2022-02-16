package model;

import java.util.Arrays;

import java.awt.Color;

/*
 * Coded by: Rowel
 */

public class Player {
	
	//Name of the player 
	private String name;
	
	//position on the board 
	private int row;
	private int column;
		
	private Color color;
	
	//the hand of cards that the player has
	Card[] hand;
	
	public Player(String name, int row, int column, Color color, Card[] hand) {		
		this.name = name;
		this.hand = hand;
		this.row = row;
		this.column = column;
		this.color = color;
		this.hand = hand;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setHand(Card[] hand) {
		this.hand = hand;
	}

	public Color getColor() {
		return color;
	}

	public Card[] getHand() {
		return hand;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setCards(Card[] hand) {
		this.hand = hand;
	}

	@Override
	public String toString() {
		return "\nPlayer:\n" + "row: " + row + "\ncolumn: " + column + "\ncolor: " + color.getRGB() + "\nhand-->\n" + Arrays.deepToString(hand);
	}
	
	//Check if all cards are found.
	public boolean isWinner() {
		boolean check = true; 
		
		for(int i = 0; i < hand.length; i++) {
			if(!(hand[i].isFound())) {
				check = false;
			}
		}
		
		return check;
	}
	
	

}