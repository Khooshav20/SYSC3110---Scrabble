# SYSC3110---Scrabble
Group 17
@authors:
- Marc Fernandes
- Khooshav Bundhoo
- Lucas Warburton
- Alexander Gardiner 

Scrabble Game

Milestone 1

This is a simplified version of the classic Scrabble game implemented for Milestone 1.The game is played via the console, and words can be placed on a 15x15 Scrabble board. The board supports word placement both horizontally and vertically, and the moves are notated in standard Scrabble format (e.g., "WORD xy +score").

Features:
- Turn-based gameplay: Players take turns to place tiles on the board.
- Scoring system: Follows the traditional Scrabble scoring rules.
- Player management: Handles multiple players and their respective turns.
- Random letter distribution: Uses a LetterBag class to ensure random tile distribution.
- Tile placement and validation: Ensures tiles can only be placed according to Scrabble's rules.

File Descriptions
- BlankSquare.java: Represents a blank square on the Scrabble board.
- Board.java: Manages the Scrabble board and handles tile placement and validation.
- LetterBag.java: Manages the bag of Scrabble tiles, ensuring randomness when players draw tiles.
- Player.java: Represents a player in the game, including their score, tiles, and turn logic.
- ScrabbleGame.java: Main game logic, player turns, and interactions between the board and players.
- Square.java: Represents a square on the Scrabble board, potentially containing a tile.
- Tile.java: Represents an individual Scrabble tile, including its letter and point value.

#Responsibilities:
- Marc Fernandes <br>
•Tile.java <br>
•LetterBag.java <br>
•Square.java <br>

- Khooshav Bundhoo <br>
 •Board.java <br>
 •README.md <br>
 •Documentation <br>

- Lucas Warburton <br>
 •ScrabbleGame.java <br>
  
- Alexander Gardiner <br>
 •Player.java <br>


Milestone 2

This milestone enhances our initial console-based Scrabble game by introducing a graphical user interface (GUI) using Java Swing. The game is now presented in a JFrame, with players able to interact via mouse clicks. To move a tile, click on the tile and then click on its intended destination. This version also includes unit tests to ensure reliable functionality, particularly focusing on word placement and scoring.

Features:
- GUI-Based Gameplay: The Scrabble board is rendered in a JFrame, providing an intuitive, visually engaging way to play the game.
- Mouse Interactions: Players can now place tiles on the board using mouse clicks, streamlining gameplay.

File Descriptions

- BlankSquare.java: Represents a blank square on the Scrabble board.
- Board.java: Manages the Scrabble board and handles tile placement and validation.
- LetterBag.java: Manages the bag of Scrabble tiles, ensuring randomness when players draw tiles.
- Player.java: Represents a player in the game, including their score, tiles, and turn logic.
- ScrabbleGame.java: Main game logic, player turns, and interactions between the board and players.
- Square.java: Represents a square on the Scrabble board, potentially containing a tile.
- Tile.java: Represents an individual Scrabble tile, including its letter and point value.
- TileTest.java: Unit tests for Tile.java, verifying tile properties and behavior.
- BoardTest.java: Unit tests for Board.java, covering various board management scenarios.
- LetterBagTest.java: Unit tests for LetterBag.java, verifying randomness and tile drawing functionality.
- BlankSquareTest.java: Unit tests for BlankSquare.java to ensure blank square functionality is working correctly.
- dictionary.txt: A list of valid words for the game, used to validate player moves.
- tiles.txt: Stores information about tile distribution and point values, defining the available tiles in the game.
- ScrabbleController.java: Controls user interactions, game events, and manages turn-based logic within the GUI, linking the model and view.
- View.java: Handles the graphical representation of the Scrabble board and user interface, interacting with the controller to update the display based on game events.


#Responsibilities:
- Marc Fernandes <br>
 •View.java <br>

- Khooshav Bundhoo <br>
 •ScrabbleController.java <br>
 •README.md <br>
 •Documentation <br>

- Lucas Warburton <br>
 •View.java <br>
 •ScrabbleController.java with View.java integration <br>
 
- Alexander Gardiner <br>
 •Test cases <br>


 Milestone 3

 This milestone adds important gameplay functionality in the existence of premium tiles (2x and 3x multipliers for both word and letter), and blank tiles which can be assigned any letter. It also includes the option to add AI players to a game, which play as many tiles per move as possible. Unit tests are included for all additional gameplay mechanics.

 Features:
 - Blank Tiles
 - Premium Tiles
 - AI Player

 File Descriptions:

 - AIPlayer.java: An extension of Player which adds the ability to generate a move automatically.
 - PremiumTile.java: A tile which multiplies the score of either a whole word or an individual letter.
 - BlankTile.java: A tile with no letter or score which can be assigned a letter.


 #Responsibilities:
 - Marc Fernandes <br>
 •AI Player <br>
 •Blank Tiles <br>

 - Khooshav Bundhoo <br>
 •AI Player <br>
 •Documentation <br>

 - Lucas Warburton <br>
 •Blank Tiles <br>
 •Premium Tiles <br>
 •README <br>

 - Alexander Gardiner <br>
 •Blank Tile Tests <br>
 •Premium Tile Tests <br>


Milestone 4

This milestone introduces advanced features that significantly enhance the functionality and user experience 
of the Scrabble game. These features include a multiple-level undo/redo system, save/load functionality using 
Java Serialization, and the ability to customize the Scrabble board with alternate placements for premium squares. 
The customization feature supports configuration through XML or JSON formats, allowing users to create their own unique game boards.

Features:

- Multiple-Level Undo/Redo:
	•	Allows players to revert their last moves or redo undone moves, supporting multiple levels of undo/redo actions.
	•	Ensures robust state management to maintain the integrity of the game.

- Save/Load Functionality:
	•	Enables players to save their game progress at any point and reload it later.
	•	Utilizes Java Serialization to persist and retrieve the game’s state, including the board, players, and tile distribution.

- Custom Boards with Alternate Premium Square Placements:
	•	Supports customization of premium square placement (e.g., 2x, 3x letter/word score tiles).
	•	Players can define custom board configurations in XML or JSON format.
	•	Enhances replayability by allowing unique game setups.
    •	Enter "specialBoard.txt" into the prompt to view a preexisting special board.


 #Responsibilities:
 - Marc Fernandes <br>
 • Undo/Redo <br>
 • Custom Board <br>

 - Khooshav Bundhoo <br>
 • UML <br>
 • Readme <br>
 • Documentation <br>

 - Lucas Warburton <br>
 • Save/Load <br>
 • Custom Board <br>

 - Alexander Gardiner <br>
 • Undo/Redo
 • Save/Load <br>


 Bonus Milestone

This milestone adds an optional timed mode to the Scrabble game. 

 Features:
 - Timer

 Contributors:
  • Alexander Gardiner <br>
  • Lucas Warburton <br>
  • Marc-Antony Fernandes <br>
  • Khooshav Bundhoo <br>

How to run?

 •Install IntelliJ IDEA <br>
 •Open IntelliJ IDEA and choose Get from Version control <br>
 •In the URL section, paste this link: https://github.com/Khooshav20/SYSC3110---Scrabble.git <br>
 •Choose your directory and then click on clone <br>
 •Run the main from ScrabbleGame.java <br>

Testing

Unit tests can be run through your IDE’s testing suite, targeting the accuracy of word placement and scoring functionality.

This README offers a comprehensive overview of the new features and guides users on setup, gameplay, and testing for Milestone 4.
 
Once done, You are good to go!! Have a great game!
