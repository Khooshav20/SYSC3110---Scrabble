/**
 * Represents a 15x15 Scrabble board with functionality to place words
 * either horizontally or vertically. The board uses a 2D array of Square objects.
 * Words are placed based on the Scrabble coordinate system:
 *
 * @author Khooshav Bundhoo (101132063)
 */

public class Board {
    // Represents a move on the board, indicating whether it's horizontal or vertical, and the starting position.
    class Move {
        boolean isHorizontal;  // false = vertical, true = horizontal
        int row;    // Row of the move (1-based, 1 to 15)
        int col;    // Column of the move (1-based, A=1, B=2, ..., O=15)

        // Constructor for Move class
        Move(boolean isHorizontal, int row, int col) {
            this.isHorizontal = isHorizontal;
            this.row = row;
            this.col = col;
        }
    }

    // The Scrabble board as a 15x15 2D array of Square objects
    private Square[][] board;
    private static final char[] COLUMN_LABELS = "ABCDEFGHIJKLMNO".toCharArray();  // Labels for columns A-O

    // Constructor: Initializes the board with blank squares
    public Board() {
        board = new Square[15][15];  // 15x15 Scrabble board
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = new BlankSquare();  // Each square starts as blank
            }
        }
    }

    /**
     * Places a word on the board based on the given tiles and location.
     *
     * @param tiles The tiles that form the word to be placed.
     * @param location The starting location on the board in Scrabble notation (e.g., 15K or K15).
     * @return The score for the word, or 0 if the move is invalid.
     */
    public int playMove(Tile[] tiles, String location) {
        if (!isValidMove(tiles, location)) {
            return 0;  // Invalid move, return score 0
        }

        int score = 0;  // Initialize the score for the move
        Move move = getPlayNotation(location);  // Parse the location into a Move object

        // Convert column and row to 0-based index for array access
        int colIndex = move.col - 1;
        int rowIndex = move.row - 1;

        // Place each tile on the board, either horizontally or vertically
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            score += tile.getScore();  // Add the score of the tile to the total score

            if (move.isHorizontal) {
                board[rowIndex][colIndex + i] = tile;  // Place tile horizontally
            } else {
                board[rowIndex + i][colIndex] = tile;  // Place tile vertically
            }
        }

        return score;  // Return the total score for the word
    }

    /**
     * Validates if a word can be placed on the board at the specified location.
     *
     * @param tiles The tiles that form the word to be placed.
     * @param location The starting location in Scrabble notation (e.g., 15K or K15).
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(Tile[] tiles, String location) {
        Move move = getPlayNotation(location);  // Parse the location into a Move object

        if (move == null) {
            return false;  // Invalid location format
        }

        // Convert column and row to 0-based index for array access
        int colIndex = move.col - 1;
        int rowIndex = move.row - 1;

        if (move.isHorizontal) {
            // Check if the word fits within the row and the board limits
            if (move.row >= 1 && move.row <= 15 && move.col + tiles.length - 1 <= 15) {
                // Ensure each square in the row is blank
                for (int i = 0; i < tiles.length; i++) {
                    if (!(board[rowIndex][colIndex + i] instanceof BlankSquare)) {
                        return false;  // A square is already occupied
                    }
                }
                return true;  // Valid move
            }
            return false;  // Invalid move, word goes beyond the board
        }

        // Check if the word fits within the column and the board limits
        if (move.col >= 1 && move.col <= 15 && move.row + tiles.length - 1 <= 15) {
            // Ensure each square in the column is blank
            for (int i = 0; i < tiles.length; i++) {
                if (!(board[rowIndex + i][colIndex] instanceof BlankSquare)) {
                    return false;  // A square is already occupied
                }
            }
            return true;  // Valid move
        }

        return false;  // Invalid move
    }

    /**
     * Returns a string representation of the Scrabble board.
     *
     * @return The board formatted as a string, with rows and columns labeled.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  A B C D E F G H I J K L M N O\n");  // Column headers
        for (int i = 0; i < 15; i++) {
            sb.append(String.format("%2d", i + 1)).append(" ");  // Row labels
            for (int j = 0; j < 15; j++) {
                Square square = board[i][j];
                sb.append(square instanceof BlankSquare ? " " : ((Tile) square).getLetter()).append(" ");  // Append square contents
            }
            sb.append("\n");
        }
        return sb.toString();  // Return the board as a string
    }

    /**
     * Parses the Scrabble notation for a move and returns a Move object.
     *
     * @param location The move notation in Scrabble format (e.g., 15K or K15).
     * @return A Move object representing the direction, row, and column, or null if invalid.
     */
    private Move getPlayNotation(String location) {
        Move move = null;

        // Check for horizontal move (e.g., 15K)
        if (location.matches("([1-9]|1[0-5])[A-O]")) {
            int row = Integer.parseInt(location.substring(0, location.length() - 1));
            int col = location.charAt(location.length() - 1) - 'A' + 1;
            move = new Move(true, row, col);  // Horizontal move
        }

        // Check for vertical move (e.g., K15)
        if (location.matches("[A-O]([1-9]|1[0-5])")) {
            int row = Integer.parseInt(location.substring(1));
            int col = location.charAt(0) - 'A' + 1;
            move = new Move(false, row, col);  // Vertical move
        }

        return move;  // Return the move object, or null if the notation is invalid
    }
}
