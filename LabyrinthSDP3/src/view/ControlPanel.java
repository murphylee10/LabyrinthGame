/*
 * Author: Murphy Lee
 */

package view;

import java.awt.Color;
import javax.swing.*;
import model.*;

// This method displays the buttons for moving a player, as well as the button to confirm a move
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	// Instance Variables
	
	private Player currentPlayer;
	
	// JButtons for player movement
	public static JButton upButton = new JButton();
	public static JButton downButton = new JButton();
	public static JButton leftButton = new JButton();
	public static JButton rightButton = new JButton();
	public static JButton confirmButton = new JButton();
	
	// Constructor Method
	public ControlPanel() {
		setLayout(null);
		setBounds(1440, 624, 288, 288);
		
		// Output buttons to the screen in disabled form
		displayButtons();
		
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		leftButton.setEnabled(false);
		rightButton.setEnabled(false);
		confirmButton.setEnabled(false);
		
	}
	
	
	// Getters and Setters
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	// Utility Methods
	
	// This method displays buttons to the panel, which control movement of the player
	public void displayButtons() {
		// Down Button
		downButton.setIcon(new ImageIcon("images/Arrow0.png"));
		downButton.setDisabledIcon(new ImageIcon("images/Arrow0BW.png"));
		downButton.setBounds(96, 192, 96, 96);
		downButton.setActionCommand("down");
		add(downButton);
		
		// Up Button
		upButton.setIcon(new ImageIcon("images/Arrow1.png"));
		upButton.setDisabledIcon(new ImageIcon("images/Arrow1BW.png"));		
		upButton.setBounds(96, 0, 96, 96);
		upButton.setActionCommand("up");
		add(upButton);
		
		// Right Button
		rightButton.setIcon(new ImageIcon("images/Arrow2.png"));
		rightButton.setDisabledIcon(new ImageIcon("images/Arrow2BW.png"));		
		rightButton.setBounds(192, 96, 96, 96);
		rightButton.setActionCommand("right");
		add(rightButton);
		
		// Left Button
		leftButton.setIcon(new ImageIcon("images/Arrow3.png"));
		leftButton.setDisabledIcon(new ImageIcon("images/Arrow3BW.png"));		
		leftButton.setBounds(0, 96, 96, 96);
		leftButton.setActionCommand("left");
		add(leftButton);
		
		// Button to confirm a move
		confirmButton.setText("Confirm");
		confirmButton.setBounds(96, 96, 96, 96);
		confirmButton.setActionCommand("confirm");
		add(confirmButton);
	}
	
	// This method enables/disables all the buttons of the control panel according to the value of the parameter
	public void setEnabling(boolean flag) {
		upButton.setEnabled(flag);
		downButton.setEnabled(flag);
		leftButton.setEnabled(flag);
		rightButton.setEnabled(flag);
		confirmButton.setEnabled(flag);
	}
	
	// This method sets the background color of the confirm button to indicate the current player
	public void setBackgroundColor(Color c) {
		confirmButton.setBackground(c);
		confirmButton.setOpaque(true);	
		confirmButton.setBorderPainted(false);
	}

}