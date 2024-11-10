import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests the player class
 *
 * @author Alexander Gardiner, 101261196
 * @version 08/11/2024
 */

public class PlayerTest {
    private Player player;

    // Helper method to create a tile with a given letter and score.
    private Tile createTile(char letter, int score) {
        return new Tile(letter, score);
    }

    @Before
    public void setUp(){
        player = new Player();
    }

    @Test
    public void testHasLetters() {
        Tile[] tiles = { createTile('A', 1), createTile('B', 3), createTile('C', 3) };
        player.addTiles(tiles);

        assertTrue(player.hasLetters("ABC"));
        assertTrue(player.hasLetters("A"));
        assertFalse(player.hasLetters("D"));
        assertFalse(player.hasLetters("AA")); // Not enough 'A's in rack
    }

    @Test
    public void testRemoveLetters() {
        Tile[] tiles = { createTile('A', 1), createTile('B', 3), createTile('C', 3) };
        player.addTiles(tiles);

        Tile[] removedTiles = player.removeLetters("AB");
        assertEquals(2, removedTiles.length);
        assertEquals('A', removedTiles[0].getLetter());
        assertEquals('B', removedTiles[1].getLetter());
        assertFalse(player.hasLetters("A")); // 'A' should be removed.
        assertTrue(player.hasLetters("C"));  // 'C' should still be in rack.
    }

    @Test
    public void testAddTiles() {
        Tile[] tiles = { createTile('A', 1), createTile('B', 3), createTile('C', 3) };
        player.addTiles(tiles);
        assertEquals("Wrong size", 3, player.getNumTiles());
        assertEquals("A is missing", player.getTiles().get(0).getLetter(), 'A');
        assertEquals("B is missing", player.getTiles().get(1).getLetter(), 'B');
        assertEquals("C is missing", player.getTiles().get(2).getLetter(), 'C');
    }

    @Test
    public void testAddScore() {
        player.addScore(10);
        assertEquals(10, player.getScore());

        player.addScore(15);
        assertEquals(25, player.getScore());
    }

    @Test
    public void testGetNumTiles() {
        assertEquals(0, player.getNumTiles()); // No tiles initially.

        player.addTiles(new Tile[] { createTile('A', 1), createTile('B', 3) });
        assertEquals(2, player.getNumTiles());

        player.removeLetters("A");
        assertEquals(1, player.getNumTiles());
    }

    @Test
    public void testGetTiles() {
        Tile[] tiles = { createTile('X', 8), createTile('Y', 4) };
        player.addTiles(tiles);

        ArrayList<Tile> rack = player.getTiles();
        assertEquals("Wrong size", 2, player.getNumTiles());
        assertEquals("X is missing", rack.get(0).getLetter(), 'X');
        assertEquals("Y is missing", rack.get(1).getLetter(), 'Y');
    }

    @Test
    public void testGetPenalty() {
        Tile[] tiles = { createTile('A', 1), createTile('B', 3), createTile('C', 3) };
        player.addTiles(tiles);

        assertEquals(7, player.getPenalty()); // Sum of tile scores (1 + 3 + 3).

        player.removeLetters("A"); // Removes the 'A' tile with score 1.
        assertEquals(6, player.getPenalty()); // Sum of remaining tile scores (3 + 3).
    }
}