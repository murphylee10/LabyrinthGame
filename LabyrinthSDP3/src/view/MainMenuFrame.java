/*
 * Author: Murphy Lee
 */
package view;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import java.awt.*;


// This class displays an intro screen, prompting the user for different game options
@SuppressWarnings("serial")
public class MainMenuFrame extends JFrame {
	public static JButton newGameButton;
	public static JButton loadGameButton;
	
	public static JLabel instructionsLabel;
	
	public MainMenuFrame() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 1920, 1080);
		
		newGameButton = new JButton("NEW GAME");
		newGameButton.setFont(new FontUIResource("", Font.BOLD, 28));
		newGameButton.setBackground(new Color(77, 166, 255));
		newGameButton.setOpaque(true);	
		newGameButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		newGameButton.setBounds(1000 + 0, 790 + 0, 350, 100);
		newGameButton.setActionCommand("new");

		menuPanel.add(newGameButton);
		
		loadGameButton = new JButton("LOAD GAME");
		loadGameButton.setFont(new FontUIResource("", Font.BOLD, 28));
		loadGameButton.setBackground(new Color(77, 166, 255));
		loadGameButton.setOpaque(true);	
		loadGameButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		loadGameButton.setBounds(1000 + 400, 790 + 0, 350, 100);
		loadGameButton.setActionCommand("load");
		menuPanel.add(loadGameButton);
		
		instructionsLabel = new JLabel();
		instructionsLabel.setIcon(new ImageIcon("images/Instructions.png"));
		instructionsLabel.setBounds(0, 0, 1920, 1080);
		menuPanel.add(instructionsLabel);
		
		add(menuPanel);
		
	}
}
