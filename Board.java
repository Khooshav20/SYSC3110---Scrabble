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
    class Move {
        boolean isHorizontal;   // false = vertical
        int row;    // 1 ... 15
        int col;    // A=1, B=2 ... O=15

        Move(boolean isHorizontal, int row, int col) {
            this.isHorizontal = isHorizontal;
            this.row = row;
            this.col = col;
        }
    }

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
    public int playMove(Tile[] tiles, String location) {
        if (!isValidMove(tiles, location)) {
            return 0;
        }

        int score = 0;
        Move move = getPlayNotation(location);

        // Convert col and row to a 0-based index
        int colIndex = move.col - 1;
        int rowIndex = move.row - 1;
        
        // Place the word on the board
        for (int i = 0; i < tiles.length; i++) {
            Tile tile = tiles[i];
            score += tile.getScore();

            if (move.isHorizontal) {
                board[rowIndex][colIndex + i] = tile;
            } else {
                board[rowIndex + i][colIndex] = tile;
            }
        }

        return score;
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
    public boolean isValidMove(Tile[] tiles, String location) {
        Move move = getPlayNotation(location);

        if (move == null) {
            return false;
        }
        // Convert col and row to a 0-based index
        int colIndex = move.col - 1;
        int rowIndex = move.row - 1;

        if (move.isHorizontal) {
            // row within 1 and 15
            // col + length of word - 1 still within board (O=15)
            if (move.row >= 1 && move.row <= 15 && move.col + tiles.length - 1 <= 15) {
                for (int i = 0; i < tiles.length; i++) {
                    if (!(board[rowIndex][colIndex + i] instanceof BlankSquare)) {
                        return false;
                    }
                }
                return true;
            }

            return false;
        } 
        
        // col within 1 and 15 (A=1 ... O=15)
        // row + length of word - 1 still within board
        if (move.col >= 1 && move.col <= 15 && move.row + tiles.length - 1 <= 15) {
            for (int i = 0; i < tiles.length; i++) {
                if (!(board[rowIndex + i][colIndex] instanceof BlankSquare)) {
                    return false;
                }
            }
            return true;
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
                Square square = board[i][j];
                sb.append(square instanceof BlankSquare ? " " : ((Tile) square).getLetter()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** Plays are usually notated in the form "WORD xy" where WORD indicates the main word played,
     *  xy is the coordinate of the first letter of the main word
     *
     * @param location The starting row (1-based)
     * @param col The starting column (character A-O)
     * @param direction "H" for horizontal, "V" for vertical
     * @return The formatted coordinate as per the Scrabble rules
     */
    private Move getPlayNotation(String location) {
        Move move = null;

        // Horizontal (15K)
        if (location.matches("([1-9]|1[0-5])[A-O]")) {
            int row = Integer.parseInt(location.substring(0, location.length() - 1));
            int col = location.charAt(location.length() - 1) - 'A' + 1;
            move = new Move(true, row, col);
        }

        // Vertical (K15)
        if (location.matches("[A-O]([1-9]|1[0-5])")) {
            int row = Integer.parseInt(location.substring(1));
            int col = location.charAt(0) - 'A' + 1;
            move = new Move(false, row, col);
        }

        return move;
    }
}
