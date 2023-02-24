package main;

public class Card {

    private int val; // The value of the card

    public Card(int val) throws Exception {
        if (val >= 0) {
            this.val = val;
        } else {
            throw new Exception("Card value cannot be a negative number");
        }
    }

    public int getValue() {
        // Returns the card's value
        return val;
    }
}
