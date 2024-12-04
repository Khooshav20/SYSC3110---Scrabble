/**
 * BoardParser is an object with the ability to parse an XML file into an array representing a Scrabble Board.
 * 
 * @author Lucas Warburton (101276823)
 * @author Marc Fernandes (101288346)
 * @version 12/03/2024
 */

 import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;


public class BoardParser extends DefaultHandler {
    private Square[][] board;
    private StringBuilder elementContent;


    /**
     * When the parser begins the parse a file, it initializes the array in which it will store the board.
     */
    @Override
    public void startDocument(){
        board = new Square[15][15];
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                board[i][j] = null;
            }
        }
    }


    /**
     * Every time the parser encounters a new element.
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if(qName.equalsIgnoreCase("MiddleTile")){
            addSquare(new MiddleTile());
        } else if (qName.equalsIgnoreCase("PremiumTile")){
            addSquare(new PremiumTile());
        } else if (qName.equalsIgnoreCase("BlankSquare")){
            addSquare(new BlankSquare());
        }
        elementContent = new StringBuilder();
    }

    /**
     * Functionality to read the text within an element.
     */
    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    /**
     * Every time the parser gets to the end of an element.
     */
    @Override
    public void endElement(String uri, String localName, String qName){
        if(qName.equalsIgnoreCase("multiplier")){
            ((PremiumTile) getLast()).setMultiplier(Integer.parseInt(elementContent.toString()));
        }else if(qName.equalsIgnoreCase("isWord")){
            ((PremiumTile) getLast()).setIsWord((elementContent.toString().equalsIgnoreCase("true")));
        }
        elementContent = new StringBuilder();
    }

    /**
     * Parses an XML file into an array representing a board.
     * 
     * @param fileName The file containing the XML representing the board.
     * @return The array representing the board.
     * @throws IOException
     */
    public Square[][] readXMLBoardFile(String fileName) throws IOException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            File file = new File(fileName);
            parser.parse(file, this);
            return board;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    /**
     * Adds a square to the first empty square in the board.
     * 
     * @param s
     */
    private void addSquare(Square s){
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                if (board[i][j] == null){
                    board[i][j] = s;
                    return;
                }
            }
        }
    }

    /**
     * Gets the last square that was added to the board.
     * 
     * @return the last square.
     */
    private Square getLast(){
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                if (board[i][j] == null){
                    if (j == 0){
                        return board[i-1][14];
                    }
                    return board[i][j-1];
                }
            }
        }
        return board[14][14];
    }
}