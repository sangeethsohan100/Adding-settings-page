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
    /*Attributes:
    ---------------
    */
    //the deck's number
    private final Integer deckNumber;
    //the cards in a deck
    private ArrayList<Card> deckHand = new ArrayList<Card>();

    /*Constructor:
    ---------------
    */
    public CardDeck(Integer deckNumber) {
        this.deckNumber = deckNumber;
    }

    /*Getters and Setters:
    ---------------
    */
    //returns the deck's number
    public Integer getDeckNumber() {
        return deckNumber;
    }

    //returns the cards in a deck
    public ArrayList<Card> getDeckHand() {
        return deckHand;
    }

    /*Methods:
    ---------------
    */
    //adds a card instance to the deck's hand
    public void dealCard(Card card){
        deckHand.add(card);
    }

    //returns the card at the top (front) of the deck
    public Card drawCard(){
        return deckHand.remove(0);
    }

    //returns a string of the cards in the deck
    public String getDeck(){
        String deck = "";
        for (Card card : deckHand) {
            deck = deck + card.getCardValue() + " ";
        }
        return deck;
    }

    //writes the decks hand to an output file
    public void writeHand(){
        try {
            File myObj = new File("deck" + deckNumber + "_output.txt");
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred creating a file.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("deck" + deckNumber + "_output.txt");
            myWriter.write("deck" + deckNumber + " contents: " + getDeck() + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing deck to a file.");
            e.printStackTrace();
        }
    }
}