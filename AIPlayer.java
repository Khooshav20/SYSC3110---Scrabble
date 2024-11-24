import java.util.ArrayList;
import java.util.List;

/**
 * AIPlayer represents an automated player in the Scrabble game.
 * This class extends the Player class and implements logic for generating
 * and executing moves based on the current state of the game board. The AI
 * evaluates all possible moves and chooses the one that maximizes its score.
 *
 *
 * @author Khooshav Bundhoo (101132063)
 */
public class AIPlayer extends Player {
    public AIPlayer() {
        
    }

    /**
     * Generates the best possible move for the AI player based on the current state
     * of the board.
     * 
     * @param board The current game board
     * @return The best Move the AI can make, or null if no valid move is possible
     */
    public Location generateBestMove(Board boardObject) {
        Square[][] board = boardObject.getBoard();
        Location longestMove = new Location(0, 0, "", "", false);
        // horizontal
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] instanceof BlankSquare) {
                    int temp = 0;
                    for (int k = j; k < 15; k++) {
                        if (board[i][k] instanceof BlankSquare) temp++;
                    }

                    int currentLength = Math.min(7, temp);
                    while (isConnectedHorizontal(board, i, j, currentLength) && currentLength > longestMove.tiles.length()) {
                        String regex = "";
                        int numLetters = 0;
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

                        regex = regex.toLowerCase();
                        ArrayList<String[]> words = getValidWords(regex);
                        for (String[] word: words) {
                            Tile[] tempTiles = removeLetters(word[1]);
                            Location tempLocation = new Location(i, j, word[1], word[0], true);
                            if (boardObject.isValidMove(tempTiles, word[0], tempLocation.location)) {
                                longestMove = tempLocation;
                                addTiles(tempTiles);
                                break;
                            }
                            addTiles(tempTiles);
                        }

                        currentLength--;
                    }
                }
            }
        }

        // vertical
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] instanceof BlankSquare) {
                    int temp = 0;
                    for (int k = i; k < 15; k++) {
                        if (board[k][j] instanceof BlankSquare) temp++;
                    }

                    int currentLength = Math.min(7, temp);
                    
                    while (isConnectedVertical(board, i, j, currentLength) && currentLength > longestMove.tiles.length()) {
                        String regex = "";
                        int numLetters = 0;
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

                        regex = regex.toLowerCase();
                        ArrayList<String[]> words = getValidWords(regex);
                        for (String[] word: words) {
                            Tile[] tempTiles = removeLetters(word[1]);
                            if (boardObject.isValidMove(tempTiles, word[0], longestMove.location)) {
                                longestMove = new Location(i, j, word[1], word[0], false);
                                addTiles(tempTiles);
                                break;
                            }
                            addTiles(tempTiles);
                        }

                        currentLength--;
                    }
                }
            }
        }

        System.out.println(longestMove.tiles);
        return longestMove;
    }

    public boolean isConnectedHorizontal(Square[][] board, int i, int j, int currentLength) {
        if (j - 1 >= 0 && !(board[i][j-1] instanceof BlankSquare)) {
            return true;
        }
        if (j + currentLength < 15 && !(board[i][j+currentLength] instanceof BlankSquare)) {
            return true;
        }
        
        for (int k = j; k < 15 && k < j + currentLength; k++) {
            if (!(board[i][k] instanceof BlankSquare)) {
                return true;
            }
            if (i - 1 >= 0 && !(board[i-1][k] instanceof BlankSquare)) {
                return true;
            }
            if (i + 1 < 15 && !(board[i+1][k] instanceof BlankSquare)) {
                return true;
            }
        }

        return false;
    }

    public boolean isConnectedVertical(Square[][] board, int i, int j, int currentLength) {
        if (i - 1 >= 0 && !(board[i-1][j] instanceof BlankSquare)) {
            return true;
        }
        if (i + currentLength < 15 && !(board[i+currentLength][j] instanceof BlankSquare)) {
            return true;
        }
        
        for (int k = i; k < 15 && k < i + currentLength; k++) {
            if (!(board[k][j] instanceof BlankSquare)) {
                return true;
            }
            if (j - 1 >= 0 && !(board[k][j-1] instanceof BlankSquare)) {
                return true;
            }
            if (j + 1 < 15 && !(board[k][j+1] instanceof BlankSquare)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<String[]> getValidWords(String regex) {
        ArrayList<String[]> words = new ArrayList<>();
        for (String word: Board.words) {
            if (word.length() == regex.length() && word.matches(regex)) {
                String newWord = "";
                String scrabbleWord = "";
                for (int i = 0; i < regex.length(); i++) {
                    if (word.charAt(i) != regex.charAt(i)) {
                        newWord += Character.toUpperCase(word.charAt(i));
                        scrabbleWord += Character.toUpperCase(word.charAt(i));
                    }
                    else scrabbleWord += word.charAt(i);
                }

                String[] tempWords = new String[2];
                tempWords[0] = scrabbleWord;
                tempWords[1] = newWord;
                if (hasLetters(newWord.toUpperCase())) {
                    words.add(tempWords);
                }
            }
        }

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