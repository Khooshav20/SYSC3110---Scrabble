import java.io.Serializable;

public class GameState implements Serializable {
    private LetterBag bag;
    private Player[] players;
    private Square[][] board;
    private int currentPlayer;
    private int turnsWithoutScore;

    public GameState(LetterBag bag, Player[] players, Square[][] board, int currentPlayer, int turnsWithoutScore) {
        this.bag = bag;
        this.players = players;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.turnsWithoutScore = turnsWithoutScore;
    }


    public LetterBag getBag() {
        return this.bag;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getTurnsWithoutScore() {
        return this.turnsWithoutScore;
    }
}
