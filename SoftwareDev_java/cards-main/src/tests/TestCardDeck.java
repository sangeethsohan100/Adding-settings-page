package tests;
import static org.junit.Assert.*;
import org.junit.Test;

import main.Card;
import main.CardDeck;

public class TestCardDeck {
    
    @Test
    public void testAddCard() {
        // Testing that adding cards to a deck works in a FIFO structure
        CardDeck deck = new CardDeck(1, false);
        Card card;
        try {
            card = new Card(3);
            deck.addCard(card);

            card = new Card(4);
            deck.addCard(card);

            card = new Card(5);
            deck.addCard(card);

            card = new Card(6);
            deck.addCard(card);

            card = new Card(3);
            deck.addCard(card);
        } catch (Exception e) {}
        assertEquals(3, deck.drawCard().getValue());
        assertEquals(4, deck.drawCard().getValue());
        assertEquals(5, deck.drawCard().getValue());
        assertEquals(6, deck.drawCard().getValue());
        assertEquals(3, deck.drawCard().getValue());
    }

    @Test
    public void testDrawCard() {
        // Tests that drawing 4 cards works where 4 cards were added
        CardDeck deck = new CardDeck(1, false);
        Card card;
        for (int i = 0; i < 4; i++) {
            try {
                card = new Card(i);
                deck.addCard(card);
            } catch (Exception e) {}
        }
        boolean works = true;
        try {
            for (int i = 0; i < 4; i++) {
                deck.drawCard();
            }
        } catch (Exception e) {
            works = false;
        }
        assertTrue(works);
        // Tests that drawing a 5th card fails where 4 were added
        try {
            deck.drawCard();
        } catch (Exception e) {
            works = false;
        }
        assertFalse(works);
    }

    @Test
    public void testGetOutputString() {
        // Testing that the output string is correctly formatted
        CardDeck deck = new CardDeck(1, false);
        Card card;
        try {
            card = new Card(3);
            deck.addCard(card);

            card = new Card(4);
            deck.addCard(card);

            card = new Card(5);
            deck.addCard(card);

            card = new Card(6);
            deck.addCard(card);
        } catch (Exception e) {}
        assertEquals("deck1 contents: 3 4 5 6", deck.getOutputString());
    }
}
