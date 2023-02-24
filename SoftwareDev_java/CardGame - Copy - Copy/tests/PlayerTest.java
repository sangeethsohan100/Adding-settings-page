import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PlayerTest {
    private Game game;
    private Player p1;

    @Before
    public void setUp() {
        game = new Game();

        game.setNumberOfPlayers(4);
        game.setPlayerWin(false);
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
            game.moveCards();
        } catch (Exception e) {
            System.out.println("Unable to distribute cards");
        }

        p1 = (Player) game.getPlayers().get(0);
    }

    @After
    public void tearDown() {
        game = null;
        p1 = null;
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
    public void selectCardToDiscardTest() {
        Card card = p1.selectCardToDiscard();

        assertNotSame("Discards players preferred card", p1.getPlayerNumber(), card.getValue());
        assertEquals("Incorrect test pack format used", 100, (int) card.getValue());
    }

    @Test
    public void getPlayerHandTest() {
        String hand = p1.getPlayerHand();
        assertEquals("Incorrect player hand or test pack format", "1 1 1 100 ", hand);
    }

    @Test
    public void writeInitialHandTest() {
        p1.writeInitialHand();
        try {
            File myObj = new File("player1_output.txt");
            Scanner myReader = new Scanner(myObj);
            int count = 0;
            while (myReader.hasNextLine()) {
                count += 1;
                String data = myReader.nextLine();
                switch (count) {
                    case 1 -> assertEquals("Wrong initial hand output", "player 1 initial hand 1 1 1 100 ", data);
                    case 2 -> System.out.println("Should not have data here");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Test
    public void writeFinalHandTest() {
        // setting victor number so player 1 gets correct final hand output
        game.setWinnerId(2);
        p1.writeFinalHand();
        // checking that players output is correct
        try {
            File myObj = new File("player1_output.txt");
            Scanner myReader = new Scanner(myObj);
            int count = 0;
            while (myReader.hasNextLine()) {
                count += 1;
                String data = myReader.nextLine();
                switch (count) {
                    case 1 -> assertEquals("No data should be present", "", data);
                    case 2 ->
                        assertEquals("Wrong win output", "player 2 has informed player 1 that player 2 has won", data);
                    case 3 -> assertEquals("Wrong exit output", "player 1 exits", data);
                    case 4 -> assertEquals("Wrong final hand output", "player 1 final hand 1 1 1 100 ", data);
                    case 6 -> System.out.println("Should not have data here");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Test
    public void setVictoryAttributesTest() {
        p1.setGameWinner();
        assertEquals("Wrong test pack used or error in run()", 1, (int) game.getWinnerId());
        assertTrue("Wrong test pack used or error in run()", game.getGameWon());
    }

    @Test
    public void runTest() {
        Player p3 = (Player) game.getPlayers().get(2);
        p3.run();

        // getting sizes of decks to left and right of player3 after player3 moves
        CardDeck d2 = (CardDeck) game.getDecks().get(1);
        CardDeck d3 = (CardDeck) game.getDecks().get(2);

        int deck2Size = d2.getDeckCards().size();
        int deck3Size = d3.getDeckCards().size();

        assertTrue(game.getGameWon());
        assertEquals("Wrong move made", 3, (int) game.getWinnerId());

        assertEquals("Card discarded to wrong deck", 5, deck3Size);
        assertEquals("Card drawn from wrong deck", 3, deck2Size);
    }
}
