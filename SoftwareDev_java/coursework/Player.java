import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Player extends Thread {
    // Thread Safe

    public ArrayList<Card> hand = new ArrayList<Card>();
    // int count;
    public ArrayList<String> players = new ArrayList<>();
    private int playercard; // preffered card

    private int id;
    private CardDeck lDeck, rDeck; // left and right deck
    public ArrayList<Card> card = new ArrayList<>();
    private int winnerid;
    private boolean playerWin;

    public Player(CardDeck left, CardDeck right, int playernumber) {
        this.id = playernumber;
        this.playercard = playernumber;
        this.lDeck = left;
        this.rDeck = right;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    private Card drawCard() {

        Card draw = lDeck.drawCard();

        return draw;

    }

    private void discardCard(Card card, CardDeck rdDeck) {
        rdDeck.addCard(card);
        hand.remove(card);
    }

    private Card randomDiscard() {
        ArrayList<Card> discard = new ArrayList<>();
        for (Card card : hand) {
            if (card.getValue() != id) {
                discard.add(card);
            }
        }

        Random rand = new Random();
        // Chooses to discard a random card from the discard list
        return discard.get(rand.nextInt(discard.size()));
    }

    public void setPlayerNumber(int playernumber) {
        this.id = playernumber;

    }

    public int getPlayerNumber() {
        return this.id;
    }

    public String getPlayerHand() {
        String playerHand = "";
        for (int i = 0; i < hand.size(); i++) {
            playerHand = playerHand + String.valueOf(hand.get(i).getValue()) + "";
        }

        return playerHand;
    }

    public int getPlayerCard() {
        return playercard;
    }

    public void run() {

        while (!getPlayerWin()) {
            if (checkPlayerWin()) {
                setWinnerAttributes();
            } else {
                if (lDeck.getDeckSize().isEmpty()) {
                    takeTurn();
                    writePlayerMove();
                }
            }
        }

    }

    private void takeTurn() {

        Card placeCard;
        Card drawnCard;
        synchronized (Player.class) {
            drawnCard = drawCard();
            hand.add(drawnCard);
            placeCard = randomDiscard();
            discardCard(placeCard, rDeck);
        }

        try {
            FileWriter writer = new FileWriter("player" + id + "_output.txt", true);
            writer.write("\nplayer " + id + " draws a " + drawnCard.getValue() + " from deck " + lDeck.getDeckNumber());
            writer.write(
                    "\nplayer " + id + " discards a " + placeCard.getValue() + " to deck " + rDeck.getDeckNumber());
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred writing a move to the file.");
            e.printStackTrace();
        }

    }

    public synchronized void setWinnerAttributes() {
        setWinnerId(id);
        setPlayerWin(true);
        System.out.println("player" + getWinnerId() + "is the winner");
    }

    public Boolean checkPlayerWin() {
        return hand.get(0).getValue() == hand.get(1).getValue() && hand.get(0).getValue() == hand.get(2).getValue()
                && hand.get(0).getValue() == hand.get(3).getValue();
    }

    public void setWinnerId(int id) {
        this.winnerid = id;
    }

    public int getWinnerId() {
        return winnerid;
    }

    public void setPlayerWin(Boolean playerWin) {
        this.playerWin = playerWin;
    }

    public Boolean getPlayerWin() {
        return playerWin;
    }

    public void writeStartingHand() {
        try {
            File createFile = new File("player" + id + "_output.txt");
            createFile.createNewFile();
        } catch (IOException e) {
            System.out.println("There was an error when creating a file.");
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter("player" + id + "_output.txt");
            writer.write("player " + id + " initial hand " + getPlayerHand() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("There was an error when writing initial hand to the file.");
            e.printStackTrace();
        }

    }

    public void writePlayerMove() {
        try {
            FileWriter writer = new FileWriter("player" + this.id + "_output.txt", true);
            writer.write("player " + this.id + " current hand is " + getPlayerHand() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing a move to a file.");
            e.printStackTrace();
        }
    }

    public void writeFinalHand() {
        int idOfWinner = getWinnerId();
        if (id == idOfWinner) {
            try {
                FileWriter writer = new FileWriter("player" + this.id + "_output.txt", true);
                writer.write("player " + id + " wins\n");
                writer.write("player " + id + " exits\n");
                writer.write("player " + id + " final hand is: " + getPlayerHand());
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred when writing the player's final hand to the file");
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter writer = new FileWriter("player" + this.id + "_output.txt", true);
                writer.write("player " + getWinnerId() + " has informed player " + id + " that player "
                        + getWinnerId() + " has won\n");
                writer.write("player " + id + " exits\n");
                writer.write("player " + id + " final hand " + getPlayerHand());
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred when writing the player's final hand to the file");
                e.printStackTrace();
            }
        }

    }

}