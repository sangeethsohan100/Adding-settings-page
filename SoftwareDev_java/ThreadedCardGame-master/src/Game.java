import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Christian Wood and Jacob Beeson
 *
 */

public class Game {
    /*Attributes:
    ---------------
    */
    //holds all the Player instances in the game
    private ArrayList<Player> players = new ArrayList<Player>();
    //holds all the CardDeck instances in the game
    private ArrayList<CardDeck> decks = new ArrayList<CardDeck>();
    //holds all the Card instances in the game
    private ArrayList<Card> pack = new ArrayList<Card>();
    //the number of players in the game
    private int numberOfPlayers;
    //the location/name of the pack text file
    private String packLocation;
    //whether the game has been won or not
    private Boolean gameWon = false;
    //the playerNumber of the winning player
    private Integer victorNumber;

    /*Constructor:
    ---------------
    */
    public Game(){ /* Nothing to instantiate */ }

    /*Getters and Setters:
    ---------------
    */
    //returns whether the game has been won or not
    public Boolean getGameWon() {
        return gameWon;
    }

    //returns the playerNumber of the winning player
    public Integer getVictorNumber() {
        return victorNumber;
    }

    //returns the name/location of the pack text file
    public String getPackLocation() {
        return packLocation;
    }

    //returns the list of players
    public ArrayList getPlayerList(){
        return players;
    }

    //returns the list of decks
    public ArrayList getDeckList(){
        return decks;
    }

    //returns the pack
    public ArrayList getPack(){
        return pack;
    }

    public Integer getNumberOfPlayers(){
        return this.numberOfPlayers;
    }


    //sets the gameWon attribute
    public void setGameWon(Boolean gameWon) {
        this.gameWon = gameWon;
    }

    //sets the victorNumber attribute
    public void setVictorNumber(Integer victorNumber) {
        this.victorNumber = victorNumber;
    }

    // check
    //sets the numberOfPlayers attribute
    public void setNumberOfPlayers(Integer num){
        this.numberOfPlayers = num;
    }

    public void setPackLocation(String packLocation){
        this.packLocation = packLocation;
    }
    //Methods:
    //---------------
    /*Methods:
    ---------------
    */
    //takes the user inputs and fills the game instance's attributes
    public void userInputs(){
        Scanner input = new Scanner(System.in);
        //checking pack input is valid
        boolean isValidPackType = false;
        String packLocationInput = null;
        while(!isValidPackType) {
            //checking number of players input is valid
            boolean validNumOfPlayers = false;
            Integer integerNumberOfPlayers;
            while (!validNumOfPlayers) {
                System.out.println("Please enter the number of players: ");
                String numberOfPlayersInput = input.nextLine();
                try {
                    integerNumberOfPlayers = Integer.parseInt(numberOfPlayersInput);
                    if (integerNumberOfPlayers <= 0) {
                        System.out.println("Please enter a non negative number of players. ");
                    } else {
                        numberOfPlayers = integerNumberOfPlayers;
                        validNumOfPlayers = true;
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("wrong type of input");
                }
            }
            System.out.println("Please enter pack location: ");
            packLocationInput = input.nextLine();
            isValidPackType = validatePack(packLocationInput);
        }
        input.close();
        packLocation = packLocationInput;
    }

    //checks whether a pack location contains a valid pack
    public boolean validatePack(String packLocation){
        try {
            //opening and reading pack
            File myObj = new File(packLocation);
            Scanner myReader = new Scanner(myObj);
            int counter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //checking that lines only contain a single value
                //" " at the end of lines they are automatically removed from files so
                //no risk of eg "3 " being flagged as containing more than one value
                if (data.contains(" ")) {
                    System.out.println("One of your lines contains more than a single number");
                    return false;
                } else {
                    try{
                        //checks that every value is an integer
                        Integer number = Integer.parseInt(data);
                        //checks every number is positive
                        if (number < 0){
                            System.out.println("One of the cards was negative");
                            return false;
                        }
                    }
                    catch (NumberFormatException ex){
                        System.out.println("One of the cards was not an integer");
                        return false;
                    }
                }
                counter += 1;
            }
            myReader.close();
            //checks that there are 8n values in the card pack where n is the number
            //of players previously inputted by the user in the command line
            if (counter == (numberOfPlayers * 8)){
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

    //creates a pack of cards for a given pack location
    public void createPack() throws FileNotFoundException {
        File myObj = new File(this.packLocation);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            pack.add(new Card(Integer.parseInt(data)));
        }
        myReader.close();
    }

    //creates n decks (one for each player)
    public void createDecks(){
        for (int i=1;i<=numberOfPlayers;i++){
            decks.add(new CardDeck(i));
        }

    }

    //creates players
    public void createPlayers(){
        for (int i=0;i<numberOfPlayers;i++) {
            //if i=0 then the player's deck will be the deck on the right of the
            //last player so will have a deck number of numberOfPlayers - 1
            if (i==0) {
                players.add(new Player((i+1), decks.get(numberOfPlayers - 1), decks.get(i), this));
            } else {
                players.add(new Player((i+1), decks.get(i - 1), decks.get(i), this));
            }
        }
    }

    //distributes the cards in a round robing fashion to the players and then the decks
    public void distributeCards(){
        for (int i=0;i<4;i++){
            for (int j=0;j<numberOfPlayers;j++){
                players.get(j).dealCard(pack.get(i*numberOfPlayers + j));
            }
        }
        for (int i=0;i<4;i++){
            for (int j=0;j<numberOfPlayers;j++){
                decks.get(j).dealCard(pack.get((i*numberOfPlayers + j) + (4*numberOfPlayers)));
            }
        }
    }

    //starts the game by starting the player's run methods through .start()
    public void startGame(){
        //check that none of the players has won off of the deal
        for (int i=0;i<numberOfPlayers;i++) {
            Player currentPlayer = players.get(i);
            currentPlayer.writeInitialHand();
            if (currentPlayer.checkWin()) {
                currentPlayer.setVictoryAttributes();
            }
        }
        //loop through all the players and call .start() for them
        for (int i=0;i<numberOfPlayers;i++) {
            Player currentPlayer = players.get(i);
            currentPlayer.start();
        }
    }
}
