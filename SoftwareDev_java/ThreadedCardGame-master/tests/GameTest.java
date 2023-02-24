import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.File; // Import the File class

public class GameTest {
    public Game game = new Game();

    @Before
    public void setUp() {
        game.setNumberOfPlayers(4);
        game.setPackLocation("test_pack_0.txt");
        try {
            game.createDecks();
        } catch (Exception e) {
            System.out.println("Unable to create decks");
        }

        try {
            game.createPlayers();
        } catch (Exception e) {
            System.out.println("Unable to create players");
        }

        try {
            game.createPack();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to create testing pack: FileNotFoundException");
        }

        try {
            game.distributeCards();
        } catch (Exception e) {
            System.out.println("Unable to distribute cards");
        }

    }

    @After
    public void tearDown() {
        game = null;
        // Deleting any possible output files created during testing
        for (int i = 1; i < 5; i++) {
            File playerFile = new File("player" + i + "_output.txt");
            try {
                playerFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j < 5; j++) {
            File deckFile = new File("deck" + j + "_output.txt");
            try {
                deckFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void userInputsTest() {
        // testing invalid number of player inputs
        // test negative num of players, non-integers, pack is not 8 x number of players
        String data = """
                -3
                test_pack_0.txt
                text
                3
                test_pack_0.txt
                4
                test_pack_0.txt""";

        System.setIn(new ByteArrayInputStream(data.getBytes()));

        game.userInputs();
        assertEquals(4, (int) game.getNumberOfPlayers());
        assertEquals("test_pack_0.txt", game.getPackLocation());

        // testing if file does not exist/ invalid file types (same test packs as used
        // in 'validatePackTest()')
        data = """
                4
                does_not_exist
                4
                test_pack_1.txt
                4
                test_pack_2.txt
                4
                test_pack_3.txt
                4
                test_pack_4.txt
                4
                test_pack_0.txt""";

        System.setIn(new ByteArrayInputStream(data.getBytes()));

        game.userInputs();
        assertEquals(4, (int) game.getNumberOfPlayers());
        assertEquals("test_pack_0.txt", game.getPackLocation());
    }

    @Test
    public void createPackTest() {
        assertEquals("Wrong length of pack", 0, game.getPack().size());
    }

    @Test
    public void createDecksTest() {
        assertEquals("Incorrect amount of decks created", 4, game.getDeckList().size());
    }

    @Test
    public void createPlayersTest() {
        assertEquals("Incorrect amount of players created", 4, game.getPlayerList().size());
    }

    @Test
    public void validatePackTest() {
        assertTrue("Valid pack location and contents", game.validatePack("test_pack.txt"));
        assertFalse("Invalid pack location", game.validatePack("doesNotExist.txt"));
        assertFalse("Invalid pack contents: Multiple values per line", game.validatePack("test_pack_1.txt"));
        assertFalse("Invalid pack contents: Non integers", game.validatePack("test_pack_2.txt"));
        assertFalse("Invalid pack contents: Negative integers", game.validatePack("test_pack_3.txt"));
        assertFalse("Invalid pack contents: Wrong amount of integers", game.validatePack("test_pack_4.txt"));
    }

    @Test
    public void distributedCardsTest() {
        // getting player and deck objects created in setUp
        Player p1 = (Player) game.getPlayerList().get(0);
        CardDeck d1 = (CardDeck) game.getDeckList().get(0);

        // getting the size of players and decks hand
        int playerHandSize = p1.getPlayerHandList().size();
        int deckHandSize = d1.getDeckHand().size();

        // Getting the value of the 4th card in their hands
        Card playerHandCard = (Card) p1.getPlayerHandList().get(3);
        Card deckHandCard = d1.getDeckHand().get(3);
        int playerCardValue = playerHandCard.getCardValue();
        int deckCardValue = deckHandCard.getCardValue();

        assertEquals("Incorrect player hand size", 4, playerHandSize);
        assertEquals("Incorrect player cards in hand", 100, playerCardValue);
        assertEquals("Incorrect deck hand size", 4, deckHandSize);
        assertEquals("Incorrect cards in deck", 6, deckCardValue);
    }
}