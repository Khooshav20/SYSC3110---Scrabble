import java.io.Serializable;

public class BlankTile extends Tile implements Serializable{
    
    public BlankTile() {
        super(' ', 0);
    }

    public void setLetter(char c){
        super.letter = c;
    }
}
