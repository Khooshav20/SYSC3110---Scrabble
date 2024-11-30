import java.io.Serializable;

public class PremiumTile extends BlankSquare implements Cloneable, Serializable {
    private int multiplier;
    private boolean isWord;

    public PremiumTile(){
        super();
    }

    public PremiumTile(int multiplier, boolean isWord){
        super();
        this.multiplier = multiplier;
        this.isWord = isWord;
    }

    public void setMultiplier(int multiplier){
        this.multiplier = multiplier;
    }

    public void setIsWord(boolean isWord){
        this.isWord = isWord;
    }

    public int getMultiplier(){
        return multiplier;
    }

    public boolean getIsWord(){
        return isWord;
    }

    public String toXML(){
        return "<PremiumTile>\n<multiplier>" + multiplier + "</multiplier>\n<isWord>" + isWord + "</isWord>\n</PremiumTile>";
    }
 
    @Override
    protected Object clone() 
        throws CloneNotSupportedException 
    { 
        return super.clone(); 
    } 
}
