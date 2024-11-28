import java.io.Serializable;

/**
 * Square indicating that the space is blank.
 * @author Marc Fernandes (101288346)
 * @version 22/10/2024
 */

public class BlankSquare implements Square, Serializable{
    
    /*
     * Empty constructor method
     */
    public BlankSquare() {
    }

    public char getLetter() {
        return ' ';
    }
}
