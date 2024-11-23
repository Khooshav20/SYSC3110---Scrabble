public class PremiumTile extends BlankSquare{
    private int multiplier;
    private boolean isWord;

    public PremiumTile(int multiplier, boolean isWord){
        super();
        this.multiplier = multiplier;
        this.isWord = isWord;
    }

    public int getMultiplier(){
        return multiplier;
    }

    public boolean getIsWord(){
        return isWord;
    }
}
