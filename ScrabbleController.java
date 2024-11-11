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
        //obtain the tiles required for the move from the player's rack
        Tile[] moveTiles = players[currentPlayer].removeLetters(moveLetters);
        
        if (board.isValidMove(moveTiles, word, location)){
            //play move
            int score = board.playMove(moveTiles, word, location);
            players[currentPlayer].addScore(score);

            //draw new tiles for player
            if (letterBag.getSize() > 0){
                players[currentPlayer].addTiles(letterBag.getTiles(Math.min(moveLetters.length(), letterBag.getSize())));
            }

            //output move to player
            JOptionPane.showMessageDialog(view, word + " played for " + score + " points.");

            //proceed to next turn
            nextPlayer();
            view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
        
            if (score > 0) turnsWithoutScore = 0;

            //end game
            if (players[currentPlayer-1 >= 0 ? currentPlayer-1: players.length-1].getNumTiles() == 0){
                view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this)); 
            }

            //move unsuccessful
            return true;
        } else {
            //put tiles back in player's hand
            players[currentPlayer].addTiles(moveTiles);

            //move successful
            return false;
        }
    }

    /**
     * Passes on the model and prompts the view with updated game state.
     */
    public void pass(){
        //proceed to next turn
        nextPlayer();
        view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
        turnsWithoutScore++;

        //output move to player
        JOptionPane.showMessageDialog(view, "Turn passed.");
        
        //end game
        if (turnsWithoutScore >= 6){
            view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
        }
    }

    /**
     * Swaps tiles on the model and prompts the view with updated game state.
     * 
     * @param exchangeString the letters to be exchanged
     * @return whether the swap was successful
     */
    public boolean swap(String exchangeString){
        if (exchangeString == null) return false;
        exchangeString = exchangeString.toUpperCase();

        if ((players[currentPlayer].hasLetters(exchangeString)) && exchangeString.length() <= letterBag.getSize()){ //if tiles can be exchanged
            //remove tiles from player hand
            Tile[] exchangeTiles = players[currentPlayer].removeLetters(exchangeString);

            //put tiles into letterBag, recieve same amount back
            exchangeTiles = letterBag.swapTiles(exchangeTiles);

            //add new tiles to player
            players[currentPlayer].addTiles(exchangeTiles);

            //proceed to next turn
            nextPlayer();
            turnsWithoutScore++;
            view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));

            //output move to player
            JOptionPane.showMessageDialog(view, "Tiles " + exchangeString + " swapped.");

            //end game
            if (turnsWithoutScore >= 6){
                view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this));
            }

            //swap successful
            return true;
        }
        //output failure to player
        JOptionPane.showMessageDialog(view, "Those tiles could not be swapped");

        //swap unsuccessful
        return false;
    }
}
