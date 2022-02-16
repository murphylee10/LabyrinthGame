/*
 * Author: Murphy Lee
 */

package view;

import javax.swing.*;

import controller.LabyrinthController;

// Subclass of JFrame that displays all the panels 
@SuppressWarnings("serial")
public class LabyrinthFrame extends JFrame {
	// Instance Variables
	
	private boolean gameStarted = false;
	public static int numCards;             // Number of cards that each player has 
	
	// Constructor method for JFrame
	public LabyrinthFrame(BoardPanel boardPanel, FreeTilePanel freeTilePanel, ControlPanel controlPanel, SavePanel savePanel) {
		super("Labyrinth");
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
	
		// Adding panels to the Frame
		add(boardPanel);
		add(freeTilePanel);
		add(controlPanel);
		add(savePanel);
		
	}
	
	// Getters and Setters
	
	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}
	
	// This method will prompt a user to save results and close the window
	public void promptResults() {
		// Prompting user if they want to change results
		int saveChoice = JOptionPane.showConfirmDialog(this, "Do you want to save the results?", "Saving Results", JOptionPane.YES_NO_OPTION);
		
		// If yes, save results to a text file
		if(saveChoice == JOptionPane.YES_OPTION) {
			LabyrinthController.saveResults();
		}
            
       
		dispose();
	}
	
	
}