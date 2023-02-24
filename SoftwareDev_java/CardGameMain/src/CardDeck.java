import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    // returns deck number
    public int getDeckNumber() {
        return deckNumber;
    }

    // returns cards in a deck
    public ArrayList<Card> getDeckCards() {
        return deck;
    }

    // adds a card to the deck
    public void addCardtoDeck(Card card) {
        deck.add(card);
    }

    // returns the card at the top of the deck
    public Card getTopCard() {
        return deck.remove(0);
    }

    // returns a string cards in the deck
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
            BufferedWriter deckWriter = new BufferedWriter(
                    new FileWriter("Deck " + deckNumber + "_output.txt"));
            deckWriter.write("deck " + deckNumber + " contents: " + getDeck() + "\n");
            deckWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing deck to a file.");
            e.printStackTrace();
        }
    }
}