import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

public class NumberleModelTest {
    private NumberleModel model;

    @Before
    public void setUp() throws Exception {
        model = new NumberleModel();
    }

    @After
    public void tearDown() throws Exception {
        model = null;
    }

    @Test
    public void testProcessInput() {
        // Test with invalid input
        model.initialize();
        boolean result = model.processInput("6*1-2=5");
        assertFalse(result); // The result should be false
        assertEquals(6, model.getRemainingAttempts()); // The remaining attempts should be 6
        assertEquals("       ", model.getCurrentGuess().toString()); // The current guess should be empty

        // Test with valid input
        model.initialize();
        String targetEquation = model.getTargetEquation();
        result = model.processInput(targetEquation);
        assertTrue(result); // The result should be true
        assertEquals(5, model.getRemainingAttempts()); // The remaining attempts should be 5
        assertEquals(targetEquation, model.getCurrentGuess().toString()); // The current guess should be "2+3*2=8"
    }

    /**
     * Test the SetColor method
     */
    @Test
    public void testSetColor() {
        model.initialize();
        assertEquals(6, model.getRemainingAttempts());
        assertFalse(model.isGameWon());

        model.processInput("6*1-2=4");
        model.setColor();

        List<List<ColoredChar>> coloredChars = model.getColor();
        assertEquals(1, coloredChars.size());

        List<ColoredChar> lastAttempt = coloredChars.get(coloredChars.size() - 1);
        for (int i = 0; i < lastAttempt.size(); i++) {
            ColoredChar coloredChar = lastAttempt.get(i);
            char ch = coloredChar.getChar();
            Color color = coloredChar.getColor();

            if (model.getTargetEquation().indexOf(ch) == -1) { // If the character is not in the target equation
                // The character should be gray
                assertEquals(Color.GRAY, color);
            } else if (i < model.getTargetEquation().length() && model.getTargetEquation().charAt(i) == ch) { // If the character is in the target equation and in the correct position
                // The character should be green
                assertEquals(Color.GREEN, color);
            } else { // If the character is in the target equation but in the wrong position
                // The character should be orange
                assertEquals(Color.ORANGE, color);
            }
        }
    }

    /**
     * Test the startNewGame method
     */
    @Test
    public void testStartNewGame() {
        model.startNewGame();
        assertEquals(6, model.getRemainingAttempts());
        assertFalse(model.isGameWon());
        assertNotNull(model.getTargetEquation()); // The target equation should not be null
        assertTrue(model.getTargetEquation().length() > 0); // The target equation should have at least one character
        assertEquals("       ", model.getCurrentGuess().toString());
    }
}