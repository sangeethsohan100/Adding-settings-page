import java.io.FileNotFoundException;

/**
 *
 * @author Christian Wood and Jacob Beeson
 *
 */

public class CardGame {
    public static void main(String[] args) throws FileNotFoundException {
        //creating an instance of the game class
        Game game = new Game();

        //getting user inputs
        game.userInputs();

        //creating decks
        game.createDecks();

        //creating players
        game.createPlayers();

        //create pack
        game.createPack();

        //distribute cards to players and decks
        game.distributeCards();

        //start game
        game.startGame();
    }
}
