/**
 * 
 * ScrabbleController is responsible for managing interactions between the Scrabble game model and view.
 * It processes user actions, updates the view based on the game state, and controls player turn rotation.
 * 
 * @author Khooshav Bundhoo (101132063)
 * @author Lucas Warburton (101276823)
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
     * @param view The Scrabble View
     * @param numPlayers The number of players
     * @throws IOException If it fails to read dictionary.txt or files.txt
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
        
        view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
    }

    /**
     * Advances to the next player's turn.
     */
    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.length; // Cycle to the next player
    }

    /**
     * Plays a move on the model and prompts the view with updated game state.
     * 
     * @param moveLetters the new letters placed in the move, in order
     * @param word the word being played
     * @param location the location of the move (including direction)
     * @return whether the move was successful
     */
    public boolean play(String moveLetters, String word, int[] location){
        Tile[] moveTiles = players[currentPlayer].removeLetters(moveLetters);
        //not sure how this will work exactly
        if (board.isValidMove(moveTiles, word, location)){
            int score = board.playMove(moveTiles, word, location);
            players[currentPlayer].addScore(score);
            if (letterBag.getSize() > 0) players[currentPlayer].addTiles(letterBag.getTiles(Math.min(moveLetters.length(), letterBag.getSize())));
            JOptionPane.showMessageDialog(view, word + " played for " + score + " points.");
            nextPlayer();
            view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
            turnsWithoutScore = 0;
            if (players[currentPlayer-1 >= 0 ? currentPlayer-1: players.length-1].getNumTiles() == 0) view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this)); //end game
            return true;
        } else {
            players[currentPlayer].addTiles(moveTiles);
            return false;
        }
    }

    /**
     * Passes on the model and prompts the view with updated game state.
     */
    public void pass(){
        nextPlayer();
        turnsWithoutScore++;
        JOptionPane.showMessageDialog(view, "Turn passed.");
        view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
        if (turnsWithoutScore >= 6)  view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this)); //end game
    }

    /**
     * Swaps tiles on the model and prompts the view with updated game state.
     * 
     * @param exchangeString the letters to be exchanged
     * @return whether the swap was successful
     */
    public boolean swap(String exchangeString){
        exchangeString = exchangeString.toUpperCase();
        if ((players[currentPlayer].hasLetters(exchangeString)) && exchangeString.length() <= letterBag.getSize()){
            Tile[] exchangeTiles = players[currentPlayer].removeLetters(exchangeString);
            exchangeTiles = letterBag.swapTiles(exchangeTiles);
            players[currentPlayer].addTiles(exchangeTiles);
            nextPlayer();
            turnsWithoutScore++;
            view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
            JOptionPane.showMessageDialog(view, "Tiles " + exchangeString + " swapped.");
            if (turnsWithoutScore >= 6)  view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this)); //end game
            return true;
        }
        JOptionPane.showMessageDialog(view, "Those tiles could not be swapped");
        if (turnsWithoutScore >= 6)  view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this)); //end game
        return false;
    }
}
