/*
 * Authors: Darren, Rowel
 */

package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;
import controller.LabyrinthController;
import model.Card;
import model.Player;
import model.Tile;

// This class displays the tiles to the frame, and produces buttons for tile shifting
@SuppressWarnings("serial")
public class BoardPanel extends JPanel implements ActionListener {

	// Instance Variables
	private JLabel gameBoard;
	private JLabel[][] boardArray = new JLabel[7][7]; 				// Array that holds the position [row][column] of each tile on the board
	private static JLabel[][] paths = new JLabel[7][7]; 			// Array that holds the images of the path overlay
	public static boolean buttonEnabled = true;						// Checks if button has been pressed
	public static int column;										// Value of the column that the tile is being inserted in
	public static int row;											// Value of the row that the tile is being inserted in
	
	public static JButton disabledButton;							// JLabel for when the button is disabled

	public static JButton[][] buttonArray = new JButton[3][4];				// 2D array that stores each tile insertion button
	
	private JLabel[] playerLabels = new JLabel[4];					// This array holds each of the player labels

	private static ControlPanel controlPanel = new ControlPanel();
	
	Graphics graphics;
	Image image;
	private int moveX;
	private int moveY;
	private int currentPlayer = 0;
	private boolean start = true;
	
	public BoardPanel() {

		setLayout(null);
		setBounds(0,0,864,864);
		
		gameBoard = new JLabel(new ImageIcon("images/GameBoard.png"));
		gameBoard.setBounds(96,96,672,672);

		addTiles();
		pushButtons();
		initializePath();

		add(gameBoard);

	}
	
	// Utility methods
	
	/*
	 * This method creates a new JLabel for each player and sets their piece icon
	 */
	public void initializePlayers() {
		for (int i = 0; i < playerLabels.length; i++) {
			String playerName = LabyrinthController.players[i].getName();
			System.out.println(playerName);
			
			// Sets icon for each player
			playerLabels[i] = new JLabel();
			playerLabels[i].setIcon(new ImageIcon("images/" + playerName + ".png"));
		}
	}
	
	/*
	 * This method places the player on the screen using their row and column
	 */
	public void displayPlayers() {
		Player[] boardPlayers = LabyrinthController.players;
		for (int i = 0; i < boardPlayers.length; i++) {
		
			// If player is not the current player, add and display them onto the board
			if (i != currentPlayer) {
				gameBoard.remove(boardArray[boardPlayers[i].getRow()][boardPlayers[i].getColumn()]);
				playerLabels[i].setBounds(boardPlayers[i].getColumn() * 96, boardPlayers[i].getRow() * 96, 96, 96);
				gameBoard.add(playerLabels[i]);
				gameBoard.add(boardArray[boardPlayers[i].getRow()][boardPlayers[i].getColumn()]);
			}	
		
			else
				gameBoard.remove(boardArray[boardPlayers[currentPlayer].getRow()][boardPlayers[currentPlayer].getColumn()]);
				gameBoard.add(boardArray[boardPlayers[currentPlayer].getRow()][boardPlayers[currentPlayer].getColumn()]);

		}
		
	}
	
	
	/*
	 * Author: Darren
	 * This method Enables or disables all button
	 */
	public void setEnabling(boolean flag) {
		
		//Vertical movement buttons
		for (int y = 0; y < 2; y++) {

			for (int x = 0; x < 3; x++) {
				buttonArray[x][y].setEnabled(flag);
				buttonEnabled = flag;
			}

		}
		
		// Horizontal movement buttons
		for (int x = 0; x < 3; x++) {

			for (int y = 2; y < 4; y++) {
				buttonArray[x][y].setEnabled(flag);

			}

		}	
	}
	
	/*
	 * Author: Darren
	 * This method disables a button if it was opposite to the previously pressed button
	 */
	public void disableOppositeButton() {
		disabledButton.setEnabled(false);
	}

