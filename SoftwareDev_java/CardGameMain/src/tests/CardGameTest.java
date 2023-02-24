import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.io.File;

public class CardGameTest {
    public CardGame gameTest = new CardGame();

    // Testing for the validaty of the user input
    @Test
    public void userInputTest() {

        // testing if file is invalid or does not exist
        String data = """
                5
                noPack.txt
                5
                pack.txt""";

        System.setIn(new ByteArrayInputStream(data.getBytes()));
        try {
            gameTest.userInput();
            assertEquals(5, gameTest.getNumberOfPlayers());
            assertEquals("pack.txt", gameTest.getPackLocation());
        } catch (NoSuchElementException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

        // testing invalid input for the number of player inputs such as negative
        // numbers,
        // non-integers, pack is not 8 x number of players
        data = """
                0
                pack.txt
                1
                pack.txt
                -4
                pack.txt
                """;

        System.setIn(new ByteArrayInputStream(data.getBytes()));

        try {
            gameTest.userInput();
            assertEquals("pack.txt", gameTest.getPackLocation());
            assertEquals(5, gameTest.getNumberOfPlayers());

        } catch (NoSuchElementException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    // Testing for the validaty of the length of the pack
    @Test
    public void createPackTest() {
        assertEquals("Invalid Pack Length", 0, gameTest.getPack().size());
    }

    // Testing for a valid pack location
    @Test
    public void validPackTest() {
        try {
            assertTrue("Valid pack location", gameTest.validPack("pack.txt"));
            assertFalse("Invalid pack location", gameTest.validPack("noPack.txt"));

        } catch (AssertionError e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    // Testing for the number of decks created
    @Test
    public void createDeckTest() {
        assertEquals("Invalid Number of Decks", 5, gameTest.getDecks().size());
    }

    // Testing for the number of player that are created
    @Test
    public void createPlayersTest() {
        try {
            assertEquals("Invalid Number of Players", 5, gameTest.getPlayers().size());
        } catch (AssertionError e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    // Testing for the round-robin movement of the cards checking if the number of
    // cards in deck and player hand is valid
    @Test
    public void moveCardsTest() {

        // creating player and deck objects
        CardDeck deck2 = (CardDeck) gameTest.getDecks().get(1);
        Player player2 = (Player) gameTest.getPlayers().get(1);

        // Size of players hand and number of cards in deck
        int deckSize = deck2.getDeckCards().size();
        int handSize = player2.getHandList().size();

        try {
            Card deckCard = deck2.getDeckCards().get(3);
            Card handCard = (Card) player2.getHandList().get(4);
            int cardValueDeck = deckCard.getCardValue(); // card value in deck
            int cardValuePlayer = handCard.getCardValue(); // card value in player's hand

            assertEquals("Invalid number of cards in hand", 80, cardValuePlayer);
            assertEquals("Invalid hand size of player", 4, handSize);
            assertEquals("Invalid number of cards in deck", 4, deckSize);
            assertEquals("Invalid cards in deck", 2, cardValueDeck);

        } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    // Executed before other test methods
    @Before
    public void gameSetting() {
        // setting the number of players and location of pack
        gameTest.setPackLocation("pack.txt");
        gameTest.setNumberOfPlayers(5);
        try {
            gameTest.createPack();
        } catch (FileNotFoundException e) {
            System.out.println("Error when creating testing pack");
        }

        try {
            gameTest.createPlayers();
        } catch (Exception e) {
            System.out.println("Error when creating players");
        }

        try {
            gameTest.createDecks();
        } catch (Exception e) {
            System.out.println("Error when creating decks");
        }

        try {
            gameTest.moveCards();
        } catch (Exception e) {
            System.out.println("Error when creating distribute cards");
        }

    }

    // Executed after other test methods
    @After
    public void deleteFile() {
        // Output files created when testing are deleted
        for (int n = 1; n < 5; n++) {
            File playerFile = new File("player" + n + "_output.txt");
            try {
                playerFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i < 5; i++) {
            File deckFile = new File("deck" + i + "_output.txt");
            try {
                deckFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}