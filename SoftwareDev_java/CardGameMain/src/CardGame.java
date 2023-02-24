import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    private ArrayList<CardDeck> decks = new ArrayList<CardDeck>(); // deck list

    private ArrayList<Player> players = new ArrayList<Player>(); // list of players
    private String packLocation;

    private int noOfPlayers;// number of players

    private ArrayList<Card> pack = new ArrayList<Card>(); // pack list

    private int winnerId; // id of the player who won

    private boolean gameWon = false;

    public static void main(String[] args) throws FileNotFoundException {
        // object for the card Game to call the methods into main
        CardGame game = new CardGame();

        // getting user inputs
        game.userInput();

        // creating decks
        game.createDecks();

        // creating players
        game.createPlayers();

        // creating pack
        game.createPack();

        // distribute cards to players and decks
        game.moveCards();

        // start game
        game.startGame();
    }

    // sets location of the pack
    public void setPackLocation(String packLocation) {
        this.packLocation = packLocation;
    }

    // returns location of the pack
    public String getPackLocation() {
        return packLocation;
    }

    // returns the pack
    public ArrayList getPack() {
        return pack;
    }

    public ArrayList getPlayers() {
        return players;
    }

    // returns number of player
    public int getNumberOfPlayers() {
        return this.noOfPlayers;
    }

    // sets the number of players in the game
    public void setNumberOfPlayers(int num) {
        this.noOfPlayers = num;
    }

    // returns the list of decks
    public ArrayList getDecks() {
        return decks;
    }

    // returns id of the winner of the game
    public int getWinnerId() {
        return winnerId;
    }

    // sets id of the winner of the game
    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public Boolean getGameWon() {
        return gameWon;
    }

    // sets the gameWon attribute
    public void setPlayerWin(Boolean gameWon) {
        this.gameWon = gameWon;
    }

    // takes in the input of the user for number of players and pack location
    public void userInput() {
        Scanner input = new Scanner(System.in);

        boolean validPackType = false;
        String inputLocation = null;
        while (!validPackType) {

            int intNoOfPlayers;
            boolean validNoOfPlayers = true;
            while (validNoOfPlayers) {
                System.out.println("Enter number of players: ");
                String noOfPlayersInput = input.nextLine();
                try {
                    intNoOfPlayers = Integer.parseInt(noOfPlayersInput);
                    if (intNoOfPlayers <= 0) {
                        System.out.println(
                                "Cannot input a negative number, please type in the valid number of players. ");
                    } else {
                        noOfPlayers = intNoOfPlayers;
                        validNoOfPlayers = false;
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid number of players, please enter the valid number of players");
                }
            }
            System.out.println("Enter pack location: ");
            inputLocation = input.nextLine();
            validPackType = validPack(inputLocation);
        }
        input.close();
        packLocation = inputLocation;
    }

    // checks whether a pack location contains a valid pack
    // checking if there is a valid pack in the pack location
    public boolean validPack(String packLocation) {
        try {
            // opening and reading pack
            File packFile = new File(packLocation);
            Scanner packReader = new Scanner(packFile);

            int packCards = 0;

            while (packReader.hasNextLine()) {
                String data = packReader.nextLine();
                if (data == "") {
                    System.out.println("There is more than a single number in one of the lines");
                    return false;
                } else {
                    try {
                        // checks that if all the values are an integer
                        int number = Integer.parseInt(data);

                        // checks that there is no negative card numbers
                        if (number < 0) {
                            System.out.println("One of the card number is negative");
                            return false;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("One of the card number is not an integer");
                        return false;
                    }
                }
                packCards += 1;
            }
            packReader.close();

            // checks if number of cards in pack equals to 8n
            if (packCards == (noOfPlayers * 8)) {
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

    // for each player n decks are created
    public void createDecks() {
        for (int n = 1; n <= noOfPlayers; n++) {
            decks.add(new CardDeck(n));
        }

    }

    // creates players
    public void createPlayers() {
        for (int n = 0; n < noOfPlayers; n++) {

            if (n == 0) {
                players.add(new Player((n + 1), this, decks.get(noOfPlayers - 1), decks.get(n)));
            } else {
                players.add(new Player((n + 1), this, decks.get(n - 1), decks.get(n)));
            }
        }
    }

    // cards are moved in a round robin fashion to players and decks
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
        // check that none of the players has won of the deal
        for (int i = 0; i < noOfPlayers; i++) {
            Player currentPlayer = players.get(i);
            currentPlayer.writeInitialHand();
            if (currentPlayer.checkWinner()) {
                currentPlayer.setGameWinner();
            }
        }
        // loop through all the players and call .start() for them
        for (int n = 0; n < noOfPlayers; n++) {
            Player currentPlayer = players.get(n);
            currentPlayer.start();
        }
    }

}
