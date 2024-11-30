/**
 * 
 * ScrabbleController is responsible for managing interactions between the Scrabble game model and view.
 * It processes user actions, updates the view based on the game state, and controls player turn rotation.
 * 
 * @author Khooshav Bundhoo (101132063)
 * @author Lucas Warburton (101276823)
 */

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class ScrabbleController implements Serializable{
    private LetterBag letterBag; // Model representing the game state
    private Player[] players; // Model representing the game state
    private Board board; // Model representing the game state
    private View view;
    private int currentPlayer; // Tracks the index of the current player

    private Stack<GameState> undoStack;
    private Stack<GameState> redoStack;
    private GameState buffer;

    private int turnsWithoutScore;

    /**
     * Constructor to initialize the controller with a game model and view.
     * @param view The Scrabble View
     * @param numPlayers The number of players
     * @throws IOException If it fails to read dictionary.txt or files.txt
     */
    public ScrabbleController (View view, int numPlayers, int numAIPlayers, Square[][] boardArray) throws IOException {
        letterBag = new LetterBag();

        undoStack = new Stack<>();
        redoStack = new Stack<>();

        players = new Player[numPlayers + numAIPlayers];
        for (int i = 0; i < numPlayers; i++){
            players[i] = new Player();
            players[i].addTiles(letterBag.getTiles(7));
        }
        for (int i = numPlayers; i < numAIPlayers + numPlayers; i++) {
            players[i] = new AIPlayer();
            players[i].addTiles(letterBag.getTiles(7));
        }

        board = new Board();
        board.setBoard(boardArray);

        turnsWithoutScore = 0;
        currentPlayer = 0;

        addToStack(getCurrentGameState(players, letterBag, board.getBoard(), currentPlayer, turnsWithoutScore));

        this.view = view;
        
        updateView();
    }

    /**
     * Advances to the next player's turn.
     */
    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.length; // Cycle to the next player
        if (!(players[currentPlayer] instanceof AIPlayer)) {
            addToStack(getCurrentGameState(players, letterBag, board.getBoard(), currentPlayer, turnsWithoutScore));
        }
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

            
            //view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this, board.getBoard(), !undoStack.empty(), !redoStack.empty()));
            //proceed to next turn
            nextPlayer();

            //output move to player
            updateView();
            JOptionPane.showMessageDialog(view, word + " played for " + score + " points.");
            
        
            if (score > 0) turnsWithoutScore = 0;

            //end game
            if (players[currentPlayer-1 >= 0 ? currentPlayer-1: players.length-1].getNumTiles() == 0){
                view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this, board.getBoard(), (undoStack.size() > 1), !redoStack.empty())); 
            }
            checkAI();
            //move successful
            return true;
        } else {
            //put tiles back in player's hand
            players[currentPlayer].addTiles(moveTiles);

            //move unsuccessful
            return false;
        }
    }

    /**
     * Passes on the model and prompts the view with updated game state.
     */
    public void pass(){
        //proceed to next turn
        nextPlayer();
        turnsWithoutScore++;

        //output move to player
        updateView();
        JOptionPane.showMessageDialog(view, "Turn passed.");
        
        //end game
        if (turnsWithoutScore >= 6){
            view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this, board.getBoard(), !undoStack.empty(), !redoStack.empty()));
        }

        checkAI();
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

            //output move to player
            updateView();
            JOptionPane.showMessageDialog(view, "Tiles " + exchangeString + " swapped.");
            

            //end game
            if (turnsWithoutScore >= 6){
                view.endGame(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this, board.getBoard(), !undoStack.empty(), !redoStack.empty()));
            }

            checkAI();

            //swap successful
            return true;
        }
        //output failure to player
        JOptionPane.showMessageDialog(view, "Those tiles could not be swapped");

        //swap unsuccessful
        return false;
    }

    /**
     * Checks if the current player is an AI Player, and handles its move if so.
     */
    public void checkAI(){
        if (players[currentPlayer] instanceof AIPlayer) {
            AIPlayer p = (AIPlayer) players[currentPlayer];
            Location l = p.generateLongestMove(board);
            if (l.word.length() == 0) { //if no valid moves
                System.out.println("AI does not have valid move, passing");
                this.pass();
            }
            else {
                this.play(l.tiles, l.word, l.location);
            }
        }
    }

    public boolean save(String filename){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static ScrabbleController load(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ScrabbleController temp = (ScrabbleController) objectInputStream.readObject();
        objectInputStream.close();
        return temp;
    }

    public boolean undo() {
        if (undoStack.empty()) {
            System.out.println("Stack empty, something went wrong");
            return false;
        }

        redoStack.add(getCurrentGameState(players, letterBag, board.getBoard(), currentPlayer, turnsWithoutScore));

        GameState gs = undoStack.pop();
        buffer = getCurrentGameState(gs.getPlayers(), gs.getBag(), gs.getBoard(), gs.getCurrentPlayer(), gs.getTurnsWithoutScore());

        players = gs.getPlayers();
        letterBag = gs.getBag();
        board.setBoard(gs.getBoard());
        currentPlayer = gs.getCurrentPlayer();
        turnsWithoutScore = gs.getTurnsWithoutScore();

        updateView();
        return undoStack.empty();
    }

    public boolean redo() {
        if (redoStack.empty()) {
            System.out.println("Stack empty, something went wrong");
            return false;
        }

        undoStack.add(getCurrentGameState(players, letterBag, board.getBoard(), currentPlayer, turnsWithoutScore));

        GameState gs = redoStack.pop();
        buffer = getCurrentGameState(gs.getPlayers(), gs.getBag(), gs.getBoard(), gs.getCurrentPlayer(), gs.getTurnsWithoutScore());

        players = gs.getPlayers();
        letterBag = gs.getBag();
        board.setBoard(gs.getBoard());
        currentPlayer = gs.getCurrentPlayer();
        turnsWithoutScore = gs.getTurnsWithoutScore();

        updateView();
        return redoStack.empty();
    }

    public GameState getCurrentGameState(Player[] players, LetterBag letterBag, Square[][] mainBoard, int currentPlayer, int turnsWithoutScore) {
        try {
            Player[] playersCopy = new Player[players.length];

            for (int i = 0; i < players.length; i++) {
                playersCopy[i] = (players[i] instanceof AIPlayer) ? new AIPlayer(): new Player();
                playersCopy[i].rack = (ArrayList<Tile>) players[i].rack.clone();
                playersCopy[i].addScore(players[i].getScore());
            }

            if (players[0].rack == playersCopy[0].rack) {
                System.out.println("player rack equal");
                System.exit(17);
            }

            LetterBag letterBagCopy = new LetterBag();
            letterBagCopy.setLetters((ArrayList<Tile>) letterBag.getLetters().clone());

            if (letterBagCopy.getLetters() == letterBag.getLetters()) {
                System.out.println("tile bag equal");
                System.exit(11);
            }

            Square[][] boardCopy = new Square[15][15];

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (mainBoard[i][j] instanceof PremiumTile) boardCopy[i][j] = (PremiumTile) ((PremiumTile) mainBoard[i][j]).clone();
                    else if (mainBoard[i][j] instanceof MiddleTile) boardCopy[i][j] = new MiddleTile();
                    else if (mainBoard[i][j] instanceof BlankSquare) boardCopy[i][j] = new BlankSquare();
                    else if (mainBoard[i][j] instanceof Tile) boardCopy[i][j] = (Tile) ((Tile) mainBoard[i][j]).clone();
                }
            }

            if (boardCopy == mainBoard) {
                System.out.println("board equal");
                System.exit(53);
            }

            return new GameState(letterBagCopy, playersCopy, boardCopy, currentPlayer, turnsWithoutScore);
            
        } catch (Exception e) { e.printStackTrace(); }

        return null;
    }

    public void addToStack(GameState gs) {
        if (buffer != null) {
            undoStack.push(buffer);
        }
        buffer = gs;
        redoStack = new Stack<>();

    }

    public void updateView(){
        view.handleScrabbleStatusUpdate(new ScrabbleEvent(players, currentPlayer, letterBag.getSize(), this, board.getBoard(), !undoStack.empty(), !redoStack.empty()));
    }

    public void setView(View v){
        view = v;
    }
}
