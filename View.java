import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A View that allows Scrabble to have a Graphical User Interface (GUI).
 * 
 * @author Marc Fernandes
 * @author Lucas Warburton
 * @version 10/11/2024
 */
public class View extends JFrame implements ActionListener{
    private JPanel mainPanel;
    
    private static Font font = new Font("Comic Sans MS", Font.BOLD, 17);

    private JPanel boardPanel;
    private JButton[][] boardButtons;

    private JPanel rackPanel;
    private JButton[] rackButtons;

    JMenuBar menubar;
    JMenu fileMenu;
    JMenuItem loadItem;
    JMenuItem saveItem;

    private JPanel LBTileColumn;

    private ArrayList<JLabel> scoreLabels;
    private JPanel leaderboardPanel;

    private JButton playButton, passButton, swapButton;
    
    private JButton currentButton;

    private JTextArea bagLabel;

    private Square[][] referenceBoard; 

    ScrabbleController sc;

    /**
     * Creates a new view with a window and all necessary 
     * components to see and interact with the game.
     * 
     * @param board the starting board
     * @throws IOException if tiles.txt or dictionary.txt is not found.
     */
    public View(Square[][] board) throws IOException {
        // set frame parameters
        setTitle("SYSC3110 Scrabble - Group 17");
        setSize(600, 600);
        setResizable(false);
        

        // MAIN BOARD

        boardButtons = new JButton[15][15];
        boardButtons[7][7]= new JButton(" * ");
        boardButtons[7][7].setBackground(new Color(0xff99ff));
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // initialize button and set parameters
                if (board[i][j] instanceof PremiumTile){
                    if (((PremiumTile)board[i][j]).getIsWord()){
                        boardButtons[i][j] = new JButton(((PremiumTile)board[i][j]).getMultiplier() + "W");
                        if (((PremiumTile)board[i][j]).getMultiplier() == 2) boardButtons[i][j].setBackground(new Color(0xff99ff));
                        else boardButtons[i][j].setBackground(new Color(0xff0000));
                    }
                    else{
                        boardButtons[i][j]= new JButton(((PremiumTile)board[i][j]).getMultiplier() + "L");
                        if (((PremiumTile)board[i][j]).getMultiplier() == 2) boardButtons[i][j].setBackground(new Color(0x68ccff));
                        else boardButtons[i][j].setBackground(new Color(0x0033ff));
                    }
                }
                if (boardButtons[i][j] == null){
                    boardButtons[i][j] = new JButton("");
                    boardButtons[i][j].setBackground(new Color(0xffffff));
                }

                boardButtons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                boardButtons[i][j].setFont(font);
                boardButtons[i][j].addActionListener(e -> {
                    // on button click
                    JButton buttonSource = (JButton) e.getSource();

                    // if a button is currently selected
                    if (currentButton != null){
                        String temp = buttonSource.getText();
                        if (currentButton.getText().equals(".")){
                            String s = "";
                            while (s.length() != 1 || !Character.isLetter(s.charAt(0))){
                                s = JOptionPane.showInputDialog("Enter the letter you desire.").toUpperCase();
                            }
                            buttonSource.setText(s);
                            buttonSource.setBackground(new Color(0x89cff0));
                        } else {
                            buttonSource.setText(currentButton.getText());
                            buttonSource.setBackground(new Color(0xffffff));
                        }
                        setBlanks(false);
                        // find selected button and swap
                        for (int k = 0; k < 7; k++){
                            if (currentButton == rackButtons[k]){
                                if (temp.length() == 1) rackButtons[k].setText(temp);
                                else rackButtons[k].setText("");
                                rackButtons[k].setEnabled(temp.length() == 1);
                                rackButtons[k].setBackground(new Color(0xffffff));
                                currentButton = null;
                                break;
                            }
                        }
                    } else {
                        // if there is no button selected and the button clicked is not blank
                        if (buttonSource.getText().length() == 1){
                            // find rack button that is blank and place it back
                            for (int k = 0; k < 7; k++){
                                if (rackButtons[k].getText().equals("")){ //if rack button is empty
                                    if (buttonSource.getBackground().equals(new Color(0x89cff0))){ //if button being replaced is blank
                                        buttonSource.setBackground(new Color(0xffffff));
                                        rackButtons[k].setText(".");
                                        rackButtons[k].setBackground(new Color(0x89cff0));
                                    } else { //button being replaced is regular Tile
                                        rackButtons[k].setText(buttonSource.getText());
                                    }
                                    rackButtons[k].setEnabled(true);
                                    int row = 0;
                                    int col = 0;
                                    for (; row < 15; row++){ //find indices of tile being vacated
                                        for (col = 0; col < 15 && boardButtons[row][col] != buttonSource; col++);
                                        if (col < 15) break;
                                    }
                                    if (row == 7 && col == 7){ //if center tile
                                        buttonSource.setText(" * ");
                                        buttonSource.setBackground(new Color(0xff99ff));
                                    } else if (board[row][col] instanceof PremiumTile){ 
                                        PremiumTile temp = (PremiumTile)board[row][col];
                                        if (temp.getIsWord()){ //if word multiplier
                                            buttonSource.setText(temp.getMultiplier() + "W");
                                            if (((PremiumTile)board[row][col]).getMultiplier() == 2) //if 2x word multiplier
                                                buttonSource.setBackground(new Color(0xff99ff));
                                            else //if 3x word multiplier
                                                buttonSource.setBackground(new Color(0xff0000));
                                        } else { //letter multiplier
                                            buttonSource.setText(temp.getMultiplier() + "L");
                                            if (((PremiumTile)board[row][col]).getMultiplier() == 2) //2x letter multiplier
                                                buttonSource.setBackground(new Color(0x68ccff));
                                            else //3x letter multiplier
                                                buttonSource.setBackground(new Color(0x0033ff));
                                        }
                                    } else //tile being vacated is empty tile
                                        buttonSource.setText("");
                                    buttonSource.setEnabled(false);
                                    currentButton = null;
                                    break;
                                }
                            }
                        }
                    }
                        
                });
                // set disabled on start
                boardButtons[i][j].setEnabled(false);
            }
        }
        
        // HAND
        rackButtons = new JButton[7];
        
        // initialize rack buttons
        for (int i = 0; i < 7; i++) {
            rackButtons[i] = new JButton();
            rackButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            rackButtons[i].setFont(font);
            rackButtons[i].setBackground(new Color(0xffffff));
            rackButtons[i].addActionListener(e -> {
                currentButton = (JButton) e.getSource();
                setBlanks(true);
            });
        }

        // initialize ScrabbleController and show frame
        int numPlayers = getPlayers();
        sc = new ScrabbleController(this, numPlayers, getAIPlayers(numPlayers));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Updates the rack, leaderboard, board, and
     * tile count on the display.
     */
    private void updateDisplay(){
        // if main panel already exists, remake it
        if (mainPanel != null) remove(mainPanel);
        // MAIN PANEL
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // BOARD
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(15, 15));
        boardPanel.setSize(450 ,450);
        boardPanel.setLocation(115, 20);
        // every time display is updated, disable all letters on the board
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

        menubar = new JMenuBar();
        setJMenuBar(menubar);

        fileMenu = new JMenu("File Menu");
        menubar.add(fileMenu);

        saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(this);

        loadItem = new JMenuItem("Load");
        fileMenu.add(loadItem);
        loadItem.addActionListener(this);
        
        LBTileColumn.setSize(90, 450);
        LBTileColumn.setBackground(Color.PINK);
        mainPanel.add(LBTileColumn);
        mainPanel.setBackground(Color.PINK);
        add(mainPanel);
    }

    /**
     * Sets all blanks on board to <code>enabled</code>.
     * @param enabled whether blank spaces should be enabled
     */
    private void setBlanks(boolean enabled) {
        // if there is a blank on the board, set enabled to what is passed
        for (JButton[] rowButtons: boardButtons) {
            for (JButton button: rowButtons) {
                if (button.getText().length() != 1) button.setEnabled(enabled);
            }
        }
    }

    /**
     * Finds the first position in the board where a user-placed tile is located.
     * @return the point which the first placed tile is located, or <code>(-1, -1)</code> if no tiles are placed.
     */
    private Point getPlacedTile() {
        // check board from top left down to find the first placed tile
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardButtons[i][j].isEnabled() && boardButtons[i][j].getText().length() == 1) {
                    return new Point(j, i);
                }
            }
        }
        // if none found, return bogus coordinate
        return new Point(-1, -1);
    }

    /**
     * Iterates through the board to find the number of tiles placed.
     * @return the number of user-placed tiles that are placed
     */
    private int getNumTilesPlaced() {
        int i = 0;
        // iterate through the board and find all tiles placed
        for (JButton[] buttonRow: boardButtons) {
            for (JButton b: buttonRow) {
                if (b.isEnabled() && b.getText().length() == 1) i++;
            }
        }
        return i;
    }

    /**
     * Sets the rack visually to <code>rack</code>.
     * @param rack the player rack containing tiles
     */
    private void setRack(ArrayList<Tile> rack) {
        // reset the rack first
        for (JButton b: rackButtons) {
            b.setEnabled(false);
            b.setText(" ");
        }
        // then set to the rack passed
        for (int i = 0; i < rack.size(); i++) {
            if (rack.get(i) instanceof BlankTile) rackButtons[i].setText(".");
            else rackButtons[i].setText(rack.get(i).getLetter() + "");
            if (rackButtons[i].getText().equals(".")) rackButtons[i].setBackground(new Color(0x89cff0));
            else rackButtons[i].setBackground(new Color(0xffffff));
            rackButtons[i].setEnabled(true);
        }
    }

    /**
     * Asks the user to input the amount of players that are playing 
     * the current game.
     * @return the number of players from 2 to 4 (inclusive).
     */
    public int getPlayers() {
        // flag to check if input is valid
        boolean valid = true;
        int input = -1;
        do {
            if (!valid) {
                JOptionPane.showMessageDialog(this, "Invalid input, please try again.");
            }
            valid = true;
            try {
                input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the amount of players to play Scrabble (1-4)"));
                // number is valid but not in between 1 and 4
                if (input > 4 || input < 1) {
                    valid = false;
                }
            }
            // if what was inputted was not a number
            catch (Exception e) {
                valid = false;
            }
        } while (!valid);
        return input;
    }

    /**
     * Determine how many AI players should be created,
     * 
     * @param humanPlayers the number of human players in the game
     * @return
     */
    public int getAIPlayers(int humanPlayers) {
        if (humanPlayers == 4) return 0;
        // flag to check if input is valid
        boolean valid = true;
        int input = -1;
        do {
            if (!valid) {
                JOptionPane.showMessageDialog(this, "Invalid input, please try again.");
            }
            valid = true;
            try {
                input = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the amount of AI players to play Scrabble (" + Math.max(2-humanPlayers, 0) + "-" + (4-humanPlayers) + ")."));
                // number is valid but not in between 2 and 4
                if (input > 4 - humanPlayers || input < 2 - humanPlayers) {
                    valid = false;
                }
            }
            // if what was inputted was not a number
            catch (Exception e) {
                valid = false;
            }
        } while (!valid);
        return input;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // if user wants to play a move
        if (e.getSource() == playButton) {
            // get where the first tile is placed, and how many are placed
            Point p = getPlacedTile();
            int numTilesPlaced = getNumTilesPlaced();

            // if nothing was placed, return
            if (p.x < 0 || p.y < 0 || numTilesPlaced == 0) return;
            int numTilesSeen = 0;
            
            // goes horizontally and finds how many were placed to the right
            for (int i = p.x; i < 15 && boardButtons[p.y][i].getText().length() == 1; i++) numTilesSeen += boardButtons[p.y][i].isEnabled() ? 1: 0;

            boolean played = false;
            // if all placed tiles were found
            if (numTilesSeen == numTilesPlaced) {
                // goes horizontally and finds the first letter of the full word
                int firstLetter;
                for (firstLetter = p.x; firstLetter >= 0 && boardButtons[p.y][firstLetter].getText().length() == 1; firstLetter--);
                firstLetter++;

                // create location array
                int[] location = new int[3];
                location[Board.DIRECTION] = Board.HORIZONTAL;
                location[Board.COLUMN] = firstLetter;
                location[Board.ROW] = p.y;

                // build full word and tiles needed to create the word
                StringBuilder sbWord = new StringBuilder();
                StringBuilder sbLetters = new StringBuilder();
                for (int i = firstLetter; i < 15 && boardButtons[p.y][i].getText().length() == 1; i++) {
                    String text = boardButtons[p.y][i].getText();
                    if (boardButtons[p.y][i].isEnabled()) {
                        sbLetters.append(text);
                        sbWord.append(text);
                    }
                    else {
                        sbWord.append(text.toLowerCase());
                    }
                }

                // set played to if the move was sucessfully played or not
                played = sc.play(sbLetters.toString(), sbWord.toString(), location);
            }
            // reset for vertical
            numTilesSeen = 0;
            
            // goes vertically and finds how many were placed downward
            for (int i = p.y; i < 15 && boardButtons[i][p.x].getText().length() == 1; i++) numTilesSeen += boardButtons[i][p.x].isEnabled() ? 1: 0;

            // if all tiles were seen and a move hasn't been played yet
            if (numTilesSeen == numTilesPlaced && !played) {
                // find first letter of full word by going upwards until a blank space is found
                int firstLetter;
                for (firstLetter = p.y; firstLetter >= 0 && boardButtons[firstLetter][p.x].getText().length() == 1; firstLetter--);
                firstLetter++;
                
                // create location array
                int[] location = new int[3];
                location[Board.DIRECTION] = Board.VERTICAL;
                location[Board.COLUMN] = p.x;
                location[Board.ROW] = firstLetter;

                // build full word and tiles needed to create the word
                StringBuilder sbWord = new StringBuilder();
                StringBuilder sbLetters = new StringBuilder();
                for (int i = firstLetter; i < 15 && boardButtons[i][p.x].getText().length() == 1; i++) {
                    String text = boardButtons[i][p.x].getText();
                    if (boardButtons[i][p.x].isEnabled()) {
                        sbLetters.append(text);
                        sbWord.append(text);
                    }
                    else {
                        sbWord.append(text.toLowerCase());
                    }
                    
                }

                // set played to if the move was sucessfully played or not
                played = sc.play(sbLetters.toString(), sbWord.toString(), location);
            }
            // if a move wasnt played, show an error message
            if (!played){
                JOptionPane.showMessageDialog(this, "The word could not be played.");
            }
        }
        else if (e.getSource() == passButton) {
            // remove all placed tiles and then pass
            removeAllPlacedTiles();
            sc.pass();
        }
        else if (e.getSource() == swapButton) {
            removeAllPlacedTiles();
            // ask user for the tiles that should be swapped
            String input = JOptionPane.showInputDialog(this, "Please enter the tiles you want to swap");
            while (!sc.swap(input)) {
                JOptionPane.showMessageDialog(this, "Incorrect input, try again");
                input = JOptionPane.showInputDialog(this, "Please enter the tiles you want to swap");
            }
        }else if (e.getSource() == loadItem){
            while (true){
                try{
                    String filename = JOptionPane.showInputDialog("Enter the filename from which would like to load your game (or \"cancel\" to cancel)");
                    if (filename.equals("cancel")) return;
                    sc = ScrabbleController.load(filename);
                    sc.setView(this);
                    sc.updateView();
                    return;
                } catch(Exception ohno){
                    JOptionPane.showMessageDialog(this, "Load failed.");
                }
            }
        } else if (e.getSource() == saveItem){
            boolean saved = false;
            while (!saved){
                String filename = JOptionPane.showInputDialog("Enter the filename in which would like to save your game (or \"cancel\" to cancel)");
                saved = sc.save(filename);
                if (!saved) JOptionPane.showMessageDialog(this, "Save failed.");
            }
        }
    }

    /**
     * Finds all placed tiles on the board and places them back
     * in the player's rack.
     */
    public void removeAllPlacedTiles() {
        // iterate through every button
        for (JButton[] rowButtons: boardButtons) {
            for (JButton button: rowButtons) {
                // if a button is a user-placed button for that turn
                if (button.isEnabled() && !button.getText().equals(" ")) {
                    // find rack button that is blank and place it back
                    for (int k = 0; k < 7; k++){
                        if (rackButtons[k].getText().equals(" ")){
                            // add button back to rack
                            rackButtons[k].setText(button.getText());
                            rackButtons[k].setEnabled(true);
                            button.setText(" ");
                            button.setEnabled(false);
                            currentButton = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles a ScrabbleEvent called from ScrabbleController.
     * @param se the event that occurred
     */
    public void handleScrabbleStatusUpdate(ScrabbleEvent se) {
        // PLAYERS
        scoreLabels = new ArrayList<JLabel>();
        scoreLabels.add(new JLabel("Players:"));
        Player[] players = se.getPlayers();
        // for every player, add to the player list
        for (int i = 0; i < players.length; i++) {
            String label = "";
            label += (players[i] instanceof AIPlayer) ? "AI ": "";
            label += "Player " + (i + 1) + ": " + players[i].getScore();
            JLabel temp = new JLabel(label);
            if (i == se.getCurrentPlayer()) {
                temp.setOpaque(true);
                temp.setBackground(new Color(150, 255, 150));
            }
            scoreLabels.add(temp);
        }

        // RACK
        setRack(players[se.getCurrentPlayer()].getTiles());
        
        // TILE
        bagLabel = new JTextArea("Bag has " + se.getNumLetters() + " tiles remaining");
        bagLabel.setLineWrap(true);
        bagLabel.setWrapStyleWord(true);
        bagLabel.setEditable(false);
        bagLabel.setBackground(Color.PINK);

        // DISABLE PLACED TILES
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (se.getBoard()[i][j] instanceof Tile) {
                    boardButtons[i][j].setBackground(new Color(0xffffff));
                    boardButtons[i][j].setText(se.getBoard()[i][j].getLetter() + "");
                    boardButtons[i][j].setEnabled(false);
                }
            }
        }

        // update after status update
        updateDisplay();
    }

    /**
     * Ends the game by displaying the leaderboard for the game
     * and then exiting.
     * @param se the event passed from ScrabbleController
     */
    public void endGame(ScrabbleEvent se) {
        // get all players
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
        // for every player, find the player with the highest score, add them to the string
        // then remove from the list
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

        // show the message then close
        JOptionPane.showMessageDialog(this, finalString);
        System.exit(0);
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
        // return -1 if index is not found
        return -1;
    }

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        new View(board.getBoard());
    }
}