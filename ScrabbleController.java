import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ScrabbleController {
    private ScrabbleGame gameModel;
    private View gameView;
    private int currentPlayer = 0;

    public ScrabbleController(ScrabbleGame model, View view) {
        this.gameModel = model;
        this.gameView = view;

        initController();
    }

    /**
     * Initializes the controller, setting up listeners for user actions in the GUI.
     */
    private void initController() {
        updateView();

        // Setup event listeners
        gameView.getPlayButton().addActionListener(new PlayButtonListener());
        gameView.getExchangeButton().addActionListener(new ExchangeButtonListener());
        gameView.getPassButton().addActionListener(new PassButtonListener());
    }

    /**
     * Updates the view with the current game state.
     */
    private void updateView() {
        gameView.updateBoard(gameModel.getBoard().getBoard());
        gameView.updateLeaderboard(getPlayerScores());
        gameView.updateTileBagCount(gameModel.getLetterBag().getSize());
        gameView.setRack(gameModel.getPlayers()[currentPlayer].getRack());
    }

    /**
     * Retrieves player scores in a format suitable for updating the leaderboard.
     * @return Array of formatted strings representing player scores.
     */
    private String[] getPlayerScores() {
        Player[] players = gameModel.getPlayers();
        String[] scores = new String[players.length];
        for (int i = 0; i < players.length; i++) {
            scores[i] = "Player " + (i + 1) + ": " + players[i].getScore() + " pts";
        }
        return scores;
    }

    /**
     * Advances to the next player.
     */
    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % gameModel.getPlayers().length;
        updateView();
    }

    /**
     * Listener for the "Play" button.
     */
    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String move = gameView.getMoveInput();
            if (gameModel.isValidMove(move, currentPlayer)) {
                int score = gameModel.playMove(move, currentPlayer);
                gameModel.getPlayers()[currentPlayer].addScore(score);
                gameView.displayMessage("Played successfully for " + score + " points.");
                nextPlayer();
            } else {
                gameView.displayMessage("Invalid move. Try again.");
            }
        }
    }

    /**
     * Listener for the "Pass" button.
     */
    private class PassButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameView.displayMessage("Player passed.");
            nextPlayer();
        }
    }
}

 
