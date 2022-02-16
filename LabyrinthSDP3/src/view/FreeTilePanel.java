/*
 * Author: Darren Sin
 */
package view;

import javax.swing.*;
import model.*;

// This method displays the "free" tile that isn't currently on the board, and provides options for the rotation of the tile
@SuppressWarnings("serial")
public class FreeTilePanel extends JPanel {
	// Instance Variables

	public static JButton clockwiseButton;
	public static JButton counterClockwiseButton;
	
	private JLabel freeTileLabel = new JLabel();
	private Tile freeTile;
	
	// Constructor Method
	public FreeTilePanel(Tile freeTile) {
		setLayout(null);
		setBounds(1056, 720, 288 + 0, 96);
		
		this.freeTile = freeTile;
		
		// Display components
		displayButtons();
		displayTile();
	}
	
	// Getters and Setters

	public Tile getFreeTile() {
		return freeTile;
	}

	public void setFreeTile(Tile movebleTile) {
		this.freeTile = movebleTile;
	}
	
	// Utility Methods
	
	// This method displays an image of the tile corresponding to the current free tile
	public void displayTile() {
		String imagePath = "images/" + freeTile.getName() + String.valueOf(freeTile.getRotation()) + ".png";
		freeTileLabel.setIcon(new ImageIcon(imagePath));
		freeTileLabel.setBounds(96, 0 + 0, 96, 96);
		add(freeTileLabel);		
	}
	
	// This method displays the buttons to the screen that control rotation of the tile
	public void displayButtons() {
		counterClockwiseButton = new JButton();
		counterClockwiseButton.setIcon(new ImageIcon("images/CounterClockwise.png"));
		counterClockwiseButton.setBounds(0, 0 + 0, 96, 96);
		counterClockwiseButton.setActionCommand("counterclockwise");
		add(counterClockwiseButton);
		
		
		clockwiseButton = new JButton();
		clockwiseButton.setIcon(new ImageIcon("images/Clockwise.png"));
		clockwiseButton.setBounds(192, 0 + 0, 96, 96);
		clockwiseButton.setActionCommand("clockwise");
		add(clockwiseButton);
		
	}
}
