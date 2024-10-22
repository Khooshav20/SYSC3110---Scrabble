/**
 * The ScrabbleGame class creates and manages a game of Scrabble.
 * 
 * @author Lucas Warburton
 * @version 22/10/2024
 */

import java.io.FileNotFoundException;
import java.util.*;

class ScrabbleGame{
    private LetterBag letterBag;
    private Player[] players;
    private Board board;

    /**
     * Creates an instance of ScrabbleGame, causing the game of scrabble to start.
     */
    public ScrabbleGame() throws FileNotFoundException {
        letterBag = new LetterBag();
        Scanner scanner = new Scanner(System.in);

        int numPlayers = -1;
        while (numPlayers > 4 || numPlayers < 2){
            System.out.println("How many players are playing? (2-4)");
            String line = scanner.nextLine();

            if (line.matches("[2-4]")) {
                numPlayers = Integer.parseInt(line);
            }
        } 
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++){
            players[i] = new Player();
            players[i].addTiles(letterBag.getTiles(7));
        }
        
        board = new Board();

        int turnsWithoutScore = 0;
        int currentPlayer = 0;

        while (true){
            System.out.println("The bag has " + letterBag.getSize() + " tiles remaining.\n");
            System.out.println("Current scores:");
            for (int i = 0; i < numPlayers; i++){
                System.out.println("Player " + (i + 1) + "'s score: " + players[i].getScore());
            }
            System.out.println("\nPlayer " + (currentPlayer + 1) + "'s turn:\n");
            System.out.println(board);
            System.out.println("Player " + (currentPlayer+1) + "'s tiles: " + players[currentPlayer].getTiles());

            int choice = 0;
            if (letterBag.getSize() > 0){
                while (choice == 0) {
                    System.out.println("\nWould you like to play a word (1), pass (2), or exchange tiles (3)?");
                    String line = scanner.nextLine();
        
                    if (line.matches("[1-3]")) {
                        choice = Integer.parseInt(line);
                    } else {
                        System.out.print("Invalid input. ");
                    }
                }
            } else {
                while (choice == 0) {
                    System.out.println("Would you like to play a word (1), or pass (2)?");
                    String line = scanner.nextLine();
        
                    if (line.matches("[1-2]")) {
                        choice = Integer.parseInt(line);
                    } else {
                        System.out.print("Invalid input. ");
                    }
                }
            }

            if (choice == 1){ //play word
                System.out.println("\nEnter your move in the form WORD xy, where WORD is the word being played and xy is the position of the first letter.");
                System.out.println("If the word is horizontal, xy should consist of the row number followed by the column letter.");
                System.out.println("If the word is vertical, xy should consist of the column letter followed by the row number.");
                System.out.println("Your move: ");
                String move = scanner.nextLine();
                while (true){
                    while (!isRightFormat(move)) {
                        System.out.println("Invalid format: ");
                        move = scanner.nextLine();
                    }

                    String[] s = move.split(" "); 
                    String word = s[0];
                    String location = s[1];
                    String letters = "";
                    for (int i = 0; i < word.length(); i++){
                        if (word.charAt(i) == '('){
                            for (; i < word.length(); i++){
                                if (word.charAt(i) == ')') break;
                            }
                        } else if (Character.isUpperCase(word.charAt(i)))
                            letters = letters + word.charAt(i);
                    }

                    if (!players[currentPlayer].hasLetters(letters)){
                        System.out.println("You don't have those letters: ");
                        move = scanner.nextLine();
                    } else {
                        Tile[] moveTiles = players[currentPlayer].removeLetters(letters);

                        //not sure how this will work exactly
                        if (board.isValidMove(moveTiles, word, location)){
                            int score =  board.playMove(moveTiles, word, location);
                            players[currentPlayer].addScore(score);
                            players[currentPlayer].addTiles(letterBag.getTiles(Math.min(letters.length(), letterBag.getSize())));
                            System.out.println("Player " + (currentPlayer + 1) + " played " + word.toUpperCase() + " for " + score + " points.");
                            break;
                        } else {
                            players[currentPlayer].addTiles(moveTiles);
                            System.out.println("Invalid move: ");
                            move = scanner.nextLine();
                        }
                    }
                }
                turnsWithoutScore = 0;
            } else if  (choice == 2){ //pass
                turnsWithoutScore++;
            } else if (choice == 3){ //exchange tiles
                System.out.println("\nEnter the tiles you would like to exchange, in the following format: ABCDEFG");
                String exchangeString = scanner.nextLine();
                while (!onlyUppercaseLetters(exchangeString) || !(players[currentPlayer].hasLetters(exchangeString)) || exchangeString.length() > letterBag.getSize()){
                    System.out.println("Invalid input, try again.");
                    exchangeString = scanner.nextLine();
                }
                Tile[] exchangeTiles = players[currentPlayer].removeLetters(exchangeString);
                exchangeTiles = letterBag.swapTiles(exchangeTiles);
                players[currentPlayer].addTiles(exchangeTiles);
                turnsWithoutScore++;
            }
            if (players[currentPlayer].getNumTiles() == 0) break;
            if (turnsWithoutScore >= 6){
                currentPlayer = -1;
                break;
            }
            currentPlayer = (currentPlayer + 1) % numPlayers;
        }

