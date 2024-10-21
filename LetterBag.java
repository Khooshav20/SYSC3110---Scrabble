import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 * The LetterBag class creates a bag of shuffled Tiles to use for the Scrabble game.
 * Allows users to create a bag of Tiles, swaps Tiles and get multiple Tiles at once.
 * 
 * @author Marc Fernandes
 * @version 20/10/2024
 */

public class LetterBag {
    private ArrayList<Tile> letters;

    /**
     * Creates a new LetterBag containing every tile to use for the game.
     * 
     * @throws FileNotFoundException if tiles.txt is not found in the same folder
     */
    public LetterBag() throws FileNotFoundException {
        // read from tiles.txt and add every tile
        File file = new File("tiles.txt");
        Scanner reader = new Scanner(file);
        while (reader.hasNextLine()) {
            // read next line, tokenize the line and add a new tile from the tokens
            String line = reader.nextLine();
            String[] tokens = line.split(",");
            for (int i = 0; i < Integer.parseInt(tokens[1]); i++) {
                letters.add(new Tile(tokens[0].charAt(0), Integer.parseInt(tokens[2])));
            }
        }

        // close reader and shuffle
        reader.close();
        shuffle();
    }

    /**
     * Shuffles the list of tiles in the bag.
     */
    private void shuffle() {
        Random r = new Random();

        // in-place shuffle
        int m = this.letters.size();
        while (m != 0) {
            int i = r.nextInt(m--);

            Tile t = this.letters.get(i);
            this.letters.set(i, this.letters.get(m));
            this.letters.set(m, t);
        }
    }

    /**
     * Swaps a set of tiles between the bag and given parameter
     * and returns the new set of tiles. If there are less than 7
     * letters in the bag, the function will return null.
     * 
     * @param tiles the tiles to swap with the bag
     * @return the new tiles swapped from the bag
     */
    public Tile[] swapTiles(Tile[] tiles) {
        // swapping when theres less than 7 letters in the bag is not allowed
        if (letters.size() < 7) {
            return null;
        }

        // swap for each element in tiles array
        for (int i = 0; i < tiles.length; i++) {
            Tile temp = tiles[i];
            tiles[i] = letters.get(i);
            letters.set(i, temp);
        }

        // shuffle the bag to ensure randomness
        shuffle();

        return tiles;
    }

    /**
     * Gets the specified amount of tiles and returns a list of
     * num tiles.
     * @param num the amount of tiles to take out of the bag
     * @return a list of tiles pulled from the bag
     */
    public Tile[] getTiles(int num) {
        // initialize a new array of tiles length num
        Tile[] tiles = new Tile[num];
        // continuously pull from the bag and add to array of tiles
        for (int i = 0; i < num; i++) {
            tiles[i] = letters.remove(letters.size() - 1);
        }

        return tiles;
    }

    /**
     * Gets the size of the bag.
     * @return the size of the bag
     */
    public int getSize() {
        return this.letters.size();
    }
}
