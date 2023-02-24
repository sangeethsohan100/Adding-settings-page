package tests;
import static org.junit.Assert.*;

import org.junit.Test;

import main.Card;

public class TestCard {
    
    @Test
    @SuppressWarnings("unused")
    public void testCard() {
        // Test working when value >= 0
        try {
            Card card = new Card(0);
            assertEquals(0, card.getValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Test forcing an error where a negative value is given
        try {
            Card card = new Card(-4);
            fail("Should have thrown an error");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

}
