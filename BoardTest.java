import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Tests the board class, note testStringToLocation() method as stringToLocation() is
 * a private method in board.
 *
 * @author Alexander Gardiner, 101261196
 * @version 08/11/2024
 */

public class BoardTest {

    private Board board;

    @Before
    public void setUp() throws IOException {
        // Initialize the board before each test.
        board = new Board();
    }

    @Test
    public void testConstructor() {
        // Test if board is initialized with 15x15 blank squares.
        Square[][] squares = board.getBoard();
        assertEquals("Board should have 15 rows", 15, squares.length);
        assertEquals("Each row should have 15 columns", 15, squares[0].length);

        // Verify each square is a BlankSquare.
        for (Square[] row : squares) {
            for (Square square : row) {
                assertTrue("Each square should be an instance of BlankSquare", square instanceof BlankSquare);
            }
        }
    }

    @Test
    public void testPlayMoveHorizontal() {
        // Set up a sample move.
        Tile[] tiles = { new Tile('H', 4), new Tile('E', 1), new Tile('L', 1), new Tile('L', 1), new Tile('O', 1) };
        String word = "HELLO";
        String location = "8H";

        // Play the move.
        int score = board.playMove(tiles, word, location);

        // Check if score is as expected.
        int expectedScore = 4 + 1 + 1 + 1 + 1;
        assertEquals("Score should be calculated based on tile values", expectedScore, score);

        // Check if tiles are placed correctly on the board.
        Square[][] squares = board.getBoard();
        assertEquals("Tile 'H' should be placed at 7,7", 'H', squares[7][7].getLetter());
        assertEquals("Tile 'E' should be placed at 7,8", 'E', squares[7][8].getLetter());
        assertEquals("Tile 'L' should be placed at 7,9", 'L', squares[7][9].getLetter());
        assertEquals("Tile 'L' should be placed at 7,10", 'L', squares[7][10].getLetter());
        assertEquals("Tile 'O' should be placed at 7,11", 'O', squares[7][11].getLetter());
    }

    @Test
    public void testPlayMoveVertical() {
        // Set up a sample move.
        Tile[] tiles = { new Tile('H', 4), new Tile('I', 1) };
        String word = "HI";
        String location = "H8";

        // Play the move.
        int score = board.playMove(tiles, word, location);

        // Check if score is as expected.
        int expectedScore = 4 + 1;
        assertEquals("Score should be calculated based on tile values", expectedScore, score);

        // Check if tiles are placed correctly on the board.
        Square[][] squares = board.getBoard();
        assertEquals("Tile 'H' should be placed at 7,7", 'H', squares[7][7].getLetter());
        assertEquals("Tile 'I' should be placed at 8,7", 'I', squares[8][7].getLetter());
    }

    @Test
    public void testIsValidMove() {
        // Set up a valid move.
        Tile[] tiles = { new Tile('W', 4), new Tile('O', 1), new Tile('R', 1), new Tile('D', 2) };
        String word = "WORD";
        String location = "8H";

        // Check that move is valid.
        assertTrue("The move should be valid", board.isValidMove(tiles, word, location));

        // Set up an invalid move that extends off the board.
        String invalidLocation = "15H";
        assertFalse("The move should be invalid as it extends off the board", board.isValidMove(tiles, word, invalidLocation));
    }

    @Test
    public void testStringToLocation() throws Exception{
        // StringToLocation() is private, so bypassing access is needed.

        // Access the private method stringToLocation using reflection.
        Method method = Board.class.getDeclaredMethod("stringToLocation", String.class);
        method.setAccessible(true); // Make the private method accessible.

        // Test cases
        Object result1 = method.invoke(board, "A1");
        assertArrayEquals(new int[]{0, 0, 0}, (int[]) result1); // A1 corresponds to row 0, column 0, direction HORIZONTAL (0).

        Object result2 = method.invoke(board, "1A");
        assertArrayEquals(new int[]{1, 0, 0}, (int[]) result2); // 1A corresponds to row 0, column 0, direction VERTICAL (1).

        Object result3 = method.invoke(board, "10H");
        assertArrayEquals(new int[]{0, 9, 1}, (int[]) result3); // 10H corresponds to row 9, column 0, direction HORIZONTAL (0).

    }

    @Test
    public void testToString() {
        // Test if toString correctly formats the board.
        String boardString = board.toString();

        // Verify the format of the output string.
        assertTrue("Board should contain column headers", boardString.contains("A B C D E F G H I J K L M N O"));
        assertTrue("Board should contain row labels", boardString.contains("1| | | | | | | | | | | | | | | |"));
        assertTrue("Board should contain lines separating rows", boardString.contains("------------------------------"));
    }
}