/*
 * Name(s): Murphy (33%), Rowel (33%), Darren (33%)
 * Roles:
 *  - Murphy: GUI implementation of Labyrinth Frame, Control Panel, Player Panel + Game Loop Logic (movenment of players & switching between player turns), rotation of the extra tile
 *  - Rowel: Player and Card object models, Game Loop logic, tile-insertion of the board panel, determining found treasures, setting up of tile and card data from CSV, pathfinding.
 *  - Darren: GUI implementations of Board Panel and FreeTilePanel & their respective event handlers, all graphics & overall aesthetic additions, animation.
 * Date of Submission: November 28, 2021
 * Course: ICS4U1-01
 * Teacher: Mr.Fernandes
 * Title: The Amazing Labyrinth (4-player game)
 * Description: Our product is a 4-player game based on the popular board game Labyrinth, which was created using Java. This game will feature a game
 *				board that contains 4 players and tiles, featuring featuring different treasures that must be attained. 
 *				The players are given treasure cards, and the objective of each player is to land on the tile that has their treasure. 
 *				Movement throughout the board is achieved by inserting a tile into movable rows & columns, or by moving the player through open paths. 
 *				The winner of the game is the first player to obtain all of their treasures.
 * Major Skills: Object oriented programming (classes, methods, fields, constructor methods, getter & setters, utility methods), stacks, hashmaps, 
 * 				access levels, conditional loops, for-loops, GUI, swing, JFrame, JButtons, boolean variables,
 * 				image icons, action listeners, importing files, math calculations
 * Added features: 
 *  - Pathfinding visualization (Rowel)
 *  - Saving and reloading of data (Murphy)
 *  - Animation of player movements (Darren)
 * Area of concerns:
 *  - Requires java.desktop for GUI components
 *  - A monitor that supports 1920x1080p is recommended to support the frame size
 *  - Major use of swing (javax) and AWT components
 * External Resources:
 *    - https://mkyong.com/java/how-to-get-current-timestamps-in-java/
 */

package application;

import controller.*;

public class LabyrinthApplication {

	public static void main(String[] args) {

		new LabyrinthController();
		
	}

}