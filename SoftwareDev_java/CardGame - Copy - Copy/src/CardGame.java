import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private ArrayList<CardDeck> decks = new ArrayList<CardDeck>();

    private ArrayList<Player> players = new ArrayList<Player>();
    private String packLocation;

    private int noOfPlayers;

    private ArrayList<Card> pack = new ArrayList<Card>();

    private int winnerId;

    private boolean gameWon = false;

    public static void main(String[] args) throws FileNotFoundException {
        // creating an instance of the game
        CardGame game = new CardGame();

        // getting user inputs
        game.userInputs();

        // creating decks
        game.createDecks();

        // creating players
        game.createPlayers();

        // create pack
        game.createPack();

        // distribute cards to players and decks
        game.moveCards();

        // start game
        game.startGame();
    }

    public Boolean getGameWon() {
        return gameWon;
    }

    public ArrayList getPlayers() {
        return players;
    }

    // returns the list of decks
    public ArrayList getDecks() {
        return decks;
    }

    // returns the pack
    public ArrayList getPack() {
        return pack;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getNumberOfPlayers() {
        return this.noOfPlayers;
    }

    // sets the gameWon attribute
    public void setPlayerWin(Boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void setNumberOfPlayers(int num) {
        this.noOfPlayers = num;
    }

    public void userInputs() {
        Scanner input = new Scanner(System.in);
        // checking pack input is valid
        boolean isValidPackType = false;
        String locationInput = null;
        while (!isValidPackType) {

            boolean validNumOfPlayers = true;
            Integer intNoOfPlayers;
            while (validNumOfPlayers) {
                System.out.println("Please enter the number of players: ");
                String noOfPlayersInput = input.nextLine();
                try {
                    intNoOfPlayers = Integer.parseInt(noOfPlayersInput);
                    if (intNoOfPlayers <= 0) {
                        System.out.println("Please enter a non negative number of players. ");
                    } else {
                        noOfPlayers = intNoOfPlayers;
                        validNumOfPlayers = false;
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid number of players, please enter the valid number of players");
                }
            }
            System.out.println("Please enter pack location: ");
            locationInput = input.nextLine();
            isValidPackType = validPack(locationInput);
        }
        input.close();
        packLocation = locationInput;
    }

    // checks whether a pack location contains a valid pack
    public boolean validPack(String packLocation) {
        try {
            // opening and reading pack
            File packFile = new File(packLocation);
            Scanner packReader = new Scanner(packFile);

            int counter = 0;

            while (packReader.hasNextLine()) {
                String data = packReader.nextLine();
                // checking that lines only contain a single value
                // " " at the end of lines they are automatically removed from files so
                // no risk of eg "3 " being flagged as containing more than one value
                if (data.contains(" ")) {
                    System.out.println("One of your lines contains more than a single number");
                    return false;
                } else {
                    try {
                        // checks that every value is an integer
                        int number = Integer.parseInt(data);
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
            packReader.close();

            if (counter == (noOfPlayers * 8)) {
                System.out.println("Pack is valid. ");
                return true;
            } else {
                System.out.println("Invalid number of cards for the number of players. ");
                return false;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. ");
            return false;
        }
    }

    // creates a pack of cards for a given pack location
    public void createPack() throws FileNotFoundException {
        File packFile = new File(this.packLocation);
        Scanner packReader = new Scanner(packFile);
        while (packReader.hasNextLine()) {
            String data = packReader.nextLine();
            pack.add(new Card(Integer.parseInt(data)));
        }
        packReader.close();
    }

    public void setPackLocation(String packLocation) {
        this.packLocation = packLocation;
    }

    public String getPackLocation() {
        return packLocation;
    }

    // creates n decks (one for each player)
    public void createDecks() {
        for (int i = 1; i <= noOfPlayers; i++) {
            decks.add(new CardDeck(i));
        }

    }

    // creates players
    public void createPlayers() {
        for (int i = 0; i < noOfPlayers; i++) {
            
            if (i == 0) {
                players.add(new Player((i + 1), this, decks.get(noOfPlayers - 1), decks.get(i)));
            } else {
                players.add(new Player((i + 1), this, decks.get(i - 1), decks.get(i)));
            }
        }
    }

    // distributes the cards in a round robing fashion to the players and then the
    // decks
    public void moveCards() {
        for (int i = 0; i < 4; i++) {
            for (int n = 0; n < noOfPlayers; n++) {
                players.get(n).addCardtoHand(pack.get(i * noOfPlayers + n));
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int n = 0; n < noOfPlayers; n++) {
                decks.get(n).addCardtoDeck(pack.get((i * noOfPlayers + n) + (4 * noOfPlayers)));
            }
        }
    }

    // starts the game by starting the player's run methods through .start()
    public void startGame() {
        // check that none of the players has won off of the deal
        for (int i = 0; i < noOfPlayers; i++) {
            Player currentPlayer = players.get(i);
            currentPlayer.writeInitialHand();
            if (currentPlayer.checkWin()) {
                currentPlayer.setGameWinner();
            }
        }
        // loop through all the players and call .start() for them
        for (int i = 0; i < noOfPlayers; i++) {
            Player currentPlayer = players.get(i);
            currentPlayer.start();
        }
    }

}
