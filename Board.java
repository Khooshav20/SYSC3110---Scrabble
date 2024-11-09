import java.io.*;
import java.util.*;

/**
 * Represents a 15x15 Scrabble board with functionality to place words
 * either horizontally or vertically. The board uses a 2D array of Square objects.
 * Words are placed based on the Scrabble coordinate system:
 * - For horizontal words, the notation is row first then column (e.g., "15K").
 * - For vertical words, the notation is column first then row (e.g., "K15").
 *
 * @author Khooshav Bundhoo (101132063)
 * @author Marc Fernandes (101288346)
 * @author Lucas Warburton (101276823)
 * @version 22/10/2024
 */

public class Board {
    private Square[][] board;

    public static final int ROW = 0;
    public static final int COLUMN = 1;
    public static final int DIRECTION = 2;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static ArrayList<String> words = new ArrayList<>();

    /**
     * Initializes the Scrabble board with 15x15 squares.
     */
    public Board() throws IOException{
        board = new Square[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = new BlankSquare();  // Initialize each square as empty
            }
        }

        if (words.size() == 0) {
            InputStream in = getClass().getResourceAsStream("/dictionary.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                words.add(line);
                line = reader.readLine();
            }
            reader.close();
        }
    }

    /**
     * Places a word on the board with appropriate notation.
     *
     * @param tiles the tiles which will be new on the board
     * @param word the word to be played
     * @param l the location formatted in Scrabble notation
     * @return A formatted string for the move in "WORD xy +score" notation
     */
    public int playMove(Tile[] tiles, String word, String l) {
        int[] location = stringToLocation(l);
        int score = 0;
        int numBlank = 0;

        if (location[DIRECTION] == HORIZONTAL) {
            for (int i = location[COLUMN]; i < location[COLUMN] + word.length(); i++) {
                boolean hasAdjacentTile = false;
                if (board[location[ROW]][i] instanceof Tile) {
                    Tile t = (Tile) board[location[ROW]][i];
                    score += t.getScore();
                }
                else {
                    int j = location[ROW] - 1;
                    while (board[j][i] instanceof Tile) {
                        Tile boardTile = (Tile) board[j][i];
                        score += boardTile.getScore();
                        hasAdjacentTile = true;
                        j--;
                    }
                    j = location[ROW] + 1;
                    while (board[j][i] instanceof Tile) {
                        Tile boardTile = (Tile) board[j][i];
                        score += boardTile.getScore();
                        hasAdjacentTile = true;
                        j++;
                    }
                }
                if (hasAdjacentTile) {
                    score += tiles[numBlank].getScore();
                    numBlank++;
                }
            }
        }
        else {
            for (int i = location[ROW]; i < location[ROW] + word.length(); i++) {
                boolean hasAdjacentTile = false;
                if (board[i][location[COLUMN]] instanceof Tile) {
                    Tile t = (Tile) board[i][location[COLUMN]];
                    score += t.getScore();
                }
                else {
                    int j = location[COLUMN] - 1;
                    while (board[i][j] instanceof Tile) {
                        Tile boardTile = (Tile) board[i][j];
                        score += boardTile.getScore();
                        hasAdjacentTile = true;
                        j--;
                    }
                    j = location[COLUMN] + 1;
                    while (board[i][j] instanceof Tile) {
                        Tile boardTile = (Tile) board[i][j];
                        score += boardTile.getScore();
                        hasAdjacentTile = true;
                        j++;
                    }
                }
                if (hasAdjacentTile) {
                    score += tiles[numBlank].getScore();
                    numBlank++;
                }
            }
        }

        
        for (Tile t: tiles) {
            score += t.getScore();
            if (location[DIRECTION] == HORIZONTAL) {
                int i = location[COLUMN];
                while (board[location[ROW]][i++] instanceof Tile);
                board[location[ROW]][--i] = t;
            }
            else {
                int i = location[ROW];
                while (board[i++][location[COLUMN]] instanceof Tile);
                board[--i][location[COLUMN]] = t;
            }
        }

        return score;
    }

    /**
     * Validates the placement of the word on the board.
     *
     * @param tiles the tiles which will be new on the board
     * @param word the word to be played
     * @param l the location formatted in Scrabble notation
     * @return true if the placement is valid, false otherwise
     */
    public boolean isValidMove(Tile[] tiles, String word, String l) {
        int[] location = stringToLocation(l);

        if (!words.contains(word.toLowerCase())) {
            System.out.println("is not a word!!");
            return false;
        }

        if (location[DIRECTION] == HORIZONTAL) {
            if (location[COLUMN] + word.length() > 15) {
                System.out.println("extends off board");
                return false;
            }
            for (int i = location[COLUMN]; i < location[COLUMN] + word.length(); i++) {
                if (board[location[ROW]][i] instanceof BlankSquare) {
                    boolean added = false;
                    StringBuilder s = new StringBuilder();
                    int j = location[ROW] - 1;
                    while (j >= 0 && board[j][i] instanceof Tile) {
                        Tile boardTile = (Tile) board[j][i];
                        s.append(boardTile.getLetter());
                        added = true;
                        j--;
                    }
                    s.reverse();

                    s.append(word.charAt(i - location[COLUMN]));
                    j = location[ROW] + 1;
                    while (j <= 14 && board[j][i] instanceof Tile) {
                        Tile boardTile = (Tile) board[j][i];
                        s.append(boardTile.getLetter());
                        added = true;
                        j++;
                    }

                    if (!words.contains(s.toString().toLowerCase()) && added) {
                        System.out.println("perpendicular word invalid");
                        return false;
                    }
                }
            }
        }
        else {
            if (location[ROW] + word.length() > 15) {
                System.out.println("extends off board");
                return false;
            }
            for (int i = location[ROW]; i < location[ROW] + word.length(); i++) {
                if (board[i][location[COLUMN]] instanceof BlankSquare) {
                    StringBuilder s = new StringBuilder();
                    boolean added = false;
                    int j = location[COLUMN] - 1;
                    while (j >= 0 && board[i][j] instanceof Tile) {
                        Tile boardTile = (Tile) board[i][j];
                        s.append(boardTile.getLetter());
                        added = true;
                        j--;
                    }
                    s.reverse();
                    s.append(word.charAt(i - location[ROW]));
                    j = location[COLUMN] + 1;
                    while (j <= 14 && board[i][j] instanceof Tile){
                        Tile boardTile = (Tile) board[i][j];
                        s.append(boardTile.getLetter());
                        added = true;
                        j++;
                    }

                    if (!words.contains(s.toString()) && added) {
                        System.out.println("perpendicular word invalid");
                        return false;
                    }
                }
            }
        }

        if (location[DIRECTION] == HORIZONTAL){
            for (int i = 0; i < word.length(); i++){
                if (Character.isLowerCase(word.charAt(i))){
                    if (board[location[ROW]][location[COLUMN] + i].getLetter() != word.charAt(i) - 0x20){
                        System.out.println("board doesn't have that letter");
                        return false;
                    }
                } else {
                    if (board[location[ROW]][location[COLUMN] + i] instanceof Tile){
                        System.out.println("there's already a tile there bro");
                        return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < word.length(); i++){
                if (Character.isLowerCase(word.charAt(i))){
                    if (board[location[ROW] + i][location[COLUMN]].getLetter() != word.charAt(i) - 0x20){
                        System.out.println("board doesn't have that letter");
                        return false;
                    }
                } else {
                    if (board[location[ROW] + i][location[COLUMN]] instanceof Tile){
                        System.out.println("there's already a tile there bro");
                        return false;
                    }
                }
            }
        }

        if (board[7][7] instanceof Tile) {
            boolean isConnected = false;
            if (location[DIRECTION] == HORIZONTAL) {
                for (int i = location[COLUMN]; i < location[COLUMN] + word.length(); i++) {
                    if (board[location[ROW]][i] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (location[ROW] <= 14 && board[location[ROW] + 1][i] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (location[ROW] >= 0 && board[location[ROW] - 1][i] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (i <= 14 && board[location[ROW]][i+1] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (i >= 0 && board[location[ROW]][i-1] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                }
            }
            else {
                for (int i = location[ROW]; i < location[ROW] + word.length(); i++) {
                    if (board[i][location[COLUMN]] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (i >= 0 && board[i-1][location[COLUMN]] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (i <= 14 && board[i+1][location[COLUMN]] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (location[COLUMN] >= 0 && board[i][location[COLUMN] - 1] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                    if (location[COLUMN] <= 14 && board[i][location[COLUMN] + 1] instanceof Tile) {
                        isConnected = true;
                        break;
                    }
                }
            }
            if (!isConnected) {
                System.out.println("word not connected to other word");
                return isConnected;
            }
        }
        else {
            if (location[DIRECTION] == HORIZONTAL) {
                if (location[COLUMN] > 7 || location[COLUMN] + word.length() < 7) {
                    System.out.println("starting word not on starting square");
                    return false;
                } 
            }
            if (location[DIRECTION] == VERTICAL) {
                if (location[ROW] > 7 || location[ROW] + word.length() < 7) {
                    System.out.println("starting word not on starting square");
                    return false;
                } 
            }
        }

        

        return true;
    }

    private int[] stringToLocation(String s) {
        int[] location = new int[3];
        if (Character.isLetter(s.charAt(0))) {
            location[DIRECTION] = VERTICAL;
            location[COLUMN] = s.charAt(0) - 'A';
            location[ROW] = Integer.parseInt(s.substring(1, s.length())) - 1;
        }
        else {
            location[DIRECTION] = HORIZONTAL;
            location[ROW] = Integer.parseInt(s.substring(0, s.length() - 1)) - 1;
            location[COLUMN] = s.charAt(s.length() - 1) - 'A';
        }

        return location;
    }

    /**
     * Returns a string representation of the board.
     *
     * @return The board in string form
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   A B C D E F G H I J K L M N O\n");
        for (int i = 0; i < 15; i++) {
            sb.append(String.format("%2d", i + 1)).append("|");
            for (int j = 0; j < 15; j++) {
                sb.append(board[i][j].getLetter()).append("|");
            }
            sb.append("\n  ------------------------------\n");
        }
        return sb.toString();
    }

    public Square[][] getBoard() {
        return board;
    }
}
