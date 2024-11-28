import java.io.Serializable;

/**
 * The Tile class keeps track of the letter and score of a particular tile.
 * 
 * @author Marc Fernandes (101288346)
 * @version 22/10/2024
 */

public class Tile implements Square, Serializable{
    protected char letter;
    private int score;

    /**
     * Create a new tile with a letter and score.
     * @param letter the letter for the tile
     * @param score the score of this tile
     * @author Marc Fernandes
     */
    public Tile(char letter, int score) {
        super();
        this.letter = letter;
        this.score = score;
    }

    /**
     * Gets the letter of this tile
     * @return the letter of this tile
     * @author Marc Fernandes
     */
    public char getLetter() {
        return letter;
    }

    /**
     * Gets the score of this tile
     * @return the score of this tile
     * @author Marc Fernandes
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns whether a Tile is equal to another Tile.
     * @return whether Tile is equal to another Tile.
     */
    public boolean equals(Object o) {
        if (!(o instanceof Tile)) return false;
        Tile otherTile = (Tile) o;
        return otherTile.score == this.score && otherTile.letter == this.letter;
    }
}
