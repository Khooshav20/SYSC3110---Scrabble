import java.util.ArrayList;
import java.util.HashMap;

/**
 * AIPlayer represents an automated player in the Scrabble game.
 * This class extends the Player class and implements logic for generating
 * and executing moves based on the current state of the game board. The AI
 * evaluates all possible moves and chooses the one that maximizes its score.
 *
 *
 * @author Khooshav Bundhoo (101132063)
 * @author Marc Fernandes (101288346)
 * @author Lucas Warburton (101276823)
 */
public class AIPlayer extends Player{
    private HashMap<String, ArrayList<String[]>> results;

    public AIPlayer() {
        results = new HashMap<>();
    }

    /**
     * Generates the longest possible move for the AI player based on the current state
     * of the board.
     * 
     * @param board The current game board
     * @return The best Move the AI can make, or null if no valid move is possible
     */
    public Location generateLongestMove(Board boardObject) {
        Square[][] board = boardObject.getBoard();
        Location longestMove = new Location(0, 0, "", "", false);
        results = new HashMap<>();
        // horizontal
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) { 
                if (board[i][j] instanceof BlankSquare) { //for every empty tile on board
                    int temp = 0;
                    for (int k = j; k < 15; k++) {
                        if (board[i][k] instanceof BlankSquare) temp++; //count empty squares after original tile
                    }

                    int currentLength = Math.min(7, temp); //set length of word being searched for to max possible
                    while (isConnectedHorizontal(board, i, j, currentLength) && currentLength > longestMove.tiles.length()) {
                        String regex = "";
                        int numLetters = 0;

                        //build regex including only the letters in the word already on the board
                        for (int k = j - 1; k >= 0 && !(board[i][k] instanceof BlankSquare); k--) regex = board[i][k].getLetter() + regex;
                        for (int k = j; k + numLetters < 15 && (k < currentLength + j || !(board[i][k + numLetters] instanceof BlankSquare));) {
                            if (!(board[i][k + numLetters] instanceof BlankSquare)) {
                                regex += board[i][k + numLetters].getLetter();
                                numLetters++;
                            }
                            else {
                                regex += ".";
                                k++;
                            };
                        }
                        // lowercase regex to allow matching with dictionary
                        regex = regex.toLowerCase();

                        // find all valid words that could be played with the tiles given
                        ArrayList<String[]> words = getValidWords(regex);

                        // try every word, if it works then its a new longest move
                        for (String[] word: words) {
                            Tile[] tempTiles = removeLetters(word[1]);
                            Location tempLocation = new Location(i, j, word[1], word[0], true);
                            if (boardObject.isValidMove(tempTiles, word[0], tempLocation.location)) { //check if word is a valid move
                                longestMove = tempLocation;
                                addTiles(tempTiles);
                                break;
                            }
                            addTiles(tempTiles);
                        }

                        // search for shorter words
                        currentLength--;
                    }
                }
            }
        }

        // vertical
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] instanceof BlankSquare) { //for every empty tile on board
                    int temp = 0;
                    for (int k = i; k < 15; k++) {
                        if (board[k][j] instanceof BlankSquare) temp++; //count empty squares after original tile
                    }

                    int currentLength = Math.min(7, temp); //set length of word being searched for to max possible
                    while (isConnectedVertical(board, i, j, currentLength) && currentLength > longestMove.tiles.length()) {
                        String regex = "";
                        int numLetters = 0;

                        //build regex including only the letters in the word already on the board
                        for (int k = i - 1; k >= 0 && !(board[k][j] instanceof BlankSquare); k--) regex = board[k][j].getLetter() + regex;
                        for (int k = i; k + numLetters < 15 && (k < currentLength + i || !(board[k + numLetters][j] instanceof BlankSquare));) {
                            if (!(board[k + numLetters][j] instanceof BlankSquare)) {
                                regex += board[k + numLetters][j].getLetter();
                                numLetters++;
                            }
                            else {
                                regex += ".";
                                k++;
                            };
                        }
                        // lowercase regex to allow matching with dictionary
                        regex = regex.toLowerCase();

                        // find all valid words that could be played with the tiles given
                        ArrayList<String[]> words = getValidWords(regex);

                        // try every word, if it works then its a new longest move
                        for (String[] word: words) {
                            Tile[] tempTiles = removeLetters(word[1]);
                            Location tempLocation = new Location(i, j, word[1], word[0], false);
                            if (boardObject.isValidMove(tempTiles, word[0], tempLocation.location)) { //check if word is a valid move
                                longestMove = tempLocation;
                                addTiles(tempTiles);
                                break;
                            }
                            addTiles(tempTiles);
                        }

                        // search for shorter words
                        currentLength--;
                    }
                }
            }
        }

        return longestMove;
    }

    /**
     * Checks if a horizontal word connects to previously placed tiles.
     * 
     * @param board the board of play
     * @param i the row of the first letter
     * @param j the column of the first letter
     * @param currentLength the length of the word
     * @return whether it is connected
     */
    public boolean isConnectedHorizontal(Square[][] board, int i, int j, int currentLength) {
        // TODO: for each indirectly connected point, check if there are any words
        // that are able to fit, if so return true otherwise return false for optimization
        // maybe include the letters that must be included so they are able to be checked as well
        boolean connected = false;
        if (j - 1 >= 0 && !(board[i][j-1] instanceof BlankSquare)) {
            connected = true;
        }
        if (j + currentLength < 15 && !(board[i][j+currentLength] instanceof BlankSquare)) { //if the word is extended by an existing tile
            connected = true;
        }
        
        for (int k = j; k < 15 && k < j + currentLength; k++) {
            StringBuilder regex = new StringBuilder();
            if (!(board[i][k] instanceof BlankSquare)) { //if the word passes through an existing tile
                connected = true;
                continue;
            }
            for (int l = i - 1; l >= 0 && !(board[l][k] instanceof BlankSquare); l--) { //if the word is below an existing tile
                regex.append(board[l][k].getLetter());
                connected = true;
            }
            regex.reverse();
            regex.append(".");
            for (int l = i + 1; l < 15 && !(board[l][k] instanceof BlankSquare); l++) { //if the word is above an existing tile
                regex.append(board[l][k].getLetter());
                connected = true;
            }
            if (regex.length() > 1) {
                
                if (getValidWords(regex.toString().toLowerCase()).size() == 0) return false;
                
            }
        }

        return connected;
    }

    /**
     * Checks if a vertical word connects to previously placed tiles.
     * 
     * @param board the board of play
     * @param i the row of the first letter
     * @param j the column of the first letter
     * @param currentLength the length of the word
     * @return whether it is connected
     */
    public boolean isConnectedVertical(Square[][] board, int i, int j, int currentLength) {
        boolean connected = false;
        if (i - 1 >= 0 && !(board[i-1][j] instanceof BlankSquare)) { //if the word extends an existing tile
            connected = true;
        }
        if (i + currentLength < 15 && !(board[i+currentLength][j] instanceof BlankSquare)) { //if the word is extended by an existing tile
            connected = true;
        }
        
        for (int k = i; k < 15 && k < i + currentLength; k++) { //if the word passes through an existing tile
            StringBuilder regex = new StringBuilder();
            if (!(board[k][j] instanceof BlankSquare)) { //if the word passes through an existing tile
                connected = true;
                continue;
            }
            for (int l = j - 1; l >= 0 && !(board[i][l] instanceof BlankSquare); l--) { //if the word is below an existing tile
                regex.append(board[i][l].getLetter());
                connected = true;
            }
            regex.reverse();
            // add the letter that the word trying to be played aligns on
            regex.append(".");
            for (int l = i + 1; l < 15 && !(board[i][l] instanceof BlankSquare); l++) { //if the word is above an existing tile
                regex.append(board[i][l].getLetter());
                connected = true;
            }
            // if there are any letters above or below, check to see if any words are possible
            if (regex.length() > 1) {
                if (getValidWords(regex.toString().toLowerCase()).size() == 0) return false;
            }
        }

        return connected;
    }

    /**
     * Gets a list of valid words that match a given regex
     * 
     * @param regex
     * @return the list of valid words, including both the whole word and the letters to be placed on the board
     */
    public ArrayList<String[]> getValidWords(String regex) {
        // all words that are valid
        ArrayList<String[]> words = new ArrayList<>();

        // if results already cached result, return result
        if (results.containsKey(regex)) {
            return results.get(regex);
        }

        // for every word in the dictionary
        for (String word: Board.words) {
            // if the word is the correct length and matches the regex
            if (word.length() == regex.length() && word.matches(regex)) {
                // find all letters needed to play the word
                String newWord = "";
                String scrabbleWord = "";
                for (int i = 0; i < regex.length(); i++) {
                    if (word.charAt(i) != regex.charAt(i)) {
                        newWord += Character.toUpperCase(word.charAt(i));
                        scrabbleWord += Character.toUpperCase(word.charAt(i));
                    }
                    else scrabbleWord += word.charAt(i);
                }

                // if the word has all the letters, then add to list of possible words
                String[] tempWords = new String[2];
                tempWords[0] = scrabbleWord;
                tempWords[1] = newWord;
                if (hasLetters(tempWords[1])) {
                    words.add(tempWords);
                }
            }
        }
        // cache result
        results.put(regex, words);
        
        return words;
    }
}

/**
 * Move represents a potential action that can be performed by the AIPlayer.
 * It contains the position, direction, and tiles involved in the move.
 */
class Location {
    int[] location;
    String tiles;
    String word;

    public Location(int row, int col, String tiles, String word, boolean horizontal) {
        this.tiles = tiles;
        this.word = word;
        this.location = new int[3];
        this.location[Board.ROW] = row;
        this.location[Board.COLUMN] = col;
        this.location[Board.DIRECTION] = horizontal? Board.HORIZONTAL: Board.VERTICAL;
    }
}