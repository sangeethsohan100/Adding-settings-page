public class Card {
    // Thread safe

    public int value;

    public Card(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "Card:" + value;
    }

}