        scanner.close();

        //handle score penalties
        int totalPenalty = 0;
        for (int i = 0; i < numPlayers; i++){
            if (i != currentPlayer){
                int penalty = players[i].getPenalty();
                totalPenalty += penalty;
                players[i].addScore(-penalty);
            }
        }
        if (currentPlayer >= 0) players[currentPlayer].addScore(totalPenalty);

        //obtain scores
        Integer[] scores = new Integer[numPlayers];
        for (int i = 0; i < numPlayers; i++){
            scores[i] = (players[i].getScore());
        }

        //output leaderboard
        System.out.println("Final Leaderboard:");
        int previousPlacement = 1;
        int previousScore = Collections.min(Arrays.asList(scores));
        for (int i = 1; i <= numPlayers; i++){
            int score = Collections.max(Arrays.asList(scores));
            int index = findIndex(scores, score);
            if (previousScore == score){
                System.out.println(previousPlacement + ": Player " + (index + 1) + " with a score of " + players[index].getScore());
            } else {
                System.out.println(i + ": Player " + (index + 1) + " with a score of " + players[index].getScore());
                previousPlacement = i + 1;
                previousScore = score;
            }
            players[index].addScore(-10000);
            scores[index] -= 10000;
        }
    }


    /**
     * Checks if a string representing a scrabble move is in the proper notation.
     * 
     * @param move The string to be verified
     * @return whether the string is the proper notation
     */
    private boolean isRightFormat(String move){
        String[] s = move.split(" ");

        //check if the move has the 2 required parameters 
        if (s.length != 2) return false;

        //check if the first parameter is a valid word input
        for (int i = 0; i < s[0].length(); i++){
            if (s[0].charAt(i) == '('){ //enter paranthesis
                for (; i < s[0].length(); i++){ 
                    if (s[0].charAt(i) > 'Z' || s[0].charAt(i) < 'A'){ //if not letter
                        if (s[0].charAt(i) == ')') break;
                        else return false;
                    }
                }
            } else if ((s[0].charAt(i) < 'A' || s[0].charAt(i) > 'Z') && (s[0].charAt(i) < 'a' || s[0].charAt(i) > 'z')) return false;
        }

        //check if the second parameter includes a valid row and column
        if (s[1].length() == 2){
            if ((s[1].charAt(0) <= '9') && (s[1].charAt(0) >= '1')){
                if ((s[1].charAt(1) < 'A') || (s[1].charAt(1) > 'O')) return false;
            } 
            else if ((s[1].charAt(0) >= 'A') && (s[1].charAt(0) <= 'O')){
                if (!((s[1].charAt(1) <= '9') && (s[1].charAt(1) >= '1'))) return false;
            }
        } else if (s[1].length() == 3){
            if ((s[1].charAt(0) <= '9') && (s[1].charAt(0) > '0')){
                if (!(((s[1].charAt(0) <= '5') && (s[1].charAt(0) >= '0')) && ((s[1].charAt(2) >= 'A') && (s[1].charAt(2) <= 'O')))) return false;
            } 
            else if ((s[1].charAt(0) >= 'A') && (s[1].charAt(0) <= 'O')){
                if (!(((s[1].charAt(1) >= '1') && (s[1].charAt(1) <= '9')) && ((s[1].charAt(2) >= '0') && (s[1].charAt(2) <= '5')))) return false;
            }
        } else return false;

        return true;
    }
    

    /**
     * Checks if a string is made up of only uppercase letters.
     * 
     * @param s The string to be checked
     * @return Whether the string is only uppercase
     */
    private boolean onlyUppercaseLetters(String s){
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) < 'A' || s.charAt(i) > 'Z') return false;
        }
        return true;
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

    public static void main(String args[]) {
        try {
            new ScrabbleGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}