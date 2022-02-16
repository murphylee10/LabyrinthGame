/*
 * Author: Murphy Lee
 */

package view;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import model.*;

// This method shows the card deck for each player, as well as their score
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
	// Instance Variables 
	
	public static int[] scores = new int[4]; 
	
	private Player[] players;
	private JLabel[][] playerCardLabels;
	private JLabel displayLabel;
	private Card[][] cards = new Card[4][];
	
	// Constructor Methods - Parameters include the number of players each card has, as well as a matrix of card filenames
	public PlayerPanel(Player[] players, int numCards) {
		setLayout(null);                  // So that we can position labels by coordinates
		setBounds(960, 50, 672, 528);     // Location and size of panel
		
		displayLabel = new JLabel();
		displayLabel.setBounds(0, 0, 672, 528);
		
		// Set scores of all the players to 0
		for (int i = 0; i < scores.length; i++) 
			scores[i] = 0;
		
		// Set array of cards
		this.players = players;
		
		// Add all cards to the panel
		for (int j = 0; j < players.length; j++) {
			this.cards[j] = players[j].getHand();
		}
		
		// Create the matrix of JLabels
		playerCardLabels = new JLabel[4][numCards + 1];
		
		initializelabels();
		
		
		// Add the JLabels for player statuses to the panel
		displayText();
		displayCards();
		
		add(displayLabel);
	}
	
	// Getters and Setters

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	// Utility Methods

	public void initializelabels() {
		for (int row = 0; row < playerCardLabels.length; row++) {
			for (int col = 0; col < playerCardLabels[row].length; col++) {
				playerCardLabels[row][col] = new JLabel();
				playerCardLabels[row][col].setBounds(col * 96, row * 132, 96, 132);
			}
		}
	}
	
	// This method displays the text containing the player names + their score
	public void displayText() {
		// Red Player
		playerCardLabels[0][0].setText("<html>" + "Red Player" + 
				"<br/>" + "Score: " + String.valueOf(scores[0]));    								
		playerCardLabels[0][0].setFont(new FontUIResource("Serif", Font.BOLD, 16));          
		playerCardLabels[0][0].setForeground(Color.RED);										
		playerCardLabels[0][0].setOpaque(true);												
		
		displayLabel.add(playerCardLabels[0][0]);
		
		// Blue Player
		playerCardLabels[1][0].setText("<html>" + "Blue Player" + 
				"<br/>" + "Score: " + String.valueOf(scores[1]));    								
		playerCardLabels[1][0].setFont(new FontUIResource("Serif", Font.BOLD, 16));          
		playerCardLabels[1][0].setForeground(Color.BLUE);										
		playerCardLabels[1][0].setOpaque(true);												
		
		displayLabel.add(playerCardLabels[1][0]);
		
		// Green Player
		playerCardLabels[2][0].setText("<html>" + "Green Player" + 
				"<br/>" + "Score: " + String.valueOf(scores[2]));    								
		playerCardLabels[2][0].setFont(new FontUIResource("Serif", Font.BOLD, 16));          
		playerCardLabels[2][0].setForeground(new Color(63, 199, 86));										
		playerCardLabels[2][0].setOpaque(true);												
		
		displayLabel.add(playerCardLabels[2][0]);
		
		// Yellow Player
		playerCardLabels[3][0].setText("<html>" + "Yellow Player" + 
				"<br/>" + "Score: " + String.valueOf(scores[3]));    								
		playerCardLabels[3][0].setFont(new FontUIResource("Serif", Font.BOLD, 16));          
		playerCardLabels[3][0].setForeground(new Color(221, 221, 104));										
		playerCardLabels[3][0].setOpaque(true);												
		
		displayLabel.add(playerCardLabels[3][0]);
		
	}
	
	// This method displays the "hand" of each player
	public void displayCards() {
		for (int row = 0; row < playerCardLabels.length; row++) {
			for (int col = 1; col < playerCardLabels[row].length; col++) {
				String filePath = "";
				// If the player card's treasure hasn't been found, set the file path to the associating PNG
				if (cards[row][col - 1].isFound() == false) {
					filePath = "images/Card" + cards[row][col - 1].getName() + ".png";				
				}
				
				// If the player card's treasure has been found, display checkmark card
				else {
					filePath = "images/CardCheck.png";
					
				}
				playerCardLabels[row][col].setIcon(new ImageIcon(filePath));
				displayLabel.add(playerCardLabels[row][col]);
				
			}
			
		}
	}
	
}