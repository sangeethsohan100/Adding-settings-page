package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {

    public static void main(String[] args) {

        // Scanner to read inputs
        Scanner userInp = new Scanner(System.in);
        
        // Retrieving no. of players from user input
        int noPlayers = getNoPlayers(userInp);

        // Retrieving card pack from file specified in user input
        ArrayList<Card> pack = getPack(noPlayers, userInp);

        // Instantiating card decks
        ArrayList<CardDeck> decks = createDecks(noPlayers, true);

        // Instantiating players
        ArrayList<PlayerThread> players = createPlayers(noPlayers, decks, true);

        // Dealing out cards in pack to players and decks
        dealOutCards(players, decks, pack);

        // Starting each player thread
        startPlaying(players);
    }

    private static ArrayList<Card> getPack(int noPlayers, Scanner userInp) {

        // Asks the user to input the file location of the pack
        System.out.println("Please enter location of pack to load:");
        String packFile = userInp.nextLine();

        ArrayList<Card> pack = new ArrayList<>();
        boolean validPack = false;
        while (!validPack) {
            try {
                Scanner fileScanner = new Scanner(new File(packFile));
                while (fileScanner.hasNextLine()) {
                    // Goes through each line in the file and parses each line to an integer
                    // Parses unsigned int so that if any value that isn't a positive integer is in the pack file it throws an exception
                    int cardVal = Integer.parseUnsignedInt(fileScanner.nextLine());
                    // A new Card is instantiated with this value and added to the pack list
                    pack.add(new Card(cardVal));
                }
                if (pack.size() == 8*noPlayers) {
                    // If the pack list generated is the correct size 8n, where n is the number of players, the pack is valid and the scanners will close
                    validPack = true;
                    fileScanner.close();
                    userInp.close();
                } else {
                    // If the size of the pack is not 8n, where n is the number of players, a new pack file will be requested
                    System.out.println("Invalid pack size, please enter location of a valid pack to load:");
                    packFile = userInp.nextLine();                    
                }
            } catch (Exception e) {
                // If something goes wrong while creating the pack, e.g. there is an invalid line or the file does not exist, a new pack will be requested
                System.out.println("Invalid pack name, please enter location of a valid pack to load:");
                packFile = userInp.nextLine();
            }
        }
        return pack;
    }

    private static int getNoPlayers(Scanner userInp) {
        // Get number of players
        int noPlayers = 0;
        System.out.println("Please enter the number of players:");
        boolean validNoPlayers = false;
        while (!validNoPlayers) {
            try {
                noPlayers = Integer.parseUnsignedInt(userInp.nextLine());
                validNoPlayers = true;
            } catch (Exception e) {
                System.out.println("Invalid number of players, please enter a valid number:");         
            }
        }
        return noPlayers;
    }

    public static ArrayList<CardDeck> createDecks(int noPlayers, boolean record) {
        ArrayList<CardDeck> decks = new ArrayList<>(); // List of decks, equal to no. of players
        for (int i=0; i<noPlayers; i++) {
            decks.add(new CardDeck(i+1, record));
        }
        return decks;
    }

    public static ArrayList<PlayerThread> createPlayers(int noPlayers, ArrayList<CardDeck> decks, boolean record) {
        ArrayList<PlayerThread> players = new ArrayList<>(); // List of players
        for (int i=0; i<noPlayers; i++) {
            // Deck to the left and right of each player
            CardDeck left = decks.get(i);
            CardDeck right = decks.get((i+1) % noPlayers);
            players.add(new PlayerThread(i+1, left, right, players, record));
        }
        return players;
    }

    public static void dealOutCards(ArrayList<PlayerThread> players, ArrayList<CardDeck> decks, ArrayList<Card> pack) {
        // Dealing the cards to players
        for (int i=0; i<4; i++) {
            // i represents the card being dealt to each player, j represents the player the card is being dealt to
            for (int j=0; j<players.size(); j++) {
                // Cards are added sequentially to the player's hands in a round-robin fashion
                players.get(j).addCard(pack.get(i*players.size() + j));
            }
        }

        // Dealing cards to decks
        for (int i=0; i<4; i++) {
            for (int j=0; j<players.size(); j++) {
                decks.get(j).addCard(pack.get(i*players.size() + j + 4*players.size()));
            }
        }
    }

    public static void startPlaying(ArrayList<PlayerThread> players) {
        for (PlayerThread playerThread : players) {
            // Creates and starts a thread for each player
            Thread thread = new Thread(playerThread);
            thread.start();
        }
    }
}