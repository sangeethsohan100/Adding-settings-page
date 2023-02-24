import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class CardDeckTest {
    CardDeck cardDeck = new CardDeck(1);
    public CardGame gameTest = new CardGame();

    // Testing deck cards
    @Test
    public void getDeckTest() {
        ArrayList deckList = gameTest.getDecks();
        CardDeck deck3 = (CardDeck) deckList.get(2);
        String deck = deck3.getDeck();
        assertEquals("Invalid number of cards in deck", "", deck);
    }

    // Testing for deck contents written in output file
    @Test
    public void writeDeckTest() {
        CardDeck deck1 = (CardDeck) gameTest.getDecks().get(2);
        deck1.writeDeckCards();
        try {
            File deckTestFile = new File("Deck 1_output.txt");
            Scanner deckReader = new Scanner(deckTestFile);
            int i = 0;
            while (deckReader.hasNextLine()) {
                i += 1;
                String data = deckReader.nextLine();
                switch (i) {
                    case 1 -> assertEquals("Deck content output incorrect", "deck1 contents: 1 1 1 5 ", data);
                }
            }
            deckReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Executed before other test methods
    @Before
    public void settingTest() {
        gameTest.setNumberOfPlayers(4);
        gameTest.setPackLocation("test_pack.txt");

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
            System.out.println("Error when moving cards in round robin");
        }

    }

    // Executed after other test methods
    @After
    public void deleteFile() {
        // Deleting output files created during testing
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
}