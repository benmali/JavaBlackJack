package blackjackgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Hand implements Parcelable {
    int value = 0;
    public ArrayList<Card> cards = new ArrayList<>();
    int bet;
    boolean standFlag = false;
    boolean burnFlag = false;
    private boolean blackJack = false;
    private int aceCounter = 0;
    private boolean isSoft = false;

    Hand(){ // constructor for initializing players
        this.bet = 0;
    }

    Hand(int bet, ArrayList<Card> cards){ // constructor for split
        this.bet = bet;
        this.cards = cards;
    }

    public int getValue() {
        return this.value;
    }
    public boolean checkIsSoft(){
     return this.isSoft;
    }
    public void getSplitCards(){ // test method to fix split
        Card card1 = new Card(1,2);
        Card card2 = new Card(2,2);
        this.cards = new ArrayList<>();
        this.cards.add(card1);
        this.cards.add(card2);
    }

    public Bitmap getHandValueIcon(Resources res){
    return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" +value , null, "com.example.blackjack"));
    }

    public ArrayList<Bitmap> getImages(Resources res){
        ArrayList<Bitmap> cardImages = new ArrayList<>();
        for(Card card:this.cards){
            cardImages.add(card.getCardImage(res));
        }
        return cardImages;
    }

    public void setValue(int value) {
        this.value = value;
    }


    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean isstandFlag() {
        return standFlag;
    }

    public void setstandFlag() {
        this.standFlag = true;
    }

    public boolean isburnFlag() {
        return burnFlag;
    }

    public void setburnFlag() {
        this.burnFlag = true;
    }

    public boolean isBlackJack() {
        return blackJack;
    }

    public void setBlackJack() {
        this.blackJack = true;
    }

    public boolean canSplit() {
        if (cards.size() == 2){
            if(cards.get(0).getValue() == cards.get(1).getValue()){
                return true;
            }
            if( cards.get(0).getValue() >= 10 && cards.get(1).getValue() >= 10){
                return true;
            }
        }
        return false;
    }

    public void win(Gambler player) {
        this.bet *= 2;
        player.incrementBank(this.bet);
        this.setBet(0);
    }


    public void winBlack(Gambler player) {
        this.bet *= 2.5;
        player.incrementBank(this.bet);
        this.bet = 0;
    }

    public void tie(Gambler player) {
        player.incrementBank(this.bet);
        this.bet = 0;
    }

    public Card getCard(Deck deck) {
        Card drawnCard = deck.draw();
        cards.add(drawnCard);
        if (drawnCard.getValue() == 1) {
            this.aceCounter += 1;
        }
        this.setValue(this.calculateHandValue());
        return drawnCard;
    }
    public Card getCardFromDeck(Deck deck){ // method for card counting
        Card drawnCard = deck.drawFromDeck();
        cards.add(drawnCard);
        if (drawnCard.getValue() == 1) {
            this.aceCounter += 1;
        }
        this.setValue(this.calculateHandValue());
        return drawnCard;
    }


    public ArrayList<Card> getCards(){
        return this.cards;

    }


    private int calculateHandValue() //working
    {
        int handValue; // variable for summing all values in hand
        ArrayList<Integer> valueList = new ArrayList<>();
        int numberOfAces = this.aceCounter;
        for (Card card : this.cards) // extracting all card values and adds them to a list
        {
            if (card.getValue() >= 10) // adds royalty and 10 to value list
            {
                valueList.add(10);
                continue;
            }
            if(card.getValue() == 1) {
                valueList.add(11);
                isSoft = true;

            }
            else // adds regular numbers not including aces
            {
                valueList.add(card.getValue());

            }
        }
        handValue = sum(valueList); // getting the sum of the hand, aces counting as 11
        if (handValue > 21){
            while(numberOfAces > 0 && handValue > 21){ // while aces could be deducted to 1
                numberOfAces -= 1; // decrease number of aces
                handValue -= 10; // decrease value by 10 for each ace if handValue is greater than 21
                isSoft = false;
            }
        }
        if (handValue == 21) {
            if (this.cards.size() == 2) // BlackJack case
            {
                this.setBlackJack();
            }
            this.setstandFlag();
            return handValue;
        }

        if (handValue > 21) {
            this.setburnFlag();
            return handValue;
        }
        return handValue;
    }


    private int sum(ArrayList <Integer> list) {
        int value = 0;
        for (int i : list) {
            value += i;
        }
        return value;
    }
    public boolean isAlive(){
        return !isburnFlag();
    }

    public void multiplyBet(){
        this.bet *= 2;
    }
    public void multiplyBetBlack(){
        this.bet *= 2.5;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value);
        dest.writeTypedList(this.cards);
        dest.writeInt(this.bet);
        dest.writeByte(this.standFlag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.burnFlag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.blackJack ? (byte) 1 : (byte) 0);
        dest.writeInt(this.aceCounter);
        dest.writeByte(this.isSoft ? (byte) 1 : (byte) 0);
    }

    protected Hand(Parcel in) {
        this.value = in.readInt();
        this.cards = in.createTypedArrayList(Card.CREATOR);
        this.bet = in.readInt();
        this.standFlag = in.readByte() != 0;
        this.burnFlag = in.readByte() != 0;
        this.blackJack = in.readByte() != 0;
        this.aceCounter = in.readInt();
        this.isSoft = in.readByte() != 0;
    }

    public static final Creator<Hand> CREATOR = new Creator<Hand>() {
        @Override
        public Hand createFromParcel(Parcel source) {
            return new Hand(source);
        }

        @Override
        public Hand[] newArray(int size) {
            return new Hand[size];
        }
    };
}


