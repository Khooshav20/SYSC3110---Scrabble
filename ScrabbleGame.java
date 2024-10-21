import java.util.*;

class ScrabbleGame{
    private LetterBag letterBag;
    private Player[] players;
    private Board board;

    public ScrabbleGame(){
        letterBag = new LetterBag();
        Scanner scanner = new Scanner(System.in);

        int numPlayers = -1;
        while (numPlayers > 4 || numPlayers < 2){
            System.out.println("How many players are playing? (2-4)");
            if (scanner.hasNextInt()) numPlayers = scanner.nextInt();
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
            System.out.println("The bag has " + letterBag.getSize() + " tiles remaining.");
            System.out.println("Current scores:");
            for (int i = 0; i < numPlayers; i++){
                System.out.println("Player " + (i + 1) + "'s score: " + players[i].getScore());
            }
            System.out.println("Player " + (currentPlayer + 1) + "'s turn:\n");
            System.out.println(board);
            System.out.println("\nPlayer " + (currentPlayer+1) + "'s tiles: " + players[currentPlayer].getTiles());

            int choice;
            if (letterBag.getSize() > 0){
                System.out.println("Would you like to play a word (1), pass (2), or exchange tiles (3)?");
                choice = scanner.nextInt();
                while (choice > 3 || choice < 1){
                    System.out.println("Invalid input. Would you like to play a word (1), exchange tiles (2), or pass (3)?");
                    choice = scanner.nextInt();
                }
            } else {
                System.out.println("Would you like to play a word (1), or pass (2)?");
                choice = scanner.nextInt();
                while (choice > 2 || choice < 1){
                    System.out.println("Invalid input. Would you like to play a word (1), or pass (2)?");
                    choice = scanner.nextInt();
                }
            }

            if (choice == 1){ //play word
                System.out.println("\nEnter your move in the form WORD xy, where WORD is the word being played and xy is the position of the first letter.");
                System.out.println("If the word is horizontal, xy should consist of the row number followed by the column letter.");
                System.out.println("If the word is vertical, xy should consist of the column letter followed by the row number.");
                System.out.println("Your move: ");
                String move = scanner.nextLine();
                while (true){
                    String[] s = move.split(" "); 
                    String letters = "";
                    for (int i = 0; i < s[0].length(); i++){
                        if (s[0].charAt(i) == '('){
                            for (; i < s[0].length(); i++){
                                if (s[0].charAt(i) == ')') break;
                            }
                        } else letters = letters + s[0].charAt(i);
                    }
                    if (!isRightFormat(move) || !players[currentPlayer].hasLetters(letters)){
                        System.out.println("Invalid format: ");
                        move = scanner.nextLine();
                    } else {
                        Tile[] moveTiles = players[currentPlayer].removeLetters(letters);
                        if (board.isValidPlacement(moveTiles, s[1])){
                            int score =  board.playWord(moveTiles, s[1]);
                            players[currentPlayer].addScore(score);
                            players[currentPlayer].addTiles(letterBag.getTiles(Math.min(letters.length(), letterBag.getSize())));
                            break;
                        } else {
                            players[currentPlayer].addTiles(moveTiles);
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
        }

        //end game
    }

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
            } else if (s[0].charAt(i) <= 'A' || s[0].charAt(i) >= 'Z') return false;
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

    private boolean onlyUppercaseLetters(String s){
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) < 'A' || s.charAt(i) > 'Z') return false;
        }
        return true;
    } 
}