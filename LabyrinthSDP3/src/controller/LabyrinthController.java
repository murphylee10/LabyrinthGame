/*
 * Authors: Rowel, Murphy, Darren
 */
package controller;

import view.*;

import java.awt.Color;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.*;
import model.*;

// This method handles the game logic and interconnects the models with the GUI output
public class LabyrinthController {

	// Instance Variables 
	public static Player[] players = new Player[4];

	public static Stack<Card> cards = new Stack<Card>();			
	public static Stack<Tile> movableTiles = new Stack<Tile>(); 
	
	public static Tile[][] tiles = new Tile[7][7];                      
	public static Tile extraTile;
	
	public static Card[][] cardHands;
	public static Player currentPlayer;
	
	public static int playerTurn;
	
	public static String menuChoice;
	
	private static char[][] pathGrid = new char[21][21];
	public static boolean[][] possiblePath = new boolean[7][7];

	private static int moveNumber = 0;
	
	// Panels and other GUI components
	private static FreeTilePanel freeTilePanel;
	public static BoardPanel boardPanel;
	private static LabyrinthFrame frame;
	private static MainMenuFrame menuFrame;
	private static ControlPanel controlPanel;
	private static PlayerPanel playerPanel;
	private static SavePanel savePanel;
	
	//Animation instance variables
	private static int oneTile = 0;
	private static boolean left = false;
	private static boolean right = false;
	private static boolean up = false;
	private static boolean down = false;
	public static boolean back = false;
	public static boolean scaleY = false;
	public static boolean scaleX = false;

