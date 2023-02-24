package main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class PlayerThread implements Runnable {

    private ArrayList<Card> hand;            // The player's hand as a list of cards

    private ArrayList<PlayerThread> players; // The list of all players in the game

    private int id;                          // The player's ID

    private CardDeck leftDeck, rightDeck;    // The decks to the player's left and right

    private volatile boolean ended = false;  // Whether the player should stop playing

    private FileWriter outputWriter;         // Writes to the file player[id]_output.txt

    private boolean record;                  // Whether the player should record its moves and initial/final hand to the output file or not 

    public PlayerThread(int id, CardDeck left, CardDeck right, ArrayList<PlayerThread> players, boolean record) {
        // Constructor
        this.players = players;
        leftDeck = left;
        rightDeck = right;
        this.id = id;
        this.record = record;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        // Add card when dealing at start of game
        hand.add(card);
    }

    public ArrayList<Card> showHand() {
        // Shows hand of player, used for testing
        return hand;
    }

    @Override
    public void run() {
        // Initial state is recorded to player[id]_output.txt
        recordInit();

        if (checkWin()) {
            for (PlayerThread player : players) {
                // If the player has already won when the game begins, tell each player to stop playing and that this player has won
                player.endGame(id);
            }
        }

        // Loop runs every 50ms until a player wins
        while (!ended) {

            // Player draws and discards
            Card drawnCard = drawCard();
            Card toDiscard = chooseDiscard();
            discard(toDiscard);

            // Move is recorded to player[id]_output.txt
            recordMove(drawnCard, toDiscard);

            if (!ended && checkWin()) {
                for (PlayerThread player : players) {
                    // If the player wins on this go, tell each player to stop playing and that this player has won
                    player.endGame(id);
                }
            }
            try {
                // Acts as a clock to synchronise moves between player threads
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void endGame(int winnerId) {
        // Stops game loop from running once this player has been told to stop playing
        ended = true;

        // Records players final state in player[id]_output.txt
        recordEnding(winnerId);
    }

    public String getInitString() {
        // Builds a string of what to output at the beginning of the game
        String string = "player "+id+" initial hand:";
        for (Card card : hand) {
            string = string.concat(" "+card.getValue());
        }
        return string;
    }

    public String getMoveString(Card drawnCard, Card toDiscard) {
        // Builds a string of what to output after each turn the player takes
        String string = "\nplayer "+id+" draws a "+drawnCard.getValue()+" from deck "+leftDeck.getId();
        string = string.concat("\nplayer "+id+" discards a "+toDiscard.getValue()+" to deck "+rightDeck.getId());
        string = string.concat("\nplayer "+id+" current hand is");
        for (Card card : hand) {
            string = string.concat(" "+card.getValue());
        }
        return string;
    }

    public String getEndingString(int winnerId) {
        // Builds a string of what to output after the game ends
        String string;
        if (winnerId == id) {
            string = "\nplayer " + id + " wins";
        } else {
            string = "\nplayer " + winnerId + " has informed player " + id + " that player " + winnerId + " has won";
        }
        string = string.concat("\nplayer " + id + " exits\nplayer " + id + " hand:");
        for (Card card : hand) {
            string = string.concat(" "+card.getValue());
        }
        return string;
    }

    public Card drawCard() {
        // Draws card from left deck
        Card card = leftDeck.drawCard();
        // Adds card to the player's hand
        hand.add(card);
        return card;
    }

    public void discard(Card card) {
        // Discards card to right deck
        rightDeck.addCard(card);
        // Removes card from the player's hand
        hand.remove(card);
    }

    public boolean checkWin() {
        int noOfSameCards = 0;
        // Finds the value of the first card in the player's hand
        int valOfCard = hand.get(0).getValue();
        for (int i=1; i<4; i++) {
            // Counts number of cards in the player's hand that have the same value as the first card
            if (hand.get(i).getValue() == valOfCard) {
                noOfSameCards++;
            } else {
                // If a card is found that does not match the first value, there is no point going through the rest of the cards
                break;
            }
        }
        // Return whether all other cards in the player's hand are the same as the first, implying a win
        return noOfSameCards == 3;
    }
    
    public Card chooseDiscard() {
        ArrayList<Card> discardables = new ArrayList<>();
        for (Card card : hand) {
            // Adds each card in the hand to the list of discardable cards if it is not of the preferred number (the player's ID)
            if (card.getValue() != id) discardables.add(card);
        }

        Random rand = new Random();
        // Chooses to discard a random card from the discardables list
        return discardables.get(rand.nextInt(discardables.size()));
    }

    private void recordInit() {
        if (record) {
                
            // Retrieves the string to record to the output file
            String outputString = getInitString();

            try {
                // Creates FileWriter that overwrites the file instead of appending to it
                outputWriter = new FileWriter("player"+id+"_output.txt", false);

                // Writes info about the player's initial hand to the file
                outputWriter.write(outputString);

                // Closes the FileWriter
                outputWriter.close();

                // Creates a new FileWriter that appends to this file instead of overwriting
                outputWriter = new FileWriter("player"+id+"_output.txt", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recordMove(Card drawnCard, Card toDiscard) {
        if (record) {

            // Retrieves the string to record to the output file
            String outputString = getMoveString(drawnCard, toDiscard);

            try {
                // Writes info about the player's draw, discard, and current hand after this move to the file
                outputWriter.write(outputString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recordEnding(int winnerId) {

        if (record) {
            if (winnerId == id) {
                // Outputs which player has won to the console
                System.out.println("player " + id + " wins");
            }

            // Retrieves the string to record to the output file
            String outputString = getEndingString(winnerId);

            try {
                // Closes the old FileWriter and creates a new one so that in the case where two players win simulatenously,
                // the second ending recording does not fail due to the FileWriter being closed by the first ending recording
                outputWriter.close();
                outputWriter = new FileWriter("player"+id+"_output.txt", true);

                // After the game ends, records the winner and this player's final hand in player[id]_output.txt
                outputWriter.write(outputString);
                // Closes the FileWriter
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Tells the deck to the player's left to record its final state
        leftDeck.writeResult();
    }
}
