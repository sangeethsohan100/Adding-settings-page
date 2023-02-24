/*
 *
 * @author Christian Wood and Jacob Beeson
 *
 */

public class Card {
    /*Attributes:
    ---------------
    */
    //the value of the card
    private final Integer cardValue;

    /*Constructor:
    ---------------
    */
    public Card(Integer cardValue) {
        this.cardValue = cardValue;
    }

    /*Getters and Setters:
    ---------------
    */
    //returns the card's value
    public Integer getCardValue() {
        return cardValue;
    }
}
