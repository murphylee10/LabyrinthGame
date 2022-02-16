/*
 * Author: Murphy
 */
package view;

import javax.swing.*;
import java.awt.*;

// This class contains a button to save data
@SuppressWarnings("serial")
public class SavePanel extends JPanel {
	// Instance Variables
	
	public static JButton saveButton;
	
	// Constructor Method
	public SavePanel() {
		setLayout(null);
		setBounds(1750, 50, 80, 60);
		
		saveButton = new JButton("Save");
		saveButton.setBackground(new Color(0, 204, 170));
		saveButton.setOpaque(true);	
		saveButton.setBorderPainted(false);
		saveButton.setBounds(0, 0, 80, 60);
		
		add(saveButton);
	}
}
