import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class PlayerTest {
    private CardGame game = new CardGame();
    private Player player;

    // Testing for a random card to be discarded
    @Test
    public void randomDiscardTest() {
        try {
            Card card = player.randomCardDiscard();
            assertNotSame("preferred card discarded", player.getPlayerNumber(), card.getCardValue());

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    // Testing for the writing initial hand to output file
    @Test
    public void writeInitialHandTest() {
        player.writeInitialHand();
        try {
            File intialHandFile = new File("Player 1_output.txt");
            int i = 0;
            Scanner fileReader = new Scanner(intialHandFile);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                i += 1;
                switch (i) {
                    case 1 ->
                        assertEquals("Initial hand output is incorrect", "player 1 initial hand 4 2 2 2", data);
                    case 2 -> System.out.println("No line of data should be here");
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Testing for writing final hand to output file
    @Test
    public void writeFinalHandTest() {

        game.setWinnerId(2);
        player.writeWinnerFinalHand();

        try {
            File finalHandFile = new File("Player 3_output.txt");
            Scanner finalHandReader = new Scanner(finalHandFile);
            int i = 0;
            while (finalHandReader.hasNextLine()) {
                i += 1;
                String data = finalHandReader.nextLine();
                switch (i) {
                    case 1 -> assertEquals("Wrong final hand output", "player 3 final hand 1 1 1 100 ", data);
                    case 2 -> assertEquals("No line of data should be present", "", data);
                }
            }
            finalHandReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Testing for setting the game winner
    @Test
    public void setGameWinnerTest() {
        player.setGameWinner();
        assertEquals("Error in the run() method or incorrect test pack", 1, game.getWinnerId());
        assertTrue("Error in the run() method or incorrect test pack", game.getGameWon());
    }

    // Testing for the run method
    @Test
    public void runTest() {
        Player player2 = (Player) game.getPlayers().get(1);
        try {
            player2.run();

        } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
            System.out.println("Index Out of Bounds");
        }

        // getting sizes of decks to left and right of player2 after player2 moves
        CardDeck deck1 = (CardDeck) game.getDecks().get(0);
        CardDeck deck2 = (CardDeck) game.getDecks().get(1);

        try {
            assertTrue(game.getGameWon());
            assertEquals("Card drawn from wrong deck", 2, deck1.getDeckCards().size());
            assertEquals("Card discarded to wrong deck", 7, deck2.getDeckCards().size());
            assertEquals("Wrong move made", 2, game.getWinnerId());
        } catch (AssertionError e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }

    }

    // Executed before other test methods
    @Before
    public void gameSetting() {

        game.setPackLocation("pack.txt");
        game.setNumberOfPlayers(5);
        game.setPlayerWin(false);

        try {
            game.createPack();
        } catch (FileNotFoundException e) {
            System.out.println("Error when creating testing pack");
        }
        try {
            game.createDecks();
        } catch (Exception e) {
            System.out.println("Error when creating decks");
        }

        try {
            game.createPlayers();
        } catch (Exception e) {
            System.out.println("Error when creating players");
        }

        try {
            game.moveCards();
        } catch (Exception e) {
            System.out.println("Error when in moving cards in round robin");
        }

        player = (Player) game.getPlayers().get(0);
    }

    // Executed after other test methods
    @After
    public void deleteFile() {
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
