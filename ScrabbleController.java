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
import java.io.IOException;

public class ScrabbleController{
    private LetterBag letterBag; // Model representing the game state
    private Player[] players; // Model representing the game state
    private Board board; // Model representing the game state
    private View view;
    private int currentPlayer; // Tracks the index of the current player
    private int turnsWithoutScore;

    /**
     * Constructor to initialize the controller with a game model and view.
     * @param model The Scrabble game model
     * @param numPlayers The number of players
     */
    public ScrabbleController (View view, int numPlayers) throws IOException {
        letterBag = new LetterBag();

        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++){
            players[i] = new Player();
            players[i].addTiles(letterBag.getTiles(7));
        }

        board = new Board();

        turnsWithoutScore = 0;
        currentPlayer = 0;

        this.view = view;
        
        view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize()));
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
        currentPlayer = (currentPlayer + 1) % players.length; // Cycle to the next player
    }

    public void play(String moveLetters, String word, int[] location){
        Tile[] moveTiles = players[currentPlayer].removeLetters(moveLetters);
        //not sure how this will work exactly
        if (board.isValidMove(moveTiles, word, location)){
            int score = board.playMove(moveTiles, word, location);
            players[currentPlayer].addScore(score);
            players[currentPlayer].addTiles(letterBag.getTiles(Math.min(moveLetters.length(), letterBag.getSize())));
            nextPlayer();
            view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize()));
            //popup: player x played "y" for z points
        } else {
            players[currentPlayer].addTiles(moveTiles);
            //popup: invalid move
        }
    }
}
