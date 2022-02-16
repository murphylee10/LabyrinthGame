package view;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.FontUIResource;

//This class displays an intro screen, prompting the user for different game options
@SuppressWarnings("serial")
public class MainMenuFrame2 extends JFrame {
	public static JButton newGameButton;
	public static JButton loadGameButton;
	
	public static JLabel instructionsLabel;
	
	public MainMenuFrame2() {
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(0, 0, 1920, 1080);
		
		
		loadGameButton = new JButton("Load Game");
		loadGameButton.setFont(new FontUIResource("Serif", Font.BOLD, 28));
		loadGameButton.setBackground(new Color(77, 166, 255));
		loadGameButton.setOpaque(true);	
		loadGameButton.setBorderPainted(false);
		loadGameButton.setBounds(110, 0, 100, 80);
		loadGameButton.setActionCommand("load");
		menuPanel.add(loadGameButton);
		
		instructionsLabel = new JLabel();
		instructionsLabel.setIcon(new ImageIcon("images/Instructions.png"));
		instructionsLabel.setBounds(0, 0, 1920, 1080);
		menuPanel.add(instructionsLabel);
		
		add(menuPanel);
		
	}
}

