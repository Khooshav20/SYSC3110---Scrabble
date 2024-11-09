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

public class ScrabbleController implements ActionListener{
    private ScrabbleGame gameModel; // Model representing the game state
    private View view;
    private int currentPlayer = 0; // Tracks the index of the current player

    /**
     * Constructor to initialize the controller with a game model and view.
     * @param model The Scrabble game model
     * @param view The Scrabble game view
     */
    public ScrabbleController(ScrabbleGame model, View view) {
        this.gameModel = model;
        this.view = view;
        initController();
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("PLAY")){
            if ()
        } else if (e.getActionCommand().equals("SWAP")){

        } else if (e.getActionCommand().equals("PASS")){

        } else {
            System.exit(0);
        }

    }

}
