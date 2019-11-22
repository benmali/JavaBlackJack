package blackjackgame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Dealer extends CardPlayer implements Parcelable {
    boolean split = false;
    private Hand hand;
    private String name;
    Deck deck;

    public Dealer(Deck deck) {
        this.name = "Dealer";
        this.deck = deck;
        this.hand = new Hand();
    }
    public Hand getHand() {
        return hand;
    }
    public int getHandValue(){
        return  this.hand.getValue();
    }
    public ArrayList<Card> getCards(){
        return hand.getCards();
    }
    public Card getFaceUpCard(){ // needs to get card from dealer

        return getActiveHand().getCards().get(0);
    }
    public void resetHand(){
        Hand hand = new Hand();
        this.hand = hand;

    }

    public int openCardsCounting(Deck deck){
        while (!this.shouldStop()) {
            this.hand.getCardFromDeck(deck);
        }
        return this.hand.getValue();
    }


    public int openCards(Deck deck) {
        while (!this.shouldStop()) {
            this.hand.getCard(deck);
        }
        return this.hand.value;
    }
    public void getCard(Deck deck){
        this.hand.getCard(deck);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeByte(this.split ? (byte) 1 : (byte) 0);
        dest.writeParcelable((Parcelable) this.hand, flags);
        dest.writeString(this.name);
    }

    protected Dealer(Parcel in) {

        this.split = in.readByte() != 0;
        this.hand = in.readParcelable(Hand.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Dealer> CREATOR = new Parcelable.Creator<Dealer>() {
        @Override
        public Dealer createFromParcel(Parcel source) {
            return new Dealer(source);
        }

        @Override
        public Dealer[] newArray(int size) {
            return new Dealer[size];
        }
    };
}



