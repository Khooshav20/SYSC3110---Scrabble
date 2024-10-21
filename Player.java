import java.util.ArrayList;
/**
 * The player class represents a player in a Scrabble game. Each player has a rack of
 * tiles and a score.
 *
 * @author Alexander Gardiner, 101261196
 * @version 20/10/2024
 */
public class Player {
    private ArrayList<Tile> rack; // Holds the player's tiles
    private int score; // Player's score

    /**
     * Constructs a new Player with an empty tile rack and a score of zero.
     */
    public Player() {
        this.rack = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Checks if the player's tile rack contains the letters needed to form the
     * specified word.
     *
     * @param s the word to check if it can be formed from the player's tiles
     * @return true if the player has all the letters needed to form the word, false
     *         if otherwise
     */
    public boolean hasLetters(String s) {
        // Create a temporary string to hold the current rack letters
        StringBuilder tempRack = new StringBuilder();
        for (Tile tile : rack) {
            tempRack.append(tile.getLetter());
        }

        // Check if each letter in the word exists in the player's rack
        for (char letter : s.toCharArray()) {
            int index = tempRack.indexOf(String.valueOf(letter));
            if (index == -1) {
                return false; // If any letter in 's' is missing, return false
            }
            tempRack.deleteCharAt(index); // Remove used letter from tempRack
        }
        return true; // All letters are available
    }

    /**
     * Removes the specified letters from the player's tile rack.
     *
     * @param s, the string of letters to be removed from the players tiles
     * @return an array of Tile objects representing the removed tiles
     */
    public Tile[] removeLetters(String s) {
        ArrayList<Tile> removedTiles = new ArrayList<>();
        // For ech letter in the string, find and remove the corresponding tile
        for (char letter : s.toCharArray()) {
            for (int i = 0; i < rack.size(); i++) {
                if (rack.get(i).getLetter() == letter) {
                    removedTiles.add(rack.remove(i)); // Remove and store the tile
                    break;
                }
            }
        }
        return removedTiles.toArray(new Tile[0]);
    }

    /**
     * Adds the specified array of tiles to the player's rack.
     *
     * @param tiles, an array of Tile objects to be added to the player's rack
     */
    public void addTiles(Tile[] tiles) {
        for (Tile tile : tiles) {
            rack.add(tile);
        }
    }

    /**
     * Adds the specified score to the player's total score.
     *
     * @param score, the score to add to the player's total
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Return the player's current total score.
     *
     * @return the player's score
     */
    public int getScore() {
        return score;
    }
}