	/*
	 * Author: Rowel
	 */
	public LabyrinthController() {
		menuFrame = new MainMenuFrame();
		menuFrame.setVisible(true);
		
		MenuHandller menuHandller = new MenuHandller();
		MainMenuFrame.newGameButton.addActionListener(menuHandller);
		MainMenuFrame.loadGameButton.addActionListener(menuHandller);
		
		// While the menu frame is still displayed
		while (menuFrame.isDisplayable() == true) {
			// Prevent the controller from executing any further until a new game or loaded game has been chosen
			while (menuChoice == null) {
				System.out.print("");
			}
			
			// Create new game
			if (menuChoice.equals("new")) {
				createNewGame();
			}
			
			// Load existing game from txt file data
			else if (menuChoice.equals("load")) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int status = fileChooser.showOpenDialog(null);
				
				// If user didn't click the cancel button, read the specified contents of the file
				if (status != JFileChooser.CANCEL_OPTION) {
					menuFrame.dispose();
					File file = fileChooser.getSelectedFile();
					loadExistingGame(file);
					
				}
				else {
					menuChoice = null;
				}
				
			}
		}
	}
	
	
	// Utility Methods

	// Logic-related methods
	
	/*
	/*
	 * Author: Rowel
	 * Creates a new game
	 */
	public void createNewGame() {
		setupData();
		
		for(char[] c : pathGrid) {
			Arrays.fill(c,' ');
		}
		
		// Use JOptionPane to get the number of cards the user wants
		int numInput = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of Cards (1-6):"));
		if (numInput < 1 || numInput > 6) {
			JOptionPane.showMessageDialog(null, "Invalid Input Given - Number of Cards Set To 1");
			LabyrinthFrame.numCards = 1;
		}
		
		else {
			LabyrinthFrame.numCards = numInput;
		}
		
		// Initialize panels and frames
		displayGame();
		
		dealCards();
		
		boardPanel.initializePlayers();
		
		playerPanel = new PlayerPanel(players, LabyrinthFrame.numCards);   
		frame.add(playerPanel);
		frame.setVisible(true);
		
		playerTurn = 0;     // Index representing which players turn is active
		
		gameLoop();
		
	}
	
	/*
	 * Author: Murphy
	 * Loads an existing game by reading from a text file in specific format
	 * */
	public void loadExistingGame(File file) {
		Map<String, Tile> tileMap = new HashMap<String, Tile>();      // For associating a tile with its card
		
		try {
			StringTokenizer st;
			String tileName;
			String cardName;
			Scanner reader = new Scanner(file);
			
			// Loop through the 50 tiles specified in the text file
			for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles[row].length; col++) {
					st = new StringTokenizer(reader.nextLine());
					
					tileName = st.nextToken();
					tiles[row][col] = new Tile(tileName, st.nextToken(), Integer.parseInt(st.nextToken()), 
							Boolean.valueOf(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
					
					tileMap.put(tileName, tiles[row][col]);
				}
			}
			
			// Extra tile
			st = new StringTokenizer(reader.nextLine());
			tileName = st.nextToken();
			extraTile = new Tile(tileName, st.nextToken(), Integer.parseInt(st.nextToken()), 
					Boolean.valueOf(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			tileMap.put(tileName, extraTile);
			
			// Read card data
			st = new StringTokenizer(reader.nextLine());
			LabyrinthFrame.numCards = Integer.parseInt(st.nextToken());
			cardHands = new Card[4][LabyrinthFrame.numCards];
			
			// Set each value of the cardHands array
			for (int i = 0; i < cardHands.length; i++) {
				for (int j = 0; j < cardHands[i].length; j++) {
					st = new StringTokenizer(reader.nextLine());
					cardName = st.nextToken();
					cardHands[i][j] = new Card(cardName, tileMap.get(cardName));
					cardHands[i][j].setFound(Boolean.valueOf(st.nextToken()));
				}
			}
			
			
			// Set up players
			for (int index = 0; index < players.length; index++) {
				st = new StringTokenizer(reader.nextLine());
				players[index] = new Player(st.nextToken(), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), null, cardHands[index]);
			}
			players[0].setColor(Color.RED);
			players[1].setColor(Color.BLUE);
			players[2].setColor(Color.GREEN);
			players[3].setColor(Color.YELLOW);
			
			// Display JPanels and frames for the game
			displayGame();
			
			// Show players on the screen
			boardPanel.initializePlayers();
			
			// Get the current playerTurn index
			st = new StringTokenizer(reader.nextLine());
			playerTurn = Integer.parseInt(st.nextToken());
			
			// Display the players
			playerPanel = new PlayerPanel(players, LabyrinthFrame.numCards);   
			frame.add(playerPanel);
			frame.setVisible(true);
			
			// Get the scores of each player
			st = new StringTokenizer(reader.nextLine());
			for (int index = 0; index < PlayerPanel.scores.length; index++) {
				PlayerPanel.scores[index] = Integer.parseInt(st.nextToken());
			}
			playerPanel.displayText();   // For updated scores
			
			
			// Get boolean indicating which buttons are disabled
			st = new StringTokenizer(reader.nextLine());
			controlPanel.setEnabling(Boolean.valueOf(st.nextToken()));
			st = new StringTokenizer(reader.nextLine());
			boardPanel.setEnabling(Boolean.valueOf(st.nextToken()));
			
			
			// If there's one more line, get the x and y value of the single disabled button
			if (reader.hasNextLine()) {
				st = new StringTokenizer(reader.nextLine());
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				
				BoardPanel.disabledButton = BoardPanel.buttonArray[x][y];
				boardPanel.disableOppositeButton();
			}
			
			reader.close();
			
			// Execute the game
			gameLoop();
		} 
		
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error loading file data. New game will be started");
			e.printStackTrace();
			createNewGame();
		}
		
	}
	
	private void displayGame() {
		// Initialize panels and frames 
		boardPanel = new BoardPanel();
		controlPanel = new ControlPanel(); 
		freeTilePanel = new FreeTilePanel(extraTile);
		savePanel = new SavePanel();
		
		frame = new LabyrinthFrame(boardPanel, freeTilePanel, controlPanel, savePanel);
		
		setupButtons();
	}
	
	public void gameLoop() {
		// Game Loop
		while(!isGameFinished()){ 
			currentPlayer = players[playerTurn];
			controlPanel.setBackgroundColor(currentPlayer.getColor());
			
			if (BoardPanel.buttonEnabled == false) 
				controlPanel.setEnabling(true);
		}
		
		frame.promptResults();	
		
	}
	
	/*
	 * Author: Murphy
	 * This method uses the type and orientation of the tiles to determine if a move is valid
	 */
	private boolean isValidMove(Player player, String direction) {
		int currentRow = player.getRow();
		int currentCol = player.getColumn();
		
		boolean[] currentTilePaths = tiles[currentRow][currentCol].getOpenPaths();
		
		// Upward movement validation
		if (direction.equals("U") && currentRow != 0) {
			boolean[] nextTilePaths = tiles[currentRow - 1][currentCol].getOpenPaths();
			if (currentTilePaths[0] == true && nextTilePaths[2] == true)
				return true;
			return false;
		}
		
		// Downward movement validation
		else if (direction.equals("D") && currentRow != 6) {
			boolean[] nextTilePaths = tiles[currentRow + 1][currentCol].getOpenPaths();
			if (currentTilePaths[2] == true && nextTilePaths[0] == true)
				return true;
			return false;	
		}
		// Left movement validation
		else if (direction.equals("L") && currentCol != 0) {
			boolean[] nextTilePaths = tiles[currentRow][currentCol - 1].getOpenPaths();
			if (currentTilePaths[3] == true && nextTilePaths[1] == true)
				return true;
			return false;	
			
		}
		
		// Right movement validation
		else if (direction.equals("R") && currentCol != 6) {
			boolean[] nextTilePaths = tiles[currentRow][currentCol + 1].getOpenPaths();
			if (currentTilePaths[1] == true && nextTilePaths[3] == true)
				return true;
			return false;	
		}
		
		// Otherwise an invalid input was given - return false
		return false;
	}

	/*
	 * Author: Murphy
	 * This method moves a player to an adjacent tile
	 */
	private void movePlayer(int dx, int dy) {
		currentPlayer.setColumn(currentPlayer.getColumn() + dx);
		currentPlayer.setRow(currentPlayer.getRow() + dy);
		//boardPanel.displayPlayers();
	}

	/*
	 * Author: Rowel
	 * This method checks if any player is a winner. A dialouge will pop up when a winner is found
	 */
	private boolean isGameFinished() {
		for(Player p: players) {
			if(p.isWinner()) {
				String name = p.getName();
				JOptionPane.showMessageDialog(null, name + " is the winner!");
				return true;
			}
		}
		return false;	
	}
	
	/*
	 * Author: Murphy
	 * This method rotates the extra tile
	 */
	private void rotateExtraTile(int rotationFactor) {
		int newRotation = 0;
		
		// Get information about the current extra tile
		Tile currentExtraTile = freeTilePanel.getFreeTile();
		int currentRotation = currentExtraTile.getRotation();
		
		// Rotation factor of 1 indicates a clockwise rotation
		if (rotationFactor == 1) {
			newRotation = (currentRotation + 1) % 4;
			
		}
		// Rotation factor of -1 indicates a counter-clockwise rotation
		else {
			newRotation = (currentRotation == 0) ? 3 : currentRotation - 1;
			
		}
		
		Tile newExtraTile = new Tile(currentExtraTile.getName(), currentExtraTile.getType(), newRotation, 
				currentExtraTile.isMoveable(),currentExtraTile.getRow(), currentExtraTile.getCol());
		
		// Set the FreeTilePanel to hold the new tile
		extraTile = newExtraTile;
		freeTilePanel.setFreeTile(newExtraTile);
		freeTilePanel.displayTile();
		
	}

	// Data-related methods

	/*
	 * Author: Rowel and Murphy
	 * This method reads from the treasures CSV file to determine the position & order of the cards and tiles
	 */
	private void setupData() { 
		try {
			Scanner input = new Scanner(new File("files/treasures.csv"));
			input.useDelimiter(",");

			while(input.hasNext()) {
				//read the values in the file
				String name = input.next().replaceAll("\n","").replaceAll("\r","");
				String type = input.next().replaceAll("\n","").replaceAll("\r","");
				int rotation = input.nextInt();
				boolean isMoveable = input.nextBoolean();
				int row = input.nextInt();
				int column = input.nextInt();

				// Select random rotation value if it's currently random
				if (rotation == -1) 
					rotation = new Random().nextInt(4);


				Tile tile = new Tile(name, type, rotation, isMoveable, row, column);    // Change (Murphy)

				// If the item is not movable, add item to array
				if (!isMoveable) {
					tiles[row][column] = tile;
				}
				// Otherwise, add to movable tiles stack
				else {
					movableTiles.add(tile);   // Change (Murphy)
				}
				cards.add(new Card(name, tile));

			}

			// Shuffle treasure stack and create cards stack
			for (Card card : cards) {
				System.out.println(card);
			}

			// Add 22 I/L tiles
			String[] types = {"I", "L"};      // Added (Murphy)

			for (int i = 0; i < 22; i++) {     // Added (Murphy)
				// Use random selection to pick either an I or L piece
				Random random = new Random();
				String type = types[random.nextInt(2)];
				movableTiles.add(new Tile(type, type, new Random().nextInt(4), true, -1, -1));
			}

			Collections.shuffle(movableTiles);    // Added (Murphy)
			createTiles(movableTiles);      // Added (Murphy)

			input.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}	


	/*
	 * Author: Rowel
	 * This method gives a random card deck to each player
	 */
	private void dealCards() { 
		//Shuffle the cards 
		Collections.shuffle(cards);

		for (Card card : cards) {
			System.out.println(card);
		}

		// A card holder
		cardHands = new Card[4][LabyrinthFrame.numCards];

		// Cycle through each player giving each the selected number of cards
		for(int i = 0; i < 4; i++) {


			for(int j = 0; j < LabyrinthFrame.numCards;j++) {
				cardHands[i][j] = cards.pop();
			}


			// Initialize the player and give the player a card hand
			players[i] = new Player(null, 0, 0, null, cardHands[i]);

		}
		
		// Setup the player's position and colors
		players[0].setName("Red");
		players[0].setRow(0);
		players[0].setColumn(0);
		players[0].setColor(Color.RED);

		players[1].setName("Blue");
		players[1].setRow(0);
		players[1].setColumn(6);
		players[1].setColor(Color.BLUE);

		players[2].setName("Green");
		players[2].setRow(6);
		players[2].setColumn(6);
		players[2].setColor(Color.GREEN);

		players[3].setName("Yellow");
		players[3].setRow(6);
		players[3].setColumn(0);
		players[3].setColor(Color.YELLOW);

	}

	/*
	 * Author: Murphy
	 * This method completes the initial tile array by adding movable tiles from the random stack
	 */
	private void createTiles(Stack<Tile> movableTiles) {     
		// Loop through the rows and columns, creating a new tile in the array
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				// Hard-Coding corner pieces
				if (row == 0 && col == 0) {
					tiles[row][col] = new Tile("RedStart", "L", 1, false, row, col);
					continue;
				}
				if (row == 6 && col == 0) {
					tiles[row][col] = new Tile("YellowStart", "L", 0, false, row, col);
					continue;
				}
				if (row == 0 && col == 6) {
					tiles[row][col] = new Tile("BlueStart", "L", 2, false, row, col);
					continue;
				}
				if (row == 6 && col == 6) {
					tiles[row][col] = new Tile("GreenStart", "L", 3, false, row, col);
					continue;
				}

				// Every other row has 3 gaps
				if (row % 2 == 0) {
					// For tiles that are movable - add from stack
					if (col % 2 == 1) {
						tiles[row][col] = movableTiles.pop();
						tiles[row][col].setRow(row);
						tiles[row][col].setCol(col);
					}
				}

				// The other row is completely empty
				else {
					tiles[row][col] = movableTiles.pop();
					tiles[row][col].setRow(row);
					tiles[row][col].setCol(col);					
				}
			}
		}
		extraTile = movableTiles.pop();    
		System.out.println("Extra Tile:" + extraTile);

	}
	
	public void setupButtons() {
		// Register event handler for control panel buttons
		MovementHandler movementHandler = new MovementHandler();    // ActionListener inner-class
		SaveDataHandler saveDataHandler = new SaveDataHandler();
		
		ControlPanel.downButton.addActionListener(movementHandler);
		ControlPanel.upButton.addActionListener(movementHandler);
		ControlPanel.rightButton.addActionListener(movementHandler);
		ControlPanel.leftButton.addActionListener(movementHandler);
		ControlPanel.confirmButton.addActionListener(movementHandler);
		
		FreeTilePanel.clockwiseButton.addActionListener(movementHandler);
		FreeTilePanel.counterClockwiseButton.addActionListener(movementHandler);
		SavePanel.saveButton.addActionListener(saveDataHandler);
		
	}


	
	public static void updateFreeTile(Tile newTile) {
		freeTilePanel.setFreeTile(newTile);
		extraTile = newTile;
		freeTilePanel.displayTile();
	}
	
	/*
	 * Author: Rowel
	 * This method checks if the current player landed on their treasure
	 */
	public boolean foundTreasure() {
		updateTilePos();
		for(int i = 0; i < LabyrinthFrame.numCards; i++) {
			if(currentPlayer.getHand()[i].getTile().getRow() == currentPlayer.getRow() && currentPlayer.getHand()[i].getTile().getCol() == currentPlayer.getColumn() 
					&& currentPlayer.getHand()[i].isFound() == false) {
				currentPlayer.getHand()[i].setFound(true);
				PlayerPanel.scores[playerTurn]++;
				playerPanel.displayText();
				return true;
			}
		}
		return false;
	}
	/*
	 * Author: Rowel
	 * This method updates the position values for all the tiles in the tile array
	 */
	
	public static void updateTilePos() {
		for(int row = 0; row < 7; row++) {
			for(int column = 0; column < 7; column++) {
				tiles[row][column].setCol(column);
				tiles[row][column].setRow(row);
			}
		}
	}
	
	// This method saves the results of the previous games to a text file
	public static void saveResults() {
		// https://mkyong.com/java/how-to-get-current-timestamps-in-java/
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    
		String filePath = "files/GameLog.txt";
		
		try {
			FileWriter file = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(file);
			
			bw.append(String.format("%s%n", String.valueOf(timestamp)));
			bw.append(String.format("Number of Cards Per Player: %d%n", LabyrinthFrame.numCards));
			bw.append(String.format("Red Player's Score: %d%n", PlayerPanel.scores[0]));
			bw.append(String.format("Blue Player's Score: %d%n", PlayerPanel.scores[1]));
			bw.append(String.format("Green Player's Score: %d%n", PlayerPanel.scores[2]));
			bw.append(String.format("Yellow Player's Score: %d%n%n", PlayerPanel.scores[3]));
			
			bw.append(String.format("Winner: %s%n%n", currentPlayer.getName()));
			
			bw.close();
		}
		
		catch (IOException exception) {
			System.out.println("File not found");
		}
	}
	
	/*
	 * Author: Murphy and Darren (added for animation)

	 * Private inner class for event-handling the movement buttons
	 */

	private class MovementHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// Movement buttons
			if (event.getActionCommand().equals("down") && isValidMove(currentPlayer, "D")) {
				movePlayer(0, 1);
				System.out.println("D");
				down = true;
				animation();
			}
			
			else if (event.getActionCommand().equals("up") && isValidMove(currentPlayer, "U")) {
				movePlayer(0, -1);
				System.out.println("U");
				up = true;
				animation();
				System.out.println(boardPanel.getCurrentPlayer());
			}
			
			else if (event.getActionCommand().equals("right") && isValidMove(currentPlayer, "R")) {
				movePlayer(1, 0);
				System.out.println("R");
				right = true;
				animation();
			}
			
			else if (event.getActionCommand().equals("left") && isValidMove(currentPlayer, "L")) {
				movePlayer(-1, 0);
				System.out.println("L");
				left = true;
				animation();
			}
			
			// Movable tile rotation
			else if (event.getActionCommand().equals("clockwise")) {
				rotateExtraTile(1);
				
			}
			
			else if (event.getActionCommand().equals("counterclockwise")) {
				rotateExtraTile(-1);
			}
			
			// Confirm a move
			else if (event.getActionCommand().equals("confirm")) {
				if (foundTreasure() == true) 
					playerPanel.displayCards();
				playerTurn = (playerTurn + 1) % 4;
				boardPanel.setEnabling(true);
				boardPanel.disableOppositeButton();
				
				ControlPanel.confirmButton.setBackground(currentPlayer.getColor());
				
				controlPanel.setEnabling(false);
				
				boardPanel.fill();
				
				boardPanel.repaint();
				boardPanel.displayPlayers();
				
				boardPanel.setCurrentPlayer(boardPanel.getCurrentPlayer() + 1);
				
			}
		}
	}
	
	/*
	 * Author: Murphy
	 * This class handles the saving of game data upon input from the save button
	 */
	private class SaveDataHandler implements ActionListener {

		/*
		 * Author: Murphy
		 * Executes when save button is pressed
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// Prompt the user for a file name
			String fileName = JOptionPane.showInputDialog("Enter File Name: (ending with '.txt')");
			
			// If the user gave a file name, prompt for directory
			if (!(fileName == null)) {
				String directory = getDirectory();
				
				// Save the game to the text file in the specified path
				if (directory != null) {
					String absolutePath = directory + "/" + fileName;
					saveGame(absolutePath);
				}	
			}
		}
		
		/*
		 * Author: Murphy
		 * This method uses a JFileChooser to prompt the user for a directory
		 * 
		 */
		private String getDirectory() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int result = fileChooser.showOpenDialog(null);
			
			// If user clicked Cancel button, return
			if (result == JFileChooser.CANCEL_OPTION) {
				System.out.println("Cancelled");
				return null;
			}
			
			// Return the path to the directory
			return fileChooser.getSelectedFile().getAbsolutePath();
		}
		
		/*
		 * Author: Murphy
		 * This method saves the data of the current game to a text file
		 */
		private void saveGame(String absPath) {
			try {
				Formatter formatter = new Formatter(absPath);
				
				// Save all tile data
				for (int row = 0; row < tiles.length; row++) {
					for (int col = 0; col < tiles[row].length; col++) {
						Tile writeTile = tiles[row][col];
						formatter.format("%s %s %d %b %d %d%n", writeTile.getName(), writeTile.getType(), writeTile.getRotation(), 
								writeTile.isMoveable(), writeTile.getRow(), writeTile.getCol());
					}
				}
				
				// Extra tile
				formatter.format("%s %s %d %b %d %d%n", extraTile.getName(), extraTile.getType(), extraTile.getRotation(), 
						extraTile.isMoveable(), extraTile.getRow(), extraTile.getCol());
				
				// Number of cards that each player has
				formatter.format("%d%n", LabyrinthFrame.numCards);
				
				// Save all the card data
				for (int handIndex = 0; handIndex < cardHands.length; handIndex++) {
					for (int cardIndex = 0; cardIndex < cardHands[handIndex].length; cardIndex++) {
						Card writeCard = cardHands[handIndex][cardIndex];
						
						formatter.format("%s %b%n", writeCard.getName(), writeCard.isFound());
					}
				}
				
				// Player data
				for (Player writePlayer : players) {
					formatter.format("%s %d %d%n", writePlayer.getName(), writePlayer.getRow(), writePlayer.getColumn());
				}
				
				// Current player index
				formatter.format("%d%n", playerTurn);
				
				// Scores of each player
				formatter.format("%d %d %d %d%n", PlayerPanel.scores[0], PlayerPanel.scores[1], PlayerPanel.scores[2], PlayerPanel.scores[3]);
				
				// Boolean that indicates if control buttons are enabled
				formatter.format("%b%n", ControlPanel.confirmButton.isEnabled());
				
				// Boolean that indicates if tile shifting buttons are enabled
				formatter.format("%b%n", BoardPanel.buttonEnabled);
					
				// X and Y value of the disabled button (if button is enabled)
				formatter.format("%s", BoardPanel.disabledButton.getActionCommand());
				
				formatter.close();
			} 
			
			catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Error Saving game. Try Again.");
			}
		}
	}
	
	/*
	 * Author: Murphy
	 * This class handles the events passed by the menu buttons
	 */
	private class MenuHandller implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand().equals("new")) {
				menuFrame.dispose();
				menuChoice = "new";
			}
			
			else if (event.getActionCommand().equals("load")) {
				menuChoice = "load";
			}
		}
		
	}
	
	/*
	 * Author: Rowel
	 * This method tests possible directions
	 * it is a part if the recursive maze traversal algorithm
	 */
	public static boolean nextMove(int row, int column) {
		int dRow = 0;
		int dCol = 0;

		for(int count = 0; count < 4; count++) {
			switch(count) {
			case 0:
				dRow = 0;
				dCol = 1;
				break;
			case 1:
				dRow = -1;
				dCol = 0;
				break;
			case 2:
				dRow = 1;
				dCol = 0;
				break;
			case 3:
				dRow = 0;
				dCol = -1;
			}

			if(validMove(row+dRow, column+dCol)) {
				if(boardTraversal(row+dRow, column+dCol)) {
					return true;
				}
			}
		}
		return false; 
	}
	
	/*
	 * Author: Rowel
	 * This method checks if the inputed direcrion is a valid path
	 * it is a part if the recursive maze traversal algorithm
	 */
	private static boolean validMove(int row, int column) {
		return ((row >= 0 && (row < pathGrid.length) && (column >=0) && (column < pathGrid[row].length) && (pathGrid[row][column] == '.')));
	}
	
	/*
	 * Author: Rowel
	 * This method finds all possible paths on the generated board
	 * it is a part if the recursive maze traversal algorithm
	 */
	public static boolean boardTraversal(int row, int column) {
		//takes first step then prints the array with first step
		pathGrid[row][column] = '+';
		moveNumber++;

		//if returned to starting position
		if(row == players[playerTurn].getRow() && column == players[playerTurn].getColumn() && moveNumber > 1) {
			return false;
		} else {			
			if(nextMove(row, column) == false) {
				//sets the dead end as "+"
				pathGrid[row][column] = '+';

				//slow down execution

				//unable to make a move
				return false;
			}//end of if

			//was able to make a move
			return true;

		}//end of else 


	}

	/*
	 * Author: Rowel
	 * This method prints the maze array
	 */
	private static void printBoard() {
		for(char[] row : pathGrid) {
			//go trhough the cells in each row
			for(char cell : row) {
				System.out.print(" "+cell);
			}
			System.out.println();
		}
		System.out.println();
	}

	/*
	 * Author: Rowel
	 * This method facilitates the conversion of the tile array into a "Maze"
	 * so that it can be put into the maze traversal algorithm
	 */
	public static void toMaze() {
		moveNumber = 0;
		fill();
		holePunch();
		breakWalls();
		createBorder();
		boardTraversal((players[playerTurn].getRow())*3+1,((players[playerTurn].getColumn()*3+1)));
		toPathArray();
		printBoard();
		printBooleanArray();
	}
	
	/*
	 * Author: Rowel
	 * This method fills the maze array with #'s
	 * it is a part if the to maze conversion algorithm
	 */
	private static void fill() {
		for(int row = 0; row < 21; row++) {
			for(int col = 0; col < 21; col++ ) {
				pathGrid[row][col] = '#';
			}
		}
	}

	/*
	 * Author: Rowel
	 * This method creates holes in the maze array
	 * it is a part if the to maze conversion algorithm
	 */
	private static void holePunch() {
		for(int row = 1; row < 20; row+=3) {
			for(int col = 1; col < 20; col+=3) {
				pathGrid[row][col] = '.';
			}
		}
	}

	/*
	 * Author: Rowel
	 * This method breaks walls based on the tile's roation, position and type 
	 * it is a part if the to maze conversion algorithm
	 */
	private static void breakWalls() {
		for(int row = 1; row < 20; row += 3) {
			for(int col = 1; col < 20; col +=3) {
				boolean[] dir = tiles[(row-1)/3][(col-1)/3].getOpenPaths();
				if(dir[0]) {
					pathGrid[row-1][col] = '.';
				}
				if(dir[1]) {
					pathGrid[row][col+1] = '.';
				}
				if(dir[2]) {
					pathGrid[row+1][col] = '.';
				}
				if(dir[3]) {
					pathGrid[row][col-1] = '.';
				}
			}
		}
	}

	/*
	 * Author: Rowel
	 * This method creates a border so that the maze traversal algorithm does not leave the board
	 * it is a part if the to maze conversion algorithm
	 */
	private static void createBorder() {
		for(int i = 0; i < 21; i++) {
			pathGrid[0][i] = '#';
		}
		for(int i = 0; i < 21; i++) {
			pathGrid[20][i] = '#';
		}
		for(int i = 0; i < 21; i++) {
			pathGrid[i][0] = '#';
		}
		for(int i = 0; i < 21; i++) {
			pathGrid[i][20] = '#';
		}		

	}

	/*
	 * Author: Rowel
	 * This method converts the maze array into a usable format. 
	 * this shows which tiles are accessable and need to be highlighted.
	 */
	public static void toPathArray() {
		for(int row = 1; row < 20; row+=3) {
			for(int col = 1; col < 20; col+=3) {
				if(pathGrid[row][col] == '+') {
					 possiblePath[(row-1)/3][(col-1)/3] = true;
				} else {
					 possiblePath[(row-1)/3][(col-1)/3] = false;
				}
			}
		}
		
	}
	
	/*
	 * Author: Rowel
	 * prints the possible path array
	 */
	public static void printBooleanArray() {
		for(int row = 0; row < 7; row++) {
			for(int col = 0; col < 7; col++) {
				if(possiblePath[row][col]) {
					System.out.print("+ ");
				}
				if(!possiblePath[row][col]) {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/*
	 * Author: Darren
	 * This method animates the current player piece when they move
	 * Extra Comments: Movement control buttons are disabled during movement animation
	 * and re-enabled after
	 */
	public static void animation() {
		
		//Disables the movement button
		controlPanel.setEnabling(false);
		
		//Increments every time the player piece moves 2 units
		oneTile = 0;
		
		//Timer for the animation
		Timer timer = new Timer(5, null);
		
		//Start the timer
		timer.start();
		
		boardPanel.setMoveY(0);
		boardPanel.setMoveX(0);
		
		timer.addActionListener(new ActionListener() {
			
			//Performs action after player pressed a movement button
			public void actionPerformed(ActionEvent e) {
				
				//Move right
				if (right) {
					back = false;
					scaleX = true;
					scaleY = false;
					boardPanel.setMoveX(boardPanel.getMoveX() + 2);
					oneTile++;
					
				}
				
				//Move left
				else if (left) {
					back = true;
					scaleX = true;
					scaleY = false;
					boardPanel.setMoveX(boardPanel.getMoveX() - 2);
					oneTile++;
					
				}
				
				//Move up
				else if (up) {
					back = true;
					scaleX = false;
					scaleY = true;
					boardPanel.setMoveY(boardPanel.getMoveY() - 2);
					oneTile++;
					
				}
				
				//Move down
				else if (down) {
					back = false;
					scaleX = false;
					scaleY = true;
					boardPanel.setMoveY(boardPanel.getMoveY() + 2);
					oneTile++;
					
				}
				
				//Repaints the player
				boardPanel.repaint();
				
				//If the player has traveled 1 tile
				if (oneTile == 48) {
					timer.stop();			//Stop timer
					right = false;
					left = false;
					up = false;
					down = false;
					controlPanel.setEnabling(true);			//Enable the movement buttons
				}
				
			}
				
		});
		
		boardPanel.setMoveY(0);
		boardPanel.setMoveX(0);
		back = false;
		scaleX = false;
		scaleY = false;
			
	}
	
}