	/*
	 * Author: Rowel
	 * This method adds the tiles onto the board
	 */
	private void addTiles() {

		for(int row = 0; row < 7; row++) {

			for(int column = 0; column < 7; column++) {

				if(!(column%2 == 0 && row%2 == 0)) {
					String filePath = ("images/" + LabyrinthController.tiles[row][column].getName() + String.valueOf(LabyrinthController.tiles[row][column].getRotation()) + ".png");
					boardArray[row][column] = new JLabel();
					boardArray[row][column].setIcon(new ImageIcon(filePath));
					boardArray[row][column].setBounds(column*96, row*96, 96, 96);

					gameBoard.add(boardArray[row][column]);

				} else {
					boardArray[row][column] = new JLabel();
					boardArray[row][column].setBounds(column*96, row*96, 96, 96);

					gameBoard.add(boardArray[row][column]);

				}
			}
		}

	}
	
	/*
	 * Author: Rowel
	 * This method reloads the tiles when a tile has been inserted
	 */
	private void reloadTiles(int row, int column) {  
		// If a column was affected
		if (row == 0 || row == 6) {
			for (int index = 0; index < 7; index++) {
				ImageIcon newImg = new ImageIcon("images/" + LabyrinthController.tiles[index][column].getName() + 
						String.valueOf(LabyrinthController.tiles[index][column].getRotation()) + ".png");
				boardArray[index][column].setIcon(newImg);
			}
		}
		
		// If a row was affected
		else if (column == 0 || column == 6) {
			for (int index = 0; index < 7; index++) {
				ImageIcon newImg = new ImageIcon("images/" + LabyrinthController.tiles[row][index].getName() + 
						String.valueOf(LabyrinthController.tiles[row][index].getRotation()) + ".png");
				boardArray[row][index].setIcon(newImg);
			}
		}
		
		reloadPath();
		
		displayPlayers();
	}
	
	/*
	 * Author: Darren
	 * This method sets the positioning of the movement buttons and adds a mouse listener to it
	 */
	private void pushButtons() {

		// Vertical movement buttons
		for (int y = 0; y < 2; y++) {

			for (int x = 0; x < 3; x++) {

				buttonArray[x][y] = new JButton();
				ImageIcon arrow = new ImageIcon("images/Arrow" + y + ".png");
				buttonArray[x][y].setIcon(arrow);
				buttonArray[x][y].setDisabledIcon(new ImageIcon("images/Arrow" + y + "BW.png"));
				buttonArray[x][y].setActionCommand(String.valueOf(x) + " " + String.valueOf(y));
				buttonArray[x][y].setActionCommand(String.valueOf(x) + " " + String.valueOf(y));

				buttonArray[x][y].setBounds(192 + (192 * x), 768 * y ,96, 96);

				add(buttonArray[x][y]);

				buttonArray[x][y].addActionListener(this);

			}

		}


		// Horizontal movement buttons
		for (int x = 0; x < 3; x++) {

			for (int y = 2; y < 4; y++) {

				buttonArray[x][y] = new JButton();
				ImageIcon arrow = new ImageIcon("images/Arrow" + y + ".png");
				buttonArray[x][y].setIcon(arrow);
				buttonArray[x][y].setDisabledIcon(new ImageIcon("images/Arrow" + y + "BW.png"));
				buttonArray[x][y].setActionCommand(String.valueOf(x) + " " + String.valueOf(y));
				buttonArray[x][y].setActionCommand(String.valueOf(x) + " " + String.valueOf(y));

				buttonArray[x][y].setBounds(768 * (y-2), 192 + (192 * x), 96, 96);

				add(buttonArray[x][y]);

				buttonArray[x][y].addActionListener(this);

			}

		}	

	}
	
