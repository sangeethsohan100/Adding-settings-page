import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Christian Wood and Jacob Beeson
 *
 */

public class Player extends Thread{
    /*Attributes:
    ---------------
    */
    //the player number (also their preferred card)
    private final Integer playerNumber;
    //the cards in the player's hand
    private ArrayList<Card> playerHand = new ArrayList<>();
    //the deck the player pick cards up from
    private final CardDeck leftCardDeck;
    //the deck the player places cards to
    private final CardDeck rightCardDeck;
    //the game instance the player belongs to
    private final Game gameInstance;
    //randomizer instance for use in selecting cards
    private Random random = new Random();

    /*Constructor:
    ---------------
    */
    public Player(Integer playerNumber, CardDeck leftCardDeck, CardDeck rightCardDeck, Game gameInstance) {
        this.playerNumber = playerNumber;
        this.leftCardDeck = leftCardDeck;
        this.rightCardDeck = rightCardDeck;
        this.gameInstance = gameInstance;
    }

    /*Getters and Setters:
    ---------------
    */
    //returns the player's number
    public Integer getPlayerNumber() {
        return playerNumber;
    }

    //returns the player's hand
    public ArrayList getPlayerHandList() {
        return playerHand;
    }

    /*Thread run method:
    ---------------
    */
    //defines the behaviour for a thread once the start method is called
    @Override
    public void run(){
        while (!gameInstance.getGameWon()){
            if (checkWin()){
                setVictoryAttributes();
            } else {
                if (!leftCardDeck.getDeckHand().isEmpty()){
                    playMove();
                    writeMove();
                }
            }
        }
        writeFinalHand();
        rightCardDeck.writeHand();
    }

    /*Methods:
    ---------------
    */
    //adds a card instance to the player's hand
    public void dealCard(Card card){
        playerHand.add(card);
    }

    //defines the actions a player should take when they are able to play
    private void playMove(){
        Card cardToPlace;
        Card cardDrawn;
        //only the methods that directly access the decks are synchronized to not waste resources
        synchronized (Player.class) {
            cardDrawn = drawCard();
            //adds the drawn card to the player's hand
            playerHand.add(cardDrawn);
            //selects the card to discard
            cardToPlace = selectCardToDiscard();
            //places the card to discard to the deck on the player's right
            placeCard(cardToPlace);
        }
        try {
            //writing the move to the player's output file
            FileWriter myWriter = new FileWriter("player" + playerNumber + "_output.txt", true);
            myWriter.write("\nplayer " + playerNumber + " draws a " + cardDrawn.getCardValue() + " from deck " + leftCardDeck.getDeckNumber());
            myWriter.write("\nplayer " + playerNumber + " discards a " + cardToPlace.getCardValue() + " to deck " + rightCardDeck.getDeckNumber() );
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing a move to the file.");
            e.printStackTrace();
        }
    }

    //calls the drawCard method from the deck on the player's left
    private Card drawCard(){
        return leftCardDeck.drawCard();
    }

    //calls the dealCard method from the deck on the player's right and gives it the card instance to place
    private void placeCard(Card cardToPlace){
        rightCardDeck.dealCard(cardToPlace);
    }

    //checks whether all the cards in the players hand have the same value
    public Boolean checkWin(){
        return (playerHand.get(0).getCardValue().equals(playerHand.get(1).getCardValue()) && playerHand.get(0).getCardValue().equals(playerHand.get(2).getCardValue()) && playerHand.get(0).getCardValue().equals(playerHand.get(3).getCardValue()));
    }

    //selects the card to discard from the player's hand
    public Card selectCardToDiscard(){
        ArrayList<Card> possibleDiscardCards = new ArrayList<>();
        //creates an ArrayList filled with all the cards that aren't the player's preferred card
        for (Card card : playerHand) {
            if (!(card.getCardValue().equals(playerNumber))) {
                possibleDiscardCards.add(card);
            }
        }
        //selects a random integer which is the index for the card that we will remove and returns the card
        int randomIndex = random.nextInt(possibleDiscardCards.size());
        Card cardToDiscard = possibleDiscardCards.get(randomIndex);
        playerHand.remove(cardToDiscard);
        return cardToDiscard;
    }

    //returns a string of the cards in the player's hand
    public String getPlayerHand(){
        String hand = "";
        for (Card card : playerHand) {
            hand = hand + card.getCardValue() + " ";
        }
        return hand;
    }

    //sets the victory attributes in the game the player belongs to this
    //is synchronized as we don't want another player playing their move
    //if a player has won and is declaring this win to the game instance
    public synchronized void setVictoryAttributes() {
        gameInstance.setVictorNumber(playerNumber);
        gameInstance.setGameWon(true);
        System.out.println("player "+gameInstance.getVictorNumber()+" wins");
    }

    //writes the player's initial hand to their output file
    public void writeInitialHand(){
        try {
            File myObj = new File("player" + playerNumber + "_output.txt");
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred creating a file.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("player" + playerNumber + "_output.txt");
            myWriter.write("player " + playerNumber + " initial hand " + getPlayerHand() + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing initial hand to a file.");
            e.printStackTrace();
        }
    }

    //writes the player's move to their output file
    public void writeMove(){
        try {
            FileWriter myWriter = new FileWriter("player"+this.playerNumber+"_output.txt", true);
            myWriter.write("\nplayer "+this.playerNumber+" current hand is "+getPlayerHand()+ "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing a move to a file.");
            e.printStackTrace();
        }
    }

    //writes the player's final hand to their output file
    public void writeFinalHand(){
        Integer playerNumberOfVictor = gameInstance.getVictorNumber();
        if (playerNumberOfVictor == playerNumber){
            try {
                FileWriter myWriter = new FileWriter("player"+this.playerNumber+"_output.txt", true);
                myWriter.write("\nplayer " + playerNumber + " wins\n");
                myWriter.write("player " + playerNumber + " exits\n");
                myWriter.write("player " + playerNumber + " final hand " + getPlayerHand());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred writing this player's win to a file.");
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter myWriter = new FileWriter("player"+this.playerNumber+"_output.txt", true);
                myWriter.write("\nplayer " + gameInstance.getVictorNumber() + " has informed player " + playerNumber + " that player " + gameInstance.getVictorNumber() + " has won\n");
                myWriter.write("player " + playerNumber + " exits\n");
                myWriter.write("player " + playerNumber + " final hand " + getPlayerHand());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred writing another player's win to a file.");
                e.printStackTrace();
            }
        }
    }
}
