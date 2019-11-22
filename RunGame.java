package blackjackgame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public  class RunGame implements Parcelable {
     public Deck deck = new Deck();
     private ArrayList <Gambler> gamblers = new ArrayList<>();
     public Dealer dealer = new Dealer(deck);
     public ArrayList<Gambler> activePlayers;


    public RunGame(ArrayList <Gambler> gamblers){
        this.gamblers = gamblers;

    }
    protected RunGame(Parcel in) {

        dealer = (Dealer) in.readValue(Dealer.class.getClassLoader());
        if (in.readByte() == 0x01) {
            activePlayers = new ArrayList<Gambler>();
            in.readList(activePlayers, Gambler.class.getClassLoader());
        } else {
            activePlayers = null;
        }
    }
    public RunGame(Deck deck, Dealer dealer, ArrayList<Gambler> gamblers){
        this.deck = deck;
        this.dealer = dealer;
        this.gamblers = gamblers;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dealer);
        if (activePlayers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(activePlayers);
        }
    }


    public static final Parcelable.Creator<RunGame> CREATOR = new Parcelable.Creator<RunGame>() {
        @Override
        public RunGame createFromParcel(Parcel in) {
            return new RunGame(in);
        }

        @Override
        public RunGame[] newArray(int size) {
            return new RunGame[size];
        }
    };
    public RunGame(){
        this.createPlayers();
    }
    public ArrayList<Gambler> validateBets(){
        ArrayList<Gambler> activePlayers = new ArrayList<>();
        for (Gambler player :this.gamblers){
            if (player.getPlayerBet() > 0){
                activePlayers.add(player);
            }
        }
        this.activePlayers = activePlayers;
        return activePlayers;
    }
    public void setPlayers(ArrayList<Gambler> gamblers){
        this.gamblers = gamblers;
    }
    public int getDealerHandValue(){
        return dealer.getHandValue();
    }
    public int getDealerFaceUpCardValue(){
        int value = dealer.getFaceUpCard().getValue();
        if(value >=10){
            return 10;
        }
        return value;
    }

    public int getBank(Gambler player){
        return player.getBank();
    }
    public void createPlayers(){
        Gambler p1 = new Gambler("Ben",this.deck,5000);
        //Gambler p2 = new Gambler("Tom",this.deck,5000);
        this.gamblers.add(p1);

    }


    public void initiateGame(){
        for(int i=0; i <= 1; i++) {
            for (CardPlayer player : this.gamblers) // deal 2 cards to initialize the game
            {
                player.getActiveHand().getCard(this.deck);
            }
            this.dealer.getCard(this.deck);
        }
    }
    public void concludeGame(Statistics stats)
    {
        this.dealer.openCards(deck);
        this.gamblers.get(0).decideState(dealer,stats); //method decides if player wins or loses

    }
    public ArrayList<Gambler> getPlayers(){
        return this.gamblers;
    }

    public void resetGame(){
        for ( Gambler player :this.gamblers){
            player.resetHands();
            dealer.resetHand();
        }
    }
    public String getDisplayCardInIdx(Hand hand, int i){

        ArrayList<Card> cards = hand.getCards();
        int suitCode = cards.get(i).getSuit();

        String suit ="";
        String valueText="";
        if (suitCode == 0){
            suit = "hearts";
        }
        if (suitCode == 1){
            suit = "diamonds";
        }
        if (suitCode == 2){
            suit = "clubs";
        }
        if (suitCode == 3){
            suit = "spades";
        }
        int value = cards.get(i).getValueDisplay();
        if (value == 1){
            valueText = "ace";
        }
        if (value == 2){
            valueText = "two";
        }
        if (value == 3){
            valueText = "three";
        }
        if (value == 4){
            valueText = "four";
        }
        if (value == 5){
            valueText = "five";
        }
        if (value == 6){
            valueText = "six";
        }
        if (value == 7){
            valueText = "seven";
        }
        if (value == 8){
            valueText = "eight";
        }
        if (value == 9){
            valueText = "nine";
        }
        if (value == 10){
            valueText = "ten";
        }
        if (value == 11){
            valueText = "jack";
        }
        if (value == 12){
            valueText = "queen";
        }
        if (value == 13){
            valueText = "king";
        }
        String card = valueText + "_of_" + suit;
        return card;
    }

    public String getDisplayCardInIdx(Dealer dealer, int i){

        ArrayList<Card> cards = dealer.getCards();
        int suitCode = cards.get(i).getSuit();

        String suit ="";
        String valueText="";
        if (suitCode == 0){
            suit = "hearts";
        }
        if (suitCode == 1){
            suit = "diamonds";
        }
        if (suitCode == 2){
            suit = "clubs";
        }
        if (suitCode == 3){
            suit = "spades";
        }
        int value = cards.get(i).getValueDisplay();
        if (value == 1){
            valueText = "ace";
        }
        if (value == 2){
            valueText = "two";
        }
        if (value == 3){
            valueText = "three";
        }
        if (value == 4){
            valueText = "four";
        }
        if (value == 5){
            valueText = "five";
        }
        if (value == 6){
            valueText = "six";
        }
        if (value == 7){
            valueText = "seven";
        }
        if (value == 8){
            valueText = "eight";
        }
        if (value == 9){
            valueText = "nine";
        }
        if (value == 10){
            valueText = "ten";
        }
        if (value == 11){
            valueText = "jack";
        }
        if (value == 12){
            valueText = "queen";
        }
        if (value == 13){
            valueText = "king";
        }
        String card = valueText + "_of_"+suit;
        return card;
    }


}
