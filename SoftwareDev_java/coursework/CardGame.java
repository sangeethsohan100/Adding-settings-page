import java.io.IOException;

public class CardGame {

    public static void main(String[] args) throws IOException {

        Game game = new Game();
        game.userInput();

        game.createDeck();

        game.createPlayers();

        game.createPack();

        game.distributeCards();

        game.startGame();

    }

}
