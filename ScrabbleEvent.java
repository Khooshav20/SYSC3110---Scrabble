import java.util.ArrayList;
import java.util.EventObject;

public class ScrabbleEvent extends EventObject{
    private ArrayList<Player> players;
    private int currentPlayer;
    private int numLetters;

    public ScrabbleEvent(ArrayList<Player> players, int currentPlayer, int numLetters, ScrabbleController model){
        super(model);
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.numLetters = numLetters;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public int getNumLetters(){
        return numLetters;
    }
}