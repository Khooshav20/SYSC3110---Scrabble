import java.util.ArrayList;
import java.util.EventObject;

/**
 * An Event used to instruct the View on what to display.
 *
 * @author Lucas Warburton (101276823)
 * @version 10/11/2024
 */
public class ScrabbleEvent extends EventObject{
    private Player[] players;
    private int currentPlayer;
    private int numLetters;
    private Square[][] board;
    private boolean undoEnabled;
    private boolean redoEnabled;

    /**
     * Initializes a ScrabbleEvent with all relevant information about the current game state.
     * 
     * @param players The players in the game
     * @param currentPlayer The index of the player whose turn it is
     * @param numLetters The number of tiles in the letter bag
     * @param model The object creating this event
     * @param board The board of the current game
     */
    public ScrabbleEvent(Player[] players, int currentPlayer, int numLetters, ScrabbleController model, Square[][] board, boolean undoEnabled, boolean redoEnabled){
        super(model);
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.numLetters = numLetters;
        this.board = board;
        this.undoEnabled = undoEnabled;
        this.redoEnabled = redoEnabled;
    }

    /**
     * Gets the array of players
     * 
     * @return the array of players
     */
    public Player[] getPlayers(){
        return players;
    }

    /**
     * Gets the index of the current player
     * 
     * @return the index of the current player
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Gets the number of letters in the letter bag
     * 
     * @return the number of letters in the letter bag
     */
    public int getNumLetters(){
        return numLetters;
    }

    public Square[][] getBoard() {
        return board;
    }

    public boolean getUndoEnabled() {
        return this.undoEnabled;
    }

    public boolean getRedoEnabled() {
        return this.redoEnabled;
    }
}