	/*
	 * Author: Darren
	 * Event Handler for tile-shifting buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ( (((JButton) e.getSource())).isEnabled() == true) {
			// Determine row and column of the opposite button
			int oppositeButtonX = 0;
			int oppositeButtonY = 0;
			
			//Determine the column of the inserted tile when the button is pressed
			if (((JComponent) e.getSource()).getX() == 0) {
				column = 0;
				oppositeButtonY = 3;
			}
			
			
			else if (((JComponent) e.getSource()).getX() == 192) {
				column = 1;
				oppositeButtonX = 0;
			}
			
			else if (((JComponent) e.getSource()).getX() == 384) {
				column = 3;
				oppositeButtonX = 1;
			}
			
			else if (((JComponent) e.getSource()).getX() == 576) {
				column = 5;
				oppositeButtonX = 2;
			}
			
			else {
				column = 6;
				oppositeButtonY = 2;
			}
			
			//Determine the row of the inserted tile when the button is pressed
			if (((JComponent) e.getSource()).getY() == 0) {
				row = 0;
				oppositeButtonY = 1;
			}
			
			else if (((JComponent) e.getSource()).getY() == 192) {
				row = 1;
				oppositeButtonX = 0;
			}
			
			else if (((JComponent) e.getSource()).getY() == 384) {
				row = 3;
				oppositeButtonX = 1;
			}
			
			else if (((JComponent) e.getSource()).getY() == 576) {
				row = 5;
				oppositeButtonX = 2;
			}
			
			else {
				row = 6;
				oppositeButtonY = 0;
			}
			
			//Disables the button opposite to the one that has been pressed
			disabledButton = buttonArray[oppositeButtonX][oppositeButtonY];
			
			//System.out.printf("(%d, %d)\n",row , column);
			
			insertTile(); // Added (Rowel)
		}
		
		
	}
	
	

	/*
	 * Author: Rowel
	 * This method inserts tiles
	 */
	public void insertTile() {
		Tile[][] tiles = LabyrinthController.tiles;
		Tile extraTile = LabyrinthController.extraTile;
		
		Tile placeholder;
		Tile insert;

		int oppositeTileCol = 0;
		int oppositeTileRow = 0;

		//
		if (row == 0 || row == 6) {
			oppositeTileCol = column;
			oppositeTileRow = 6 - row;
		}
		
		else if (column == 0 || column == 6) {
			oppositeTileCol = 6 - column;
			oppositeTileRow = row;
		}

		
		placeholder = new Tile(tiles[oppositeTileRow][oppositeTileCol].getName(), tiles[oppositeTileRow][oppositeTileCol].getType(), 
				tiles[oppositeTileRow][oppositeTileCol].getRotation(), tiles[oppositeTileRow][oppositeTileCol].isMoveable(),
				-1, -1);
		
		

		System.out.println("("+ oppositeTileRow +","+ oppositeTileCol +")");
		
		// Create new tile to be inserted
		insert = new Tile(extraTile.getName(), extraTile.getType(), extraTile.getRotation(), extraTile.isMoveable(),
				row, column);

		
		if(row == 0) { //insert from the top
			
			// Shifting of tiles
			for(int i = 6; i > 0;i--) {
				tiles[i][column] = new Tile( tiles[i - 1][column].getName(),tiles[i - 1][column].getType(),
						tiles[i - 1][column].getRotation(),tiles[i - 1][column].isMoveable(),i,column);
				matchCard(tiles[i][column]);	
			}
			
			tiles[row][column] = insert;
			matchCard(tiles[row][column]);
			
			extraTile = placeholder;
			matchCard(extraTile);

			reloadTiles(row, column);
		} 
		
		else if (row == 6){ //insert from the bottom

			for(int i = 0; i < 6;i++) {
				tiles[i][column] = new Tile( tiles[i + 1][column].getName(),tiles[i + 1][column].getType(),
						tiles[i + 1][column].getRotation(),tiles[i + 1][column].isMoveable(),i,column);
				matchCard(tiles[i][column]);
			}
			tiles[row][column] = insert;
			matchCard(tiles[row][column]);

			extraTile = placeholder;
			matchCard(extraTile);
			
			reloadTiles(row, column);

		} else if (column == 0) {//insert from the left

			for(int i = 6; i > 0;i--) {

				tiles[row][i] = new Tile(tiles[row][i-1].getName(),tiles[row][i-1].getType(),
						tiles[row][i-1].getRotation(),tiles[row][i-1].isMoveable(),row,i);
				matchCard(tiles[row][i]);
			}
			tiles[row][column] = insert;
			matchCard(tiles[row][column]);

			extraTile = placeholder;
			matchCard(extraTile);
			
			reloadTiles(row, column);

		} else if (column == 6) {//insert from the right

			for(int i = 0; i < 6;i++) {
				tiles[row][i] =new Tile(tiles[row][i+1].getName(),tiles[row][i+1].getType(),
						tiles[row][i+1].getRotation(),tiles[row][i+1].isMoveable(),row,i);
				matchCard(tiles[row][i]);
			}
			tiles[row][column] = insert;
			matchCard(tiles[row][column]);

			extraTile = placeholder;
			matchCard(extraTile);
			
			reloadTiles(row, column);
			
		}
		
		LabyrinthController.updateFreeTile(extraTile);
		
		for (int currentPlayer = 0; currentPlayer < LabyrinthController.players.length; currentPlayer++)
		
			onTile(LabyrinthController.players[currentPlayer].getRow(), LabyrinthController.players[currentPlayer].getColumn(), currentPlayer);
		
		LabyrinthController.updateTilePos();
		setEnabling(false);
		controlPanel.setEnabled(true);
		
		
		reloadPath();
		
	}
	
