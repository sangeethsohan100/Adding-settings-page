import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    static Scanner scan = new Scanner(System.in);
    static int noOfPlayers;
    public ArrayList<Card> hand = new ArrayList<Card>();
    ArrayList<Player> player = new ArrayList<>();
    public ArrayList<Card> cards = new ArrayList<Card>();
    public ArrayList<CardDeck> deck = new ArrayList<>();
    private String packLocation;

    public void userInput() {

        String inputPackLocation;
        boolean validPack;

        while (validPack = false) {
            boolean validNoOfPlayers;
            int positiveNoOfPlayers;

            while (validNoOfPlayers = false) {
                System.out.println("Enter the number of players");
                String playerInput = scan.nextLine();
                try {
                    positiveNoOfPlayers = Integer.parseInt(playerInput);
                    if (positiveNoOfPlayers <= 0) {
                        System.out.println("Enter a non-negative number of players");
                    } else {
                        noOfPlayers = positiveNoOfPlayers;
                        validNoOfPlayers = true;

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Please type in a valid input");
                }
                System.out.println("Enter the location of the pack file");
                inputPackLocation = scan.nextLine();
                validPack = checkValidPack(inputPackLocation);
            }
        }
    }

    public boolean checkValidPack(String packLocation) {
        try {
            // opening and reading pack
            File myObj = new File(packLocation);
            Scanner myReader = new Scanner(myObj);
            int counter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                // checking that lines only contain a single value
                // " " at the end of lines they are automatically removed from files so
                // no risk of eg "3 " being flagged as containing more than one value
                if (data.contains(" ")) {
                    System.out.println("One of your lines contains more than a single number");
                    return false;
                } else {
                    try {
                        // checks that every value is an integer
                        Integer number = Integer.parseInt(data);
                        // checks every number is positive
                        if (number < 0) {
                            System.out.println("One of the cards was negative");
                            return false;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("One of the cards was not an integer");
                        return false;
                    }
                }
                counter += 1;
            }
            myReader.close();
            // checks that there are 8n values in the card pack where n is the number
            // of players previously inputted by the user in the command line
            if (counter == (noOfPlayers * 8)) {
                System.out.println("Pack is valid. ");
                return true;
            } else {
                System.out.println("Not the right number of cards for the number of players. ");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. ");
            return false;
        }
    }

    public void createPack() throws FileNotFoundException {
        File myObj = new File(this.packLocation);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            cards.add(new Card(Integer.parseInt(data)));
        }
        myReader.close();
    }

    public void createDeck() {

        for (int i = 0; i < noOfPlayers; i++) {
            deck.add(new CardDeck(i));
        }

    }

    public void createPlayers() {
        for (int i = 0; i < noOfPlayers; i++) {
            // if i=0 then the player's deck will be the deck on the right of the
            // last player so will have a deck number of numberOfPlayers - 1
            if (i == 0) {
                player.add(new Player(deck.get(noOfPlayers - 1), deck.get(i), (i + 1)));
            } else {
                player.add(new Player(deck.get(i - 1), deck.get(i), (i + 1)));
            }
        }
    }

    public void distributeCards() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < noOfPlayers; j++) {
                player.get(j).addCard(cards.get(i * noOfPlayers + j));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < noOfPlayers; j++) {
                deck.get(j).addCard(cards.get((i * noOfPlayers + j) + (4 * noOfPlayers)));
            }
        }
    }

    public void startGame() {
        // check that none of the players has won off of the deal
        for (int i = 0; i < noOfPlayers; i++) {
            Player currentPlayer = player.get(i);
            currentPlayer.writeStartingHand();
            if (currentPlayer.checkPlayerWin()) {
                currentPlayer.setWinnerAttributes();
            }
        }
        // loop through all the players and call .start() for them
        for (int i = 0; i < noOfPlayers; i++) {
            Player currentPlayer = player.get(i);
            currentPlayer.start();
        }
    }

}
