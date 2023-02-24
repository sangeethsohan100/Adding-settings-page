import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Thread {

    private int id; // player number

    private CardDeck lDeck, rDeck; // left and right deck

    private ArrayList<Card> hand = new ArrayList<>(); // list of cards in hand of player

    // the state of the game the player is in
    private final CardGame gameState;

    // Constructor
    public Player(int playerNumber, CardGame gamestate, CardDeck leftDeck, CardDeck rightDeck) {
        this.id = playerNumber;
        this.gameState = gamestate;
        this.lDeck = leftDeck;
        this.rDeck = rightDeck;
    }

    @Override
    public void run() {
        while (!gameState.getGameWon()) {
            if (checkWinner()) {
                setGameWinner();
            } else {
                if (!lDeck.getDeckCards().isEmpty()) {
                    playerMove();
                    writeMove();
                }
            }
        }
        writeLoserFinalHand();
        writeWinnerFinalHand();
        rDeck.writeDeckCards();
    }

    public int getPlayerNumber() {
        return id;
    }

    // card is drawn from left deck
    private Card drawCard() {
        return lDeck.getTopCard();
    }

    // returns the player's hand
    public ArrayList getHandList() {
        return hand;
    }

    // card is added to the hand
    public void addCardtoHand(Card card) {
        hand.add(card);
    }

    // removes card from hand and adds the card to right deck
    private void removeCard(Card card) {
        hand.remove(card);
        rDeck.addCardtoDeck(card);
    }

    // checks whether all the cards in the players hand have the same value
    public synchronized Boolean checkWinner() {
        int cardValueCheck = this.hand.get(0).getCardValue();
        // the first player declares that they have four cards of the same value wins
        for (int i = 1; i < hand.size(); i++) {
            if (cardValueCheck != this.hand.get(i).getCardValue()) {
                return false;
            }
        }
        return true;
    }

    // selects the card to discard from the player's hand
    public Card randomCardDiscard() {
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

    // adds cards in the player hand into a string
    public String getPlayerHand() {
        String handString = "";
        for (Card card : hand) {
            handString = handString + card.getCardValue() + " ";
        }
        return handString;
    }

    // writng to the output file which cards the player has drawn and discarded from
    // and to a deck
    private void playerMove() {
        Card drawnCard;
        Card placeCard;

        drawnCard = drawCard();

        hand.add(drawnCard);

        placeCard = randomCardDiscard();

        removeCard(placeCard);

        try {
            // writing the move to the player's output file
            BufferedWriter playerMoveWriter = new BufferedWriter(
                    new FileWriter("Player " + this.id + "_output.txt", true));
            playerMoveWriter.write("\nplayer " + id + " draws card number " + drawnCard.getCardValue() + " from deck "
                    + lDeck.getDeckNumber());
            playerMoveWriter.write("\nplayer " + id + " discards card number " + placeCard.getCardValue() + " to deck "
                    + rDeck.getDeckNumber());
            playerMoveWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing a move to the file.");
            e.printStackTrace();
        }
    }

    public synchronized void setGameWinner() {
        gameState.setPlayerWin(true);
        gameState.setWinnerId(id);
        System.out.println("player " + gameState.getWinnerId() + " wins");
    }

    // writes the player's initial hand to their output file
    public void writeInitialHand() {
        try {
            BufferedWriter initialHandWriter = new BufferedWriter(new FileWriter("Player " + id + "_output.txt"));
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
            BufferedWriter moveWriter = new BufferedWriter(new FileWriter("Player " + this.id + "_output.txt", true));
            moveWriter.write("\nplayer " + this.id + " current hand is " + getPlayerHand() + "\n");
            moveWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing a move to a file.");
            e.printStackTrace();
        }
    }

    // writes the final hand of the player who have not won the game
    public synchronized void writeLoserFinalHand() {

        if (id != gameState.getWinnerId()) {
            try {
                BufferedWriter loserHandWriter = new BufferedWriter(
                        new FileWriter("Player " + this.id + "_output.txt", true));

                loserHandWriter
                        .write("\nplayer " + gameState.getWinnerId() + " has informed player " + getPlayerNumber()
                                + " that player " + gameState.getWinnerId() + " has won\n");
                loserHandWriter.write("player " + id + " exits\n");
                loserHandWriter.write("\nplayer " + id + " final hand " + getPlayerHand());
                loserHandWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred when writing another player's win to a file.");
                e.printStackTrace();
            }

        }
    }

    // writes the player's final hand to their output file
    public synchronized void writeWinnerFinalHand() {

        if (id == gameState.getWinnerId()) {
            try {
                BufferedWriter winnerHandWriter = new BufferedWriter(
                        new FileWriter("Player " + this.id + "_output.txt", true));
                winnerHandWriter.write("\nplayer " + gameState.getWinnerId() + " wins\n");
                winnerHandWriter.write("player " + id + " exits\n");
                winnerHandWriter.write("\nplayer " + id + " final hand " + getPlayerHand());
                winnerHandWriter.close();

            } catch (IOException e) {
                System.out.println("An error occurred when writing the player's win to a file.");
                e.printStackTrace();
            }

        }
    }

}
