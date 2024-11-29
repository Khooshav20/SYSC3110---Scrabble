import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BoardParser extends DefaultHandler {
    private Square[][] board;
    private StringBuilder elementContent;


    @Override
    public void startDocument(){

        board = new Square[15][15];
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++){
                board[i][j] = null;
            }
        }
    }

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

    @Override
    public void characters(char[] ch, int start, int length){
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        if(qName.equalsIgnoreCase("multiplier")){
            ((PremiumTile) getLast()).setMultiplier(Integer.parseInt(elementContent.toString()));
        }else if(qName.equalsIgnoreCase("isWord")){
            ((PremiumTile) getLast()).setIsWord((elementContent.toString().equalsIgnoreCase("true")));
        }
        elementContent = new StringBuilder();
    }

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