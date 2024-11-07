import javax.swing.*;
import java.awt.*;

// ScrabbleView class that extends JFrame to create the main game window
//
// @author Khooshav Bundhoo
public class ScrabbleView extends JFrame {
    private Board board;           // The game board model that holds the state of the game
    private JLabel statusLabel;

    // Constructor to initialize the ScrabbleView
    public ScrabbleView(Board board) {
        this.board = board;  // Set the board model

        // Set up the main window properties
        setTitle("Scrabble Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());  // Use BorderLayout for layout management

        // Create the board panel, which will display the game board
        JPanel boardPanel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//
//                // Loop through each tile on the board and draw it
//                //to complete
//                for (int i = 0; i < Board.SIZE; i++) {
//                    for (int j = 0; j < Board.SIZE; j++) {
//                        Tile tile = board.getTile(i, j);  // Get the tile at the current position
//                        if (tile != null) {
//                            // Draw the tile's rectangle and letter if it exists
//                            g.setColor(Color.BLACK);
//                           //to complete
//                        } else {
//                            // Draw an empty square if no tile is present
//                            g.setColor(Color.LIGHT_GRAY);
//                            //to complete
//                        }
//                    }
//                }
//            }
       };

        // Set the preferred size for the board panel
        boardPanel.setPreferredSize(new Dimension(600, 600));
        add(boardPanel, BorderLayout.CENTER);  // Add the board panel to the center of the window

        // Create a status label to show game messages and add it to the window
        statusLabel = new JLabel("Welcome to Scrabble!");
        add(statusLabel, BorderLayout.SOUTH);  // Add the label to the bottom of the window

        // Make the window visible
        setVisible(true);
    }

    // Method to refresh the board and update the display
    public void updateBoard() {
        repaint();  // Call repaint to redraw the entire frame, including the board panel
    }

    // Method to update the status label with a new message
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}
