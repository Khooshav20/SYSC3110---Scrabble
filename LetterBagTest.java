import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * Tests the letterBag class, note the testShuffle() method as
 * shuffle() is a private method in letterBag.
 *
 * @version 08/11/2024
 */

public class LetterBagTest {
    private LetterBag letterBag;

    @Before
    public void setUp() throws IOException {
        letterBag = new LetterBag();
    }

    @Test
    public void testLetterBagInitialization() {
        // Assuming tiles.txt contains a sufficient number of tiles.
        assertTrue("Letter bag should not be empty after initialization.", letterBag.getSize() > 0);
    }

    @Test
    public void testShuffle() throws IOException {
        // testShuffle() has to implemented in an awkward way because shuffle() is private,
        // this was done by initializing a new letterBag, which achieves the same requirement.

        // Step 1: Create an initial LetterBag instance to get the initial order.
        LetterBag initialBag = new LetterBag();
        Tile[] initialTiles = initialBag.getTiles(initialBag.getSize()); // Get all tiles to examine order.

        // Step 2: Create a new LetterBag instance, which should be shuffled.
        LetterBag shuffledBag = new LetterBag();
        Tile[] shuffledTiles = shuffledBag.getTiles(shuffledBag.getSize()); // Get all tiles again.

        // Step 3: Check if the order is different (non-deterministic but expected to differ).
        boolean isOrderDifferent = false;
        for (int i = 0; i < initialTiles.length; i++) {
            if (initialTiles[i].getLetter() != shuffledTiles[i].getLetter()) {
                isOrderDifferent = true;
                break;
            }
        }

        // Assert that the order should be different after shuffling.
        assertTrue("The order of tiles should differ due to shuffling", isOrderDifferent);
    }

    @Test
    public void testSwapTiles() {
        Tile[] initialTiles = letterBag.getTiles(7);
        Tile[] tilesToCompare = letterBag.getTiles(7);
        int originalBagSize = letterBag.getSize();

        Tile[] swappedTiles = letterBag.swapTiles(initialTiles);

        // Verify the returned tiles are not null and the bag's size remains unchanged.
        assertNotNull("Swapped tiles should not be null", swappedTiles);
        assertEquals("The size of the bag should remain the same after swapping", originalBagSize, letterBag.getSize());

        // Check that the tiles have actually been swapped.
        boolean isDifferent = false;
        for (int i = 0; i < initialTiles.length; i++) {
            if (tilesToCompare[i].getLetter() != swappedTiles[i].getLetter()) {
                isDifferent = true;
                break;
            }
        }

        assertTrue("Swapped tiles should differ from the initial set.", isDifferent);
    }

    @Test
    public void testSwapTilesInsufficientTiles() {
        // Remove all but 6 tiles to simulate an almost-empty bag.
        while (letterBag.getSize() > 6) {
            letterBag.getTiles(1);
        }

        Tile[] tilesToSwap = new Tile[3];
        assertNull("Should return null when trying to swap with less than 7 tiles in bag", letterBag.swapTiles(tilesToSwap));
    }

    @Test
    public void testGetTiles() {
        int numTiles = 5;
        int originalBagSize = letterBag.getSize();

        Tile[] pulledTiles = letterBag.getTiles(numTiles);

        // Verify the correct number of tiles are pulled and bag size decreases.
        assertEquals("Should pull the specified number of tiles", numTiles, pulledTiles.length);
        assertEquals("Bag size should decrease by the number of pulled tiles", originalBagSize - numTiles, letterBag.getSize());
    }

    @Test
    public void testGetSize() {
        int initialSize = letterBag.getSize();
        letterBag.getTiles(3);

        assertEquals("The size should decrease by 3 after pulling 3 tiles", initialSize - 3, letterBag.getSize());
    }
}
