/**
 * Represents a 15x15 Scrabble board with functionality to place words
 * either horizontally or vertically. The board uses a 2D array of Square objects.
 * Words are placed based on the Scrabble coordinate system:
 * - For horizontal words, the notation is row first then column (e.g., "15K").
 * - For vertical words, the notation is column first then row (e.g., "K15").
 *
 * @author Khooshav Bundhoo (101132063)
 */
public class Board {
    private Square[][] board;
    private static final char[] COLUMN_LABELS = "ABCDEFGHIJKLMNO".toCharArray(); // Columns a-o, represented as A-O

    /**
     * Initializes the Scrabble board with 15x15 squares.
     */
    public Board() {
        board = new Square[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = new BlankSquare();  // Initialize each square as empty
            }
        }
    }

    /**
     * Places a word on the board with appropriate notation.
     *
     * @param word The word to place
     * @param row The starting row (1-based, i.e. 1-15)
     * @param col The starting column (1-based, i.e. 1-15 corresponding to A-O)
     * @param direction "H" for horizontal, "V" for vertical
     * @param score The score for the word
     * @return A formatted string for the move in "WORD xy +score" notation
     */
    public String playMove(String word, int row, char col, String direction, int score) {
        // Convert column letter to a 0-based index
        int colIndex = Character.toUpperCase(col) - 'A';
        int rowIndex = row - 1; // Convert to 0-based row index

        if (!isValidMove(word, rowIndex, colIndex, direction)) {
            return "Invalid move";
        }

        // Place the word on the board
        if (direction.equals("H")) {
            for (int i = 0; i < word.length(); i++) {
                board[rowIndex][colIndex + i].setLetter(word.charAt(i));
            }
        } else if (direction.equals("V")) {
            for (int i = 0; i < word.length(); i++) {
                board[rowIndex + i][colIndex].setLetter(word.charAt(i));
            }
        }

        String coordinate = getPlayNotation(row, col, direction);
        return String.format("%s %s +%d", word.toUpperCase(), coordinate, score);
    }

    /**
     * Validates the placement of the word on the board.
     *
     * @param word The word to place
     * @param row The starting row (0-based index)
     * @param col The starting column (0-based index)
     * @param direction "H" for horizontal, "V" for vertical
     * @return true if the placement is valid, false otherwise
     */
    private boolean isValidMove(String word, int row, int col, String direction) {
        if (direction.equals("H")) {
            return col + word.length() <= 15 && row >= 0 && row < 15;
        } else if (direction.equals("V")) {
            return row + word.length() <= 15 && col >= 0 && col < 15;
        }
        return false;
    }

    /**
     * Returns a string representation of the board.
     *
     * @return The board in string form
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  A B C D E F G H I J K L M N O\n");
        for (int i = 0; i < 15; i++) {
            sb.append(String.format("%2d", i + 1)).append(" ");
            for (int j = 0; j < 15; j++) {
                sb.append(board[i][j].getLetter()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** Plays are usually notated in the form "WORD xy +score" where WORD indicates the main word played,
     *  xy is the coordinate of the first letter of the main word,
     *
     *
     * @param row The starting row (1-based)
     * @param col The starting column (character A-O)
     * @param direction "H" for horizontal, "V" for vertical
     * @return The formatted coordinate as per the Scrabble rules
     */
    private String getPlayNotation(int row, char col, String direction) {
        if (direction.equals("H")) {
            // Row comes first, then column letter (e.g., 15K)
            return String.format("%d%c", row, Character.toUpperCase(col));
        } else if (direction.equals("V")) {
            // Column letter comes first, then row (e.g., K15)
            return String.format("%c%d", Character.toUpperCase(col), row);
        }
        return "";
    }
}
