package main;

import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;

public class CardDeck {

    private volatile Queue<Card> cards; // Cards in deck represented as a queue due to the FIFO nature of drawing and discarding

    private int id;                     // The ID of the deck

    private boolean record;             // Whether the deck should record its final state to the output file or not

    public CardDeck(int id, boolean record) {
        cards = new LinkedList<>();
        this.id = id;
        this.record = record;
    }

    public synchronized void addCard(Card card) {
        // Add card when dealing at start of game and when a player discards to this deck
        cards.add(card);
    }

    public synchronized Card drawCard() {
        // For when a player draws a card from this deck
        return cards.remove();
    }

    public int getId() {
        // Returns the ID of the deck
        return id;
    }

    public String getOutputString() {
        String string = "deck" + id + " contents:";
            for (Card card : cards) {
                string = string.concat(" "+card.getValue());
            }
        return string;
    }

    public void writeResult() {
        if (record) {
            try {
                // Creates a FileWriter to record the result to deck[id]_output.txt
                FileWriter outputWriter = new FileWriter("deck"+id+"_output.txt");
                String outputString = getOutputString();
                // Writes the cards in the deck at the end of the game to the file
                outputWriter.write(outputString);

                // Closes the FileWriter
                outputWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Queue<Card> showDeck() {
        // Returns the list of cards in the deck (represented as a queue) for testing purposes
        return cards;
    }
}