	/*
	 * This method updates the position that is stored in the treasure cards, when a tile has been inserted
	 */
	private void matchCard(Tile tile) {
		Card matchingCard = null;
		if (!(tile.getName().equals("L") || tile.getName().equals("I"))) {
			for (Card[] hand : LabyrinthController.cardHands) {
				for (Card card : hand) {
					if (card.getName().equals(tile.getName())) {
						matchingCard = card;
						break;
					}
				}
			}
		}
		
		if (!(matchingCard == null)) {
			matchingCard.setTile(tile);
		}
		
	}
	
	/*
	 * Authors: Darren and Rowel
	 * This method moves players with the tiles that are being shifted
	 */
	public void onTile(int row, int column, int currentPlayer) {
		
		// If any players are on the column that a tile is being inserted in
		if(LabyrinthController.players[currentPlayer].getColumn() == BoardPanel.column && LabyrinthController.players[currentPlayer].getColumn()%2 != 0) {
			
			// If the player is on the edge and tile pushes player off the board, move player to opposite side
			if(LabyrinthController.players[currentPlayer].getRow() == 6 && BoardPanel.row == 0) {
				LabyrinthController.players[currentPlayer].setRow(0);
			} else if(LabyrinthController.players[currentPlayer].getRow() == 0 && BoardPanel.row == 6) {
				LabyrinthController.players[currentPlayer].setRow(6);
				
			} else if(BoardPanel.row == 0) {
				LabyrinthController.players[currentPlayer].setRow(row + 1);
			} else if (BoardPanel.row == 6) {
				LabyrinthController.players[currentPlayer].setRow(row - 1);
			}
			this.currentPlayer = currentPlayer;
			
		}
		
		// If any players are on the row that a tile is being inserted in
		if(LabyrinthController.players[currentPlayer].getRow() == BoardPanel.row && LabyrinthController.players[currentPlayer].getRow()%2 != 0) {
			
			// If the player is on the edge and tile pushes player off the board, move player to opposite side
			if(LabyrinthController.players[currentPlayer].getColumn() == 6 && BoardPanel.column == 0) {
				LabyrinthController.players[currentPlayer].setColumn(0);
			} else if(LabyrinthController.players[currentPlayer].getColumn() == 0 && BoardPanel.column == 6) {
				LabyrinthController.players[currentPlayer].setColumn(6);
				
			} else if(BoardPanel.column == 0) {
				LabyrinthController.players[currentPlayer].setColumn(column + 1);
			} else if (BoardPanel.column == 6) {
				LabyrinthController.players[currentPlayer].setColumn(column - 1);
			}
			
			this.currentPlayer = currentPlayer;
			
		}
		
		displayPlayers();		// Updates player position on the game board
				
		this.currentPlayer = LabyrinthController.playerTurn;
		
	}
	
