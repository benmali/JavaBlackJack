package blackjackgame;

public class Dealer extends CardPlayer {
    int bank = 5000;
    boolean split = false;
    private Hand hand;
    private String name;
    Deck deck;

    public Dealer(Deck deck) {
        this.name = "Dealer";
        this.deck = deck;
    }

    public Hand getHand() {
        return hand;
    }
    public int getHandValue(){
        return  this.hand.getValue();
    }
    public Card getFaceUpCard(){ // needs to get card from dealer
        Card card = new Card("Diamonds",13);
        return card;
    }



    public int open_cards(Deck deck) {
        while (!this.shouldStop()) {
            this.hand.getCard(deck);
        }
        return this.hand.value;
    }

    @Override
    protected Hand getActiveHand() {
        return this.getHand();
    }

    @Override
    public boolean shouldStop() {
        return this.hand.getValue() >= 17;
    }
    public boolean isBurned(){
        return this.hand.getValue() > 21;
    }
}



