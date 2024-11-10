import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener{
    private JPanel mainPanel;
    
    
    private static Font font = new Font("Comic Sans MS", Font.BOLD, 20);

    private JPanel boardPanel;
    private JButton[][] boardButtons;

    private JPanel rackPanel;
    private JButton[] rackButtons;

    private JPanel LBTileColumn;

    private ArrayList<JLabel> scoreLabels;
    private JPanel leaderboardPanel;

    private JButton playButton, passButton, swapButton;
    
    private JButton currentButton;

    private JTextArea bagLabel;

    ScrabbleController sc;

    public View(Square[][] board) throws IOException {
        setTitle("SYSC3110 Scrabble - Group 17");
        setSize(600, 600);
        setResizable(false);
        

        // MAIN BOARD

        boardButtons = new JButton[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                boardButtons[i][j] = new JButton(" ");
                boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                boardButtons[i][j].setFont(font);
                boardButtons[i][j].addActionListener(e -> {
                    System.out.println("balls");
                    JButton buttonSource = (JButton) e.getSource();
                        if (currentButton != null){
                            String temp = buttonSource.getText();
                            buttonSource.setText(currentButton.getText());
                                setBlanks(false);
                                for (int k = 0; k < 7; k++){
                                    if (currentButton == rackButtons[k]){
                                        rackButtons[k].setText(temp);
                                        rackButtons[k].setEnabled(!temp.equals(" "));
                                        currentButton = null;
                                        updateDisplay();
                                        break;
                                    }
                                }
                        } else {
                            if (!buttonSource.getText().equals(" ")){
                                for (int k = 0; k < 7; k++){
                                    if (rackButtons[k].getText().equals(" ")){
                                        rackButtons[k].setText(buttonSource.getText());
                                        rackButtons[k].setEnabled(true);
                                        buttonSource.setText(" ");
                                        currentButton = null;
                                        updateDisplay();
                                        break;
                                    }
                                }
                            }
                        }
                        
                });
                //l.setSize(25, 25);
                boardButtons[i][j].setEnabled(false);
            }
        }
        
        // HAND
        rackButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            rackButtons[i] = new JButton((char)(i + 'A') + "");
            rackButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            rackButtons[i].setFont(font);
            rackButtons[i].addActionListener(e -> {
                currentButton = (JButton) e.getSource();
                setBlanks(true);
            });
        }


        //updateDisplay();
        sc = new ScrabbleController(this, getPlayers());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void updateDisplay(){
        if (mainPanel != null) remove(mainPanel);
        // MAIN PANEL
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // BOARD
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(15, 15));
        boardPanel.setSize(450 ,450);
        boardPanel.setLocation(115, 20);
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                if (boardButtons[i][j].getText().equals(" ")) {
                    boardButtons[i][j].setEnabled(false);
                }
                boardPanel.add(boardButtons[i][j]);
            }
        }
        mainPanel.add(boardPanel);
        
        // RACK
        rackPanel = new JPanel();
        rackPanel.setLayout(new GridLayout(1, 7));
        rackPanel.setSize(210, 30);
        rackPanel.setLocation(355, 490);
        for (JButton b: rackButtons){
            rackPanel.add(b);
        }
        mainPanel.add(rackPanel);

        LBTileColumn = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;

        // LEADERBOARD
        leaderboardPanel = new JPanel(new GridLayout(0, 1));
        leaderboardPanel.setBackground(new Color(0xDFB3DF));
        for (JLabel l: scoreLabels) leaderboardPanel.add(l);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 2;
        LBTileColumn.add(leaderboardPanel, c);

        // TILE
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 50;
        LBTileColumn.add(bagLabel, c);
        LBTileColumn.setLocation(20, 20);

        // PLAYER DECISION BUTTONS
        playButton = new JButton("PLAY");
        playButton.setBounds(20, 490, 100, 30);
        playButton.addActionListener(this);
        playButton.setFont(font);
        mainPanel.add(playButton);

        passButton = new JButton("PASS");
        passButton.setBounds(130, 490, 100, 30);
        passButton.addActionListener(this);
        passButton.setFont(font);
        mainPanel.add(passButton);

        swapButton = new JButton("SWAP");
        swapButton.setBounds(240, 490, 100, 30);
        swapButton.addActionListener(this);
        swapButton.setFont(font);
        mainPanel.add(swapButton);
        
        LBTileColumn.setSize(90, 450);
        LBTileColumn.setBackground(Color.PINK);
        mainPanel.add(LBTileColumn);
        mainPanel.setBackground(Color.PINK);
        add(mainPanel);
    }

    private void setBlanks(boolean enabled) {
        for (JButton[] rowButtons: boardButtons) {
            for (JButton button: rowButtons) {
                if (button.getText().equals(" ")) button.setEnabled(enabled);
            }
        }
    }

    private Point getPlacedTile() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardButtons[i][j].isEnabled() && !boardButtons[i][j].getText().equals(" ")) {
                    return new Point(j, i);
                }
            }
        }
        return new Point(-1, -1);
    }

    private int getNumTilesPlaced() {
        int i = 0;
        for (JButton[] buttonRow: boardButtons) {
            for (JButton b: buttonRow) {
                if (b.isEnabled() && !b.getText().equals(" ")) i++;
            }
        }
        return i;
    }

    private void setRack(ArrayList<Tile> rack) {
        for (JButton b: rackButtons) {
            b.setEnabled(false);
            b.setText(" ");
        }
        for (int i = 0; i < rack.size(); i++) {
            rackButtons[i].setText(rack.get(i).getLetter() + "");
            rackButtons[i].setEnabled(true);
        }
    }

    public int getPlayers() {
        boolean valid = true;
        int input = -1;
        do {
            if (!valid) {
                JOptionPane.showMessageDialog(this, "Invalid input, please try again.");
            }
            valid = true;
            try {
                input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the amount of players to play Scrabble (2-4)"));
                if (input > 4 || input < 2) {
                    valid = false;
                }
            }
            catch (Exception e) {
                valid = false;
            }
        } while (!valid);
        return input;
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        new View(board.getBoard());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            Point p = getPlacedTile();
            int numTilesPlaced = getNumTilesPlaced();

            if (p.x < 0 || p.y < 0 || numTilesPlaced == 0) return;
            int numTilesSeen = 0;
            
            for (int i = p.x; i < 15 && !boardButtons[p.y][i].getText().equals(" "); i++) numTilesSeen += boardButtons[p.y][i].isEnabled() ? 1: 0;

            boolean played = false;
            if (numTilesSeen == numTilesPlaced) {
                int firstLetter;
                for (firstLetter = p.x; firstLetter >= 0 && !boardButtons[p.y][firstLetter].getText().equals(" "); firstLetter--);
                firstLetter++;

                System.out.println("FIRST LETTER: " + firstLetter);

                int[] location = new int[3];
                location[Board.DIRECTION] = Board.HORIZONTAL;
                location[Board.COLUMN] = firstLetter;
                location[Board.ROW] = p.y;

                StringBuilder sbWord = new StringBuilder();
                StringBuilder sbLetters = new StringBuilder();
                for (int i = firstLetter; i < 15 && !boardButtons[p.y][i].getText().equals(" "); i++) {
                    String text = boardButtons[p.y][i].getText();
                    if (boardButtons[p.y][i].isEnabled()) {
                        sbLetters.append(text);
                        sbWord.append(text);
                    }
                    else {
                        sbWord.append(text.toLowerCase());
                    }
                }

                played = sc.play(sbLetters.toString(), sbWord.toString(), location);
            }
            numTilesSeen = 0;
            
            for (int i = p.y; i < 15 && !boardButtons[i][p.x].getText().equals(" "); i++) numTilesSeen += boardButtons[i][p.x].isEnabled() ? 1: 0;
            System.out.println(numTilesSeen + " " + numTilesPlaced);
            if (numTilesSeen == numTilesPlaced && !played) {
                int firstLetter;
                for (firstLetter = p.y; firstLetter >= 0 && !boardButtons[firstLetter][p.x].getText().equals(" "); firstLetter--);
                firstLetter++;
                
                System.out.println("FIRST LETTER: " + firstLetter);
                int[] location = new int[3];
                location[Board.DIRECTION] = Board.VERTICAL;
                location[Board.COLUMN] = p.x;
                location[Board.ROW] = firstLetter;

                StringBuilder sbWord = new StringBuilder();
                StringBuilder sbLetters = new StringBuilder();
                for (int i = firstLetter; i < 15 && !boardButtons[i][p.x].getText().equals(" "); i++) {
                    String text = boardButtons[i][p.x].getText();
                    if (boardButtons[i][p.x].isEnabled()) {
                        sbLetters.append(text);
                        sbWord.append(text);
                    }
                    else {
                        sbWord.append(text.toLowerCase());
                    }
                    
                }

                played = sc.play(sbLetters.toString(), sbWord.toString(), location);
            }
            if (!played){
                JOptionPane.showMessageDialog(this, "The word could not be played.");
            }
        }
        else if (e.getSource() == passButton) {
            sc.pass();
        }
        else if (e.getSource() == swapButton) {
            String input = JOptionPane.showInputDialog(this, "Please enter the tiles you want to swap");
            while (!sc.swap(input)) {
                JOptionPane.showMessageDialog(this, "Incorrect input, try again");
                input = JOptionPane.showInputDialog(this, "Please enter the tiles you want to swap");
            }
        }
    }

    public void handleScrabbleStatusUpdate(ScrabbleEvent se) {
        // LEADERBOARD
        scoreLabels = new ArrayList<JLabel>();
        scoreLabels.add(new JLabel("Leaderboard:"));
        Player[] players = se.getPlayers();
        for (int i = 0; i < players.length; i++) {
            JLabel temp = new JLabel("Player " + (i + 1) + ": " + players[i].getScore());
            if (i == se.getCurrentPlayer()) {
                temp.setOpaque(true);
                temp.setBackground(new Color(150, 255, 150));
            }
            scoreLabels.add(temp);
        }

        setRack(players[se.getCurrentPlayer()].getTiles());
        
        // TILE
        bagLabel = new JTextArea("Bag has " + se.getNumLetters() + " tiles remaining");
        bagLabel.setLineWrap(true);
        bagLabel.setWrapStyleWord(true);
        bagLabel.setEditable(false);
        bagLabel.setBackground(Color.PINK);

        // DISABLE PLACED TILES
        for (JButton[] rowButtons: boardButtons) {
            for (JButton button: rowButtons) {
                if (!button.getText().equals(" ")) button.setEnabled(false);
            }
        }

        updateDisplay();
    }

    public void endGame(ScrabbleEvent se) {
        Player[] players = se.getPlayers();
        String finalString = "";

        //obtain scores
        Integer[] scores = new Integer[players.length];
        for (int i = 0; i < players.length; i++){
            scores[i] = (players[i].getScore());
        }
        
        finalString += "Final Leaderboard:\n";
        int previousPlacement = 1;
        int previousScore = Collections.min(Arrays.asList(scores));
        for (int i = 1; i <= players.length; i++){
            int score = Collections.max(Arrays.asList(scores));
            int index = findIndex(scores, score);
            if (previousScore == score){
                finalString += previousPlacement + ": Player " + (index + 1) + " with a score of " + players[index].getScore() + "\n";
            } else {
                finalString += i + ": Player " + (index + 1) + " with a score of " + players[index].getScore() + "\n";
                previousPlacement = i + 1;
                previousScore = score;
            }
            players[index].addScore(-10000);
            scores[index] -= 10000;
        }

        JOptionPane.showMessageDialog(this, finalString);
        dispose();
    }

    
    /**
     * Finds the first index of an integer in an array of integers, returning -1 if it is not present.
     * 
     * @param a The array of integers to be searched
     * @param value the integer being searched for
     * @return the index of the first appearance of the value
     */
    private int findIndex(Integer[] a, int value){
        for (int i = 0; i < a.length; i++) if (a[i] == value) return i;
        return -1;
    }
}
