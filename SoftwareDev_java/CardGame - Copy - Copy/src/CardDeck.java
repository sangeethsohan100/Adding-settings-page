import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Christian Wood and Jacob Beeson
 *
 */

public class CardDeck {

    private int deckNumber;

    private ArrayList<Card> deck = new ArrayList<Card>();

    /*
     * Constructor:
     * ---------------
     */
    public CardDeck(int deckNumber) {
        this.deckNumber = deckNumber;
    }

    /*
     * Getters and Setters:
     * ---------------
     */
    // returns the deck's number
    public int getDeckNumber() {
        return deckNumber;
    }

    // returns the cards in a deck
    public ArrayList<Card> getDeckCards() {
        return deck;
    }

    /*
     * Methods:
     * ---------------
     */
    // adds a card instance to the deck's hand
    public void addCardtoDeck(Card card) {
        deck.add(card);
    }

    // returns the card at the top (front) of the deck
    public Card getTopCard() {
        return deck.remove(0);
    }

    // returns a string of the cards in the deck
    public String getDeck() {
        String deckString = "";
        for (Card card : deck) {
            deckString = deckString + card.getCardValue() + " ";
        }
        return deckString;
    }

    // writes the decks hand to an output file
    public void writeDeckCards() {
        try {
            File deckFile = new File("Deck " + deckNumber + "_output.txt");
            deckFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred creating a file.");
            e.printStackTrace();
        }

        try {
            FileWriter deckWriter = new FileWriter("Deck " + deckNumber + "_output.txt");
            deckWriter.write("deck " + deckNumber + " contents: " + getDeck() + "\n");
            deckWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing deck to a file.");
            e.printStackTrace();
        }
    }
}