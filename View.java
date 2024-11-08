import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    
    private static Font font = new Font("Comic Sans MS", Font.BOLD, 20);
    private JLabel[] tiles;
    private JPanel leaderboardPanel;

    public View(Square[][] board) {
        tiles = new JLabel[7];
        for (int i = 0; i < 7; i++) {
            tiles[i] = new JLabel("N", SwingConstants.CENTER);
            tiles[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            tiles[i].setFont(font);
        }

        setTitle("SYSC3110 Scrabble - Group 17");
        setSize(600, 600);
        setResizable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // MAIN BOARD
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(15, 15));
        boardPanel.setSize(450 ,450);
        boardPanel.setLocation(115, 20);
        for (Square[] row: board) {
            for (Square s: row) {
                JLabel l = new JLabel("A", SwingConstants.CENTER);
                l.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                l.setFont(font);
                //l.setSize(25, 25);
                boardPanel.add(l);
            }
        }
        mainPanel.add(boardPanel);

        // HAND
        JPanel rackPanel = new JPanel();
        rackPanel.setLayout(new GridLayout(1, 7));
        rackPanel.setSize(210, 30);
        rackPanel.setLocation(355, 490);
        for (JLabel l: tiles) {
            rackPanel.add(l);
        }
        mainPanel.add(rackPanel);

        JPanel LBTileColumn = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;

        // LEADERBOARD
        leaderboardPanel = new JPanel(new GridLayout(0, 1));
        leaderboardPanel.add(new JLabel("Player 1: 500 pts"));
        leaderboardPanel.add(new JLabel("Player 1: 500 pts"));
        leaderboardPanel.add(new JLabel("Player 1: 500 pts"));
        leaderboardPanel.add(new JLabel("Player 1: 500 pts"));
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 2;
        LBTileColumn.add(leaderboardPanel, c);

        // TILE
        JTextArea bagLabel = new JTextArea("Bag has 69 tiles remaining");
        bagLabel.setLineWrap(true);
        bagLabel.setWrapStyleWord(true);
        bagLabel.setEditable(false);
        bagLabel.setBackground(Color.PINK);
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 50;
        LBTileColumn.add(bagLabel, c);
        LBTileColumn.setLocation(20, 20);

        LBTileColumn.setSize(90, 450);
        LBTileColumn.setBackground(Color.PINK);
        mainPanel.add(LBTileColumn);
        mainPanel.setBackground(Color.PINK);
        add(mainPanel);

        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void setRack(ArrayList<Tile> rack) {
        for (int i = 0; i < rack.size(); i++) {
            tiles[i].setText(rack.get(i).getLetter() + "");
        }
    }

    public static int getPlayers() {
        return 2; // TODO
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        new View(board.getBoard());
    }
}
