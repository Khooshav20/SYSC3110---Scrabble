import java.util.ArrayList;
import java.util.List;

/**
 * AIPlayer represents an automated player in the Scrabble game.
 * This class extends the Player class and implements logic for generating
 * and executing moves based on the current state of the game board. The AI
 * evaluates all possible moves and chooses the one that maximizes its score.
 *
 *
 * @author Khooshav Bundhoo (101132063)
 */
public class AIPlayer extends Player {
    public AIPlayer(String name) {
        super(name, true); /
    }

    /**
     * Generates the best possible move for the AI player based on the current state
     * of the board.
     * 
     * @param board The current game board
     * @return The best Move the AI can make, or null if no valid move is possible
     */
    public Move generateBestMove(Board board) {
        Move bestMove = null;
        int bestScore = 0;

        // Iterate over the board and evaluate all possible moves
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                for (boolean horizontal : new boolean[] { true, false }) {
                    // Generate subsets of tiles from the rack to simulate possible words
                    List<Tile> tiles = getTiles();
                    for (int subsetSize = 1; subsetSize <= tiles.size(); subsetSize++) {
                        List<Tile> subset = tiles.subList(0, subsetSize);

                        // Check if the move is valid and calculate its score
                        Tile[] tilesArray = subset.toArray(new Tile[0]);
                        int score = board.calculateScore(tilesArray, row, col, horizontal);
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = new Move(row, col, tilesArray, horizontal);
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    /**
     * Executes the move chosen by the AI.
     * 
     * @param board The current game board
     * @return True if the move was successfully executed, false otherwise
     */
    public boolean playMove(Board board) {
        Move bestMove = generateBestMove(board);
        if (bestMove != null) {
            return board.placeTiles(bestMove.tiles, bestMove.row, bestMove.col, bestMove.horizontal);
        }
        return false; // No valid move found
    }
}

/**
 * Move represents a potential action that can be performed by the AIPlayer.
 * It contains the position, direction, and tiles involved in the move.
 */
class Move {
    int row;
    int col;
    Tile[] tiles;
    boolean horizontal;

    public Move(int row, int col, Tile[] tiles, boolean horizontal) {
        this.row = row;
        this.col = col;
        this.tiles = tiles;
        this.horizontal = horizontal;
    }
}