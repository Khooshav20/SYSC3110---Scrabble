import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the tile class.
 *
 * @version 08/11/2024
 */

public class TileTest {

    @Test
    public void testConstructor() {
        Tile tile = new Tile('A', 1);

        // Verify that the letter and score are correctly assigned.
        assertEquals("The letter should be 'A'", 'A', tile.getLetter());
        assertEquals("The score should be 1", 1, tile.getScore());
    }

    @Test
    public void testGetLetter() {
        Tile tile = new Tile('B', 3);

        // Check that getLetter returns the correct letter.
        assertEquals("The letter should be 'B'", 'B', tile.getLetter());
    }

    @Test
    public void testGetScore() {
        Tile tile = new Tile('C', 5);

        // Check that getScore returns the correct score.
        assertEquals("The score should be 5", 5, tile.getScore());
    }

    @Test
    public void testEqualsSameTile() {
        Tile tile1 = new Tile('D', 2);
        Tile tile2 = new Tile('D', 2);

        // Verify that two tiles with the same letter and score are equal.
        assertTrue("Tiles with the same letter and score should be equal", tile1.equals(tile2));
    }

    @Test
    public void testEqualsDifferentTile() {
        Tile tile1 = new Tile('E', 4);
        Tile tile2 = new Tile('F', 4);
        Tile tile3 = new Tile('E', 6);

        // Verify that tiles with different letters or scores are not equal.
        assertFalse("Tiles with different letters should not be equal", tile1.equals(tile2));
        assertFalse("Tiles with different scores should not be equal", tile1.equals(tile3));
    }

    @Test
    public void testEqualsDifferentObject() {
        Tile tile = new Tile('G', 7);
        String nonTileObject = "NotATile";

        // Verify that a Tile is not equal to a non-Tile object.
        assertFalse("Tile should not be equal to a non-Tile object", tile.equals(nonTileObject));
    }
}