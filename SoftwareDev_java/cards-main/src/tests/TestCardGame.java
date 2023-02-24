package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import main.*;

public class TestCardGame {

    private ArrayList<Card> pack = createPack();

    private ArrayList<Card> badPack = createBadPack();
        
    private ArrayList<CardDeck> decks;

    private ArrayList<PlayerThread> players;

    @Test
    public void testCreateDecks() {
        // Tests that the correct number of decks are created
        decks = CardGame.createDecks(4, false);
        assertEquals(4, decks.size());
    }

    @Test
    public void testCreatePlayers() {
        // Tests that the correct number of players are created
        decks = CardGame.createDecks(4, false);        
        players = CardGame.createPlayers(4, decks, false);
        assertEquals(4, players.size());
    }

    @Test
    public void testDealOutCards() {
        // Tests that dealing out cards fails if the pack is the wrong size        
        decks = CardGame.createDecks(4, false);
        players = CardGame.createPlayers(4, decks, false);

        try {
            CardGame.dealOutCards(players, decks, badPack);
            fail("Should have thrown an error");
        } catch (Exception e) {
            assertTrue(true);
        }

        // Tests that every player and deck starts with 0 cards
        decks = CardGame.createDecks(4, false);
        players = CardGame.createPlayers(4, decks, false);

        for (PlayerThread player : players) {
            assertEquals(0, player.showHand().size());
        }
        for (CardDeck deck : decks) {
            assertEquals(0, deck.showDeck().size());
        }
        
        // Tests that dealing out cards with a valid pack means
        // each player and deck has 4 cards
        CardGame.dealOutCards(players, decks, pack);

        assertEquals(decks.size(), players.size());
        for (PlayerThread player : players) {
            assertEquals(4, player.showHand().size());
        }
        for (CardDeck deck : decks) {
            assertEquals(4, deck.showDeck().size());
        }
    }

    @Test
    public void testStartPlaying() {
        // Tests 50 runs of the game to make sure they don't break
        for (int i = 0; i < 50; i++) {
            decks = CardGame.createDecks(4, false);
            players = CardGame.createPlayers(4, decks, false);
            CardGame.dealOutCards(players, decks, pack);
    
            CardGame.startPlaying(players);
            
        }        
    }

    private ArrayList<Card> createPack() {
        try {
            return new ArrayList<>(Arrays.asList(
                new Card(1), new Card(4), new Card(5),
                new Card(1), new Card(6), new Card(1),
                new Card(3), new Card(2), new Card(6),
                new Card(7), new Card(6), new Card(8),
                new Card(3), new Card(2), new Card(7),
                new Card(1), new Card(5), new Card(4),
                new Card(2), new Card(8), new Card(4),
                new Card(6), new Card(5), new Card(3),
                new Card(7), new Card(7), new Card(8),
                new Card(3), new Card(5), new Card(4),
                new Card(8), new Card(2)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    private ArrayList<Card> createBadPack() {
        try {
            return new ArrayList<>(Arrays.asList(new Card(1)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
