import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class BoardTest {
    private Board board;
    int ROW = 0;
    int COLUMN = 1;
    int DIRECTION = 2;
    int VERTICAL = 0;
    int HORIZONTAL = 1;

    // Utilize Board's stringToLocation() method here.
    // Unique one implemented here as to not interfere with
    // main codebase and save space in implementation.
    private int[] testStringToLocation(String s) {
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

    @Before
    public void setUp() throws IOException {
        board = new Board();
    }

    @Test
    public void testBoardInitialization() {
        Square[][] boardArray = board.getBoard();
        assertNotNull("Board should be initialized", boardArray);
        assertEquals("Board should be 15x15", 15, boardArray.length);
        assertTrue("All squares should be initialized as BlankSquare", boardArray[0][0] instanceof BlankSquare);
    }

    @Test
    public void testBlankSquaresPresence() {
        Square[][] boardArray = board.getBoard();
        assertTrue("Top-left square should be a BlankSquare", boardArray[0][0] instanceof BlankSquare);
        assertTrue("Center square should be a BlankSquare", boardArray[7][7] instanceof BlankSquare);
    }

    @Test
    public void testPremiumTilesPresence() {
        Square[][] boardArray = board.getBoard();

        // Example locations for premium tiles; adjust according to your board configuration.
        int[][] premiumTileLocations = {
                {0, 0}, {8, 6}, {14, 14}, {0, 7}, {7, 0}
        };

        for (int[] location : premiumTileLocations) {
            int row = location[ROW];
            int col = location[COLUMN];
            assertTrue("Square at (" + row + ", " + col + ") should be a PremiumTile", boardArray[row][col] instanceof PremiumTile);

            PremiumTile premiumTile = (PremiumTile) boardArray[row][col];
            assertTrue("PremiumTile multiplier should be > 1", premiumTile.getMultiplier() > 1);
        }
    }

    @Test
    public void testPremiumTileProperties() {
        Square[][] boardArray = board.getBoard();

        // Example location for a premium tile
        int row = 0, col = 0;
        if (boardArray[row][col] instanceof PremiumTile) {
            PremiumTile premiumTile = (PremiumTile) boardArray[row][col];
            assertEquals("PremiumTile multiplier should match", 3, premiumTile.getMultiplier());
            assertTrue("PremiumTile should be a word multiplier", premiumTile.getIsWord());
        }
    }

    @Test
    public void testStringToLocationHorizontal() {
        int[] location = testStringToLocation("K15");
        assertEquals(Board.HORIZONTAL, location[Board.DIRECTION]);
        assertEquals(14, location[Board.ROW]);
        assertEquals(10, location[Board.COLUMN]);
    }

    @Test
    public void testStringToLocationVertical() {
        int[] location = testStringToLocation("15K");
        assertEquals(Board.VERTICAL, location[Board.DIRECTION]);
        assertEquals(14, location[Board.ROW]);
        assertEquals(10, location[Board.COLUMN]);
    }

    @Test
    public void testValidMoveOnEmptyBoard() {
        Tile[] tiles = { new Tile('T', 1), new Tile('E', 1), new Tile('S', 1), new Tile('T', 1) };
        int[] location = testStringToLocation("H8");
        assertTrue("Valid move for starting position", board.isValidMove(tiles, "TEST", location));
    }

    @Test
    public void testPlayMoveAndScore() {
        Tile[] tiles = { new Tile('H', 4), new Tile('E', 2), new Tile('L', 2), new Tile('L', 1), new Tile('O', 2) };
        int[] location = testStringToLocation("8H");
        // Due to premium tiles:
        // Letter at [8][8] ('H') gives all letters x2 score
        // Letter at [8][12] ('O') gives the 'O' x2 score
        int score = board.playMove(tiles, "HELLO", location);
        assertEquals("Score should be calculated correctly", 26, score);  // Adjust based on scoring logic
    }

    @Test
    public void testInvalidMoveExtendingOffBoard() {
        Tile[] tiles = { new Tile('L', 1), new Tile('O', 1), new Tile('N', 1), new Tile('G', 2) };
        int[] location = testStringToLocation("15H");

        assertFalse("Move should be invalid for extending off board", board.isValidMove(tiles, "LONG", location));
    }

    @Test
    public void testInvalidMoveNotConnected() {
        Tile[] tiles = { new Tile('A', 1), new Tile('P', 3), new Tile('P', 3), new Tile('L', 1), new Tile('E', 1) };
        int[] location = testStringToLocation("5D");

        assertFalse("Move should be invalid for not being connected", board.isValidMove(tiles, "APPLE", location));
    }

    @Test
    public void testToStringBoard() {
        String boardString = board.toString();
        assertNotNull("Board string should not be null", boardString);
        assertTrue("Board string should contain column labels", boardString.contains("A B C D E F G H I J K L M N O"));
    }
}