	/*
	 * Author: Rowel
	 * This method reloads the path 
	 */
	public void reloadPath() {

		// Get latest path based on new board and next player
		LabyrinthController.toMaze();

		for(int row = 0; row < 7; row++) {

			for(int column = 0; column < 7; column++) {

				if(LabyrinthController.possiblePath[row][column]) {
					//if the path is reachable, the path will be highlighted
					String filePath = ("images/" + LabyrinthController.tiles[row][column].getType() + "Path" +String.valueOf(LabyrinthController.tiles[row][column].getRotation()) + ".png");
					gameBoard.remove(boardArray[row][column]);
					ImageIcon newImg = new ImageIcon(filePath);
					paths[row][column].setIcon(newImg);
					paths[row][column].setBounds(column*96, row*96, 96, 96);

					gameBoard.add(paths[row][column]);
					gameBoard.add(boardArray[row][column]);
				} else {
					//if the tile is unreachable the path will be blank
					paths[row][column].setIcon(new ImageIcon("images/Blank.png"));
					paths[row][column].setBounds(column*96, row*96, 96, 96);
					gameBoard.add(paths[row][column]);
				}
			}
		}
		
		displayPlayers();

	}
	
	/*
	 * Author: Rowel
	 * This method clears the path 
	 */
	public void fill() {
		for(int row = 0; row < 7; row++) {
			for(int column = 0; column < 7; column++) {
				paths[row][column].setIcon(new ImageIcon("images/Blank.png"));
				paths[row][column].setBounds(column*96, row*96, 96, 96);
				gameBoard.add(paths[row][column]);
			}
		}
	}
	
	/*
	 * Author: Rowel
	 * This method initializes all the JLabels for the path label array
	 */
	public void initializePath(){
		for(int row = 0; row < 7; row++) {
			for(int column = 0; column < 7; column++) {
				paths[row][column] = new JLabel();
			}
		}
	}
	
	/*
	 * Author: Darren
	 * This method updates the player pieces as it is being animated across tiles
	 */
	public void paint(Graphics g) {
		
		displayPlayers();				//Display the other players
		
		super.paint(g);					//Paints the container
		
		// Depending on which is the current player, paint only that player
		
		animatePlayerMove(g);
		
		start = false;
		
		// Set the current player icon to dimension of 0x0 in order to hide it
		// Hides the current player icon while the paint method is animating the player image
		playerLabels[currentPlayer].setBounds(LabyrinthController.players[currentPlayer].getColumn() * 96, LabyrinthController.players[currentPlayer].getRow() * 96, 0, 0);
		
	}
	
	public void animatePlayerMove(Graphics g) {
		//Variables
		//Current row and position of the player before the move
		int currentRow;
		int currentColumn;
		
		int playerTurn = LabyrinthController.playerTurn;
		
		//Set the current row and position
		currentRow = LabyrinthController.players[playerTurn].getRow() * 96;
		currentColumn = LabyrinthController.players[playerTurn].getColumn() * 96;
		
		//If the player is moving horizontally
		if (LabyrinthController.scaleX) {
			
			currentRow = LabyrinthController.players[playerTurn].getRow() * 96;
			currentColumn = LabyrinthController.players[playerTurn].getColumn() * 96 - 96;
			
			//If the player is moving left towards 0
			if (LabyrinthController.back) {
				currentColumn = (LabyrinthController.players[playerTurn].getColumn() + 1) * 96;
				currentRow = (LabyrinthController.players[playerTurn].getRow()) * 96;
	
			}
			
		}
		
		//If the player is moving vertically
		else if (LabyrinthController.scaleY) {
			
			//Set the current row and position
			currentColumn = (LabyrinthController.players[playerTurn].getColumn()) * 96;
			currentRow = (LabyrinthController.players[playerTurn].getRow())  * 96 - 96;
		
			//If the player is moving up towards 0
			if (LabyrinthController.back) {
				currentColumn = (LabyrinthController.players[playerTurn].getColumn()) * 96;
				currentRow = (LabyrinthController.players[playerTurn].getRow() + 1) * 96;
			}
			
		}
		
		//Set the imageIcon to the current player
		image = new ImageIcon("images/" + LabyrinthController.players[playerTurn].getName() + ".png").getImage();
			
		Graphics g2D = (Graphics2D) g;
		
		g2D = g.create(96,96,672,672);
			
		g2D.drawImage(image, currentColumn + moveX, currentRow + moveY, null);
				
	}

	//Getter and setters
	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		
		//Validation code, once the current player reaches 4, set the current player back to 0
		if (currentPlayer == 4)
			this.currentPlayer = 0;
		
		else
			this.currentPlayer = currentPlayer;
		
	}	
	
}