import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Thread {

    private int id; // player number

    private ArrayList<Card> hand = new ArrayList<>(); // list of cards in hand of player

    private CardDeck lDeck, rDeck; // left and right deck

    // the state of the game the player is in
    private final CardGame gameState;

    public Player(int playerNumber, CardGame gamestate, CardDeck leftDeck, CardDeck rightDeck) {
        this.id = playerNumber;
        this.gameState = gamestate;
        this.lDeck = leftDeck;
        this.rDeck = rightDeck;
    }

    @Override
    public void run() {
        while (!gameState.getGameWon()) {
            if (checkWin()) {
                setGameWinner();
            } else {
                if (!lDeck.getDeckCards().isEmpty()) {
                    playerMove();
                    writeMove();
                }
            }
        }
        writeFinalHand();
        rDeck.writeDeckCards();
    }

    public void addCardtoHand(Card card) {
        hand.add(card);
    }

    // defines the actions a player should take when they are able to play
    private void playerMove() {
        Card drawnCard;
        Card placeCard;

        // only the methods that directly access the decks are synchronized to not waste
        // resources
        synchronized (Player.class) {
            drawnCard = drawCard();
            // adds the drawn card to the player's hand
            hand.add(drawnCard);
            // selects the card to discard
            placeCard = selectCardToDiscard();
            // places the card to discard to the deck on the player's right
            removeCard(placeCard);
        }
        try {
            // writing the move to the player's output file
            FileWriter playerMoveWriter = new FileWriter("Player " + id + "_output.txt", true);
            playerMoveWriter.write("\nplayer " + id + " draws card number " + drawnCard.getCardValue() + " from deck "
                    + lDeck.getDeckNumber());
            playerMoveWriter.write("\nplayer " + id + " discards card number " + placeCard.getCardValue() + " to deck "
                    + rDeck.getDeckNumber());
            playerMoveWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing a move to the file.");
            e.printStackTrace();
        }
    }

    // calls the drawCard method from the deck on the player's left
    private Card drawCard() {
        return lDeck.getTopCard();
    }

    // calls the dealCard method from the deck on the player's right and gives it
    // the card instance to place
    private void removeCard(Card card) {
        hand.remove(card);
        rDeck.addCardtoDeck(card);
    }

    // checks whether all the cards in the players hand have the same value
    public Boolean checkWin() {
        return hand.get(0).getCardValue() == hand.get(1).getCardValue()
                && hand.get(0).getCardValue() == hand.get(2).getCardValue()
                && hand.get(0).getCardValue() == hand.get(3).getCardValue();
    }

    // selects the card to discard from the player's hand
    public Card selectCardToDiscard() {
        ArrayList<Card> discardCardList = new ArrayList<>();

        Random random = new Random();
        for (Card card : hand) {
            if (!(card.getCardValue() == id)) {
                discardCardList.add(card);
            }
        }

        int randomDiscard = random.nextInt(discardCardList.size());
        Card discardCard = discardCardList.get(randomDiscard);
        hand.remove(discardCard);
        return discardCard;
    }

    public String getPlayerHand() {
        String handString = "";
        for (Card card : hand) {
            handString = handString + card.getCardValue() + " ";
        }
        return handString;
    }

    public synchronized void setGameWinner() {
        gameState.setPlayerWin(true);
        gameState.setWinnerId(id);
        System.out.println("player " + gameState.getWinnerId() + " wins");
    }

    // writes the player's initial hand to their output file
    public void writeInitialHand() {
        try {
            File intialHandFile = new File("Player " + id + "_output.txt");
            intialHandFile.createNewFile();
        } catch (IOException e) {
            System.out.println("There was an error when creating a file.");
            e.printStackTrace();
        }

        try {
            FileWriter initialHandWriter = new FileWriter("Player " + id + "_output.txt");
            initialHandWriter.write("player " + id + " initial hand " + getPlayerHand() + "\n");
            initialHandWriter.close();
        } catch (IOException e) {
            System.out.println("There was an error when writing the initial hand to a file.");
            e.printStackTrace();
        }
    }

    // writes the player's move to their output file
    public void writeMove() {
        try {
            FileWriter moveWriter = new FileWriter("Player " + this.id + "_output.txt", true);
            moveWriter.write("\nplayer " + this.id + " current hand is " + getPlayerHand() + "\n");
            moveWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing a move to a file.");
            e.printStackTrace();
        }
    }

    // writes the player's final hand to their output file
    public synchronized void writeFinalHand() {

        if (gameState.getWinnerId() == id) {
            try {
                FileWriter finalHandWriter = new FileWriter("Player " + this.id + "_output.txt", true);
                finalHandWriter.write("\nplayer " + gameState.getWinnerId() + " wins\n");
                finalHandWriter.write("player " + id + " exits\n");
                finalHandWriter.write("\nplayer " + id + " final hand " + getPlayerHand());
                finalHandWriter.close();

            } catch (IOException e) {
                System.out.println("An error occurred writing this player's win to a file.");
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter finalHandWriter = new FileWriter("Player " + this.id + "_output.txt", true);

                finalHandWriter
                        .write("\nplayer " + gameState.getWinnerId() + " has informed player " + getPlayerNumber()
                                + " that player " + gameState.getWinnerId() + " has won\n");
                finalHandWriter.write("player " + id + " exits\n");
                finalHandWriter.write("\nplayer " + id + " final hand " + getPlayerHand());
                finalHandWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred writing another player's win to a file.");
                e.printStackTrace();
            }
        }
    }

    public int getPlayerNumber() {
        return id;
    }

    // returns the player's hand
    public ArrayList getPlayerHandList() {
        return hand;
    }

}
