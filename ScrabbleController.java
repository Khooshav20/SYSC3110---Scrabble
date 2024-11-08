/**
 * 
 * ScrabbleController is responsible for managing interactions between the Scrabble game model and view.
 * It processes user actions, updates the view based on the game state, and controls player turn rotation.
 * 
 * @author Khooshav Bundhoo (101132063)
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleController {
    private ScrabbleGame gameModel; // Model representing the game state
    private View gameView; // View representing the GUI
    private int currentPlayer = 0; // Tracks the index of the current player

    /**
     * Constructor to initialize the controller with a game model and view.
     * @param model The Scrabble game model
     * @param view The view to display the game state
     */
    public ScrabbleController(ScrabbleGame model, View view) {
        this.gameModel = model;
        this.gameView = view;

        initController();
    }

    /**
     * Initializes the controller, setting up listeners for user actions in the GUI.
     */
    private void initController() {
        updateView(); // Update the view to reflect the initial game state

        // Setup event listeners for Play, Exchange, and Pass actions
        gameView.getPlayButton().addActionListener(new PlayButtonListener());
        gameView.getExchangeButton().addActionListener(new ExchangeButtonListener());
        gameView.getPassButton().addActionListener(new PassButtonListener());
    }

    /**
     * Updates the view with the current game state, including board, scores, and tile rack.
     */
    private void updateView() {
        gameView.updateBoard(gameModel.getBoard().getBoard()); // Display the current board
        gameView.updateLeaderboard(getPlayerScores()); // Display the leaderboard
        gameView.updateTileBagCount(gameModel.getLetterBag().getSize()); // Display the count of remaining tiles
        gameView.setRack(gameModel.getPlayers()[currentPlayer].getRack()); // Display the current player's rack
    }

    /**
     * Retrieves player scores in a format suitable for updating the leaderboard.
     * @return Array of formatted strings representing player scores.
     */
    private String[] getPlayerScores() {
        Player[] players = gameModel.getPlayers();
        String[] scores = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            scores[i] = "Player " + (i + 1) + ": " + players[i].getScore() + " pts"; // Format each player's score
        }
        return scores;
    }

    /**
     * Advances to the next player's turn.
     */
    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % gameModel.getPlayers().length; // Cycle to the next player
        updateView(); // Update the view for the new current player
    }

    /**
     * Listener for the "Play" button, processing player moves.
     */
    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String move = gameView.getMoveInput(); // Get the move input from the user
            if (gameModel.isValidMove(move, currentPlayer)) { // Check if the move is valid
                int score = gameModel.playMove(move, currentPlayer); // Play the move and get the score
                gameModel.getPlayers()[currentPlayer].addScore(score); // Add score to the current player
                gameView.displayMessage("Played successfully for " + score + " points."); // Display success message
                nextPlayer(); // Advance to the next player
            } else {
                gameView.displayMessage("Invalid move. Try again."); // Display error message for invalid move
            }
        }
    }

    /**
     * Listener for the "Pass" button, allowing a player to pass their turn.
     */
    private class PassButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameView.displayMessage("Player passed."); // Notify that the player passed
            nextPlayer(); // Advance to the next player
        }
    }
}
