import java.util.ArrayList;

public class CardDeck {

    private ArrayList<Card> deck = new ArrayList<Card>();
    public int deckNumber;
    public ArrayList<Player> players = new ArrayList<>();

    public CardDeck(int id) {

        this.deckNumber = id;
    }

    public void addCard(Card card) {
        synchronized (deck) {
            deck.add(card);
        }
    }

    public Card[] getDeck() {
        Card[] d = new Card[this.deck.size()];
        for (Integer n = 0; n < d.length; n++) {
            d[n] = this.deck.get(n);
        }
        return d;
    }

    public Card drawCard() {

        return deck.remove(0);

    }

    public Card getTopCard() {
        Card top = deck.get(0);
        deck.remove(top);
        return top;
    }

    // private Card getBottomCard() {
    // Card bottom = deck.get(deck.size() - 1);
    // deck.remove(bottom);
    // return bottom;
    // }

    public ArrayList<Card> getDeckSize() {
        return deck;
    }

    public void setDeckNumber(int deckNo) {
        this.deckNumber = deckNo;
    }

    public int getDeckNumber() {
        return this.deckNumber;

    }

}
