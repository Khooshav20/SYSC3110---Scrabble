import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the blankSquare class.
 *
 * @author Alexander Gardiner, 101261196
 * @version 08/11/2024
 */

public class BlankSquareTest {

    @Test
    public void testGetLetter() {
        BlankSquare blankSquare = new BlankSquare();

        // Check that getLetter returns a space character.
        assertEquals("The letter for BlankSquare should be a space", ' ', blankSquare.getLetter());
    }
}