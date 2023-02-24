package tests;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.Card;
import main.CardDeck;
import main.PlayerThread;

public class TestPlayerThread {

    @Test
    public void testaddCard() {
        // Tests that adding a card puts it in the players hand
        PlayerThread player = new PlayerThread(1, null, null, null, false);
        assertEquals(0, player.showHand().size());
        try {
            Card c1 = new Card(1);
            player.addCard(c1);
        } catch (Exception e){}
        assertEquals(1, player.showHand().size());
    }

    @Test
    public void testGetInitString() {
        // Tests that the formatting of the initial outptut string is correct
        try {
            ArrayList<PlayerThread> players = new ArrayList<>();
            CardDeck left = new CardDeck(1, false);
            PlayerThread player = new PlayerThread(1, left, null, players, false);
            players.add(player);
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));

            assertEquals("player 1 initial hand: 1 1 1 1", player.getInitString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetMoveString() {
        // Tests that the formatting of the outptut string for each move is correct
        try {
            ArrayList<PlayerThread> players = new ArrayList<>();
            CardDeck left = new CardDeck(1, false);
            CardDeck right = new CardDeck(2, false);
            PlayerThread player = new PlayerThread(1, left, right, players, false);
            players.add(player);
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(1));
            player.addCard(new Card(0));

            assertEquals("\nplayer 1 draws a 0 from deck 1"
                + "\nplayer 1 discards a 1 to deck 2"
                + "\nplayer 1 current hand is 1 1 1 0",
                player.getMoveString(new Card(0), new Card(1)));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
    }

    @Test
    public void testGetEndingString() {
        // Tests that the formatting of the final outptut string is correct,
        // depending on whether the player has won or not
        try {
            ArrayList<PlayerThread> players = new ArrayList<>();
            CardDeck left = new CardDeck(1, false);
            PlayerThread player = new PlayerThread(1, left, null, players, false);
            players.add(player);
            player.addCard(new Card(1));
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));

            assertEquals("\nplayer 1 wins"
                + "\nplayer 1 exits"
                + "\nplayer 1 hand: 1 2 3 4",
                player.getEndingString(1));

            assertEquals("\nplayer 2 has informed player 1 that player 2 has won"
                + "\nplayer 1 exits"
                + "\nplayer 1 hand: 1 2 3 4",
                player.getEndingString(2));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDrawCard() {
        // Tests that drawing 4 cards works where 4 cards were added,
        // and that drawing follows a FIFO structure
        ArrayList<PlayerThread> players = new ArrayList<>();
        CardDeck left = new CardDeck(1, false);
        PlayerThread player = new PlayerThread(1, left, null, players, false);
        players.add(player);
        try {
            left.addCard(new Card(1));
            left.addCard(new Card(2));
            left.addCard(new Card(3));
            left.addCard(new Card(4));
        } catch (Exception e) {}
        assertEquals(1, player.drawCard().getValue());
        assertEquals(2, player.drawCard().getValue());
        assertEquals(3, player.drawCard().getValue());
        assertEquals(4, player.drawCard().getValue());

        // Tests that drawing a 5th card fails where 4 were added
        try {
            player.drawCard();
            fail("Should have thrown an error");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDiscard() {
        // Tests that the right deck recieves the cards that the player discards,
        // and that discarding follows the FIFO structure
        ArrayList<PlayerThread> players = new ArrayList<>();
        CardDeck right = new CardDeck(1, false);
        PlayerThread player = new PlayerThread(1, null, right, players, false);
        players.add(player);
        try {
            Card c1 = new Card(1);
            player.addCard(c1);
            Card c2 = new Card(2);
            player.addCard(c2);
            Card c3 = new Card(3);
            player.addCard(c3);
            Card c4 = new Card(4);
            player.addCard(c4);
            
            player.discard(c1);
            player.discard(c2);
            player.discard(c3);
            player.discard(c4);
        } catch (Exception e) {}
        assertEquals(1, right.drawCard().getValue());
        assertEquals(2, right.drawCard().getValue());
        assertEquals(3, right.drawCard().getValue());
        assertEquals(4, right.drawCard().getValue());
    }

    @Test
    public void testCheckWin() {
        // Testing that if a player has 4 cards of an equal value, they win,
        // and if they don't, they don't win
        ArrayList<PlayerThread> players = new ArrayList<>();
        PlayerThread player1 = new PlayerThread(1, null, null, players, false);
        PlayerThread player2 = new PlayerThread(2, null, null, players, false);
        players.add(player1);
        players.add(player2);
        try {
            player1.addCard(new Card(1));
            player1.addCard(new Card(2));
            player1.addCard(new Card(3));
            player1.addCard(new Card(4));

            player2.addCard(new Card(1));
            player2.addCard(new Card(1));
            player2.addCard(new Card(1));
            player2.addCard(new Card(1));
        } catch (Exception e) {}
        assertFalse(player1.checkWin());
        assertTrue(player2.checkWin());
    }

    @Test
    public void testChooseDiscard() {
        // Tests that a player never chooses to discard a card with a value that
        // matches their ID
        ArrayList<PlayerThread> players = new ArrayList<>();
        PlayerThread player = new PlayerThread(1, null, null, players, false);
        players.add(player);
        try {
            player.addCard(new Card(1));
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
        } catch (Exception e) {}
        for (int i=0; i<20; i++) {
            assertNotEquals(1, player.chooseDiscard().getValue());
        }

        player = new PlayerThread(2, null, null, players, false);
        players.add(player);
        try {
            player.addCard(new Card(1));
            player.addCard(new Card(2));
            player.addCard(new Card(3));
            player.addCard(new Card(4));
        } catch (Exception e) {}
        for (int i=0; i<20; i++) {
            assertNotEquals(2, player.chooseDiscard().getValue());
        }
    }
}