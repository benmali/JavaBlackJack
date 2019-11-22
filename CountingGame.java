package blackjackgame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public  class CountingGame implements Parcelable {
    public Deck deck = new Deck();
    private ArrayList <Gambler> gamblers = new ArrayList<>();
    public Dealer dealer = new Dealer(deck);
    private int runningCount = 0;
    public ArrayList<Gambler> activePlayers;


    protected CountingGame(Parcel in) {
        dealer = (Dealer) in.readValue(Dealer.class.getClassLoader());
        if (in.readByte() == 0x01) {
            activePlayers = new ArrayList<Gambler>();
            in.readList(activePlayers, Gambler.class.getClassLoader());
        } else {
            activePlayers = null;
        }
    }
    public CountingGame(Deck deck, Dealer dealer, ArrayList<Gambler> gamblers){
        this.deck = deck;
        this.dealer = dealer;
        this.gamblers = gamblers;
    }
    public CountingGame(int numOfDecks, int numOfBots){
        this.deck = new Deck(numOfDecks);
        this.createPlayers(numOfBots);
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
    public void createPlayers(int numOfBots){
        Gambler player = new Gambler("Ben",this.deck,5000);
        this.gamblers.add(player);
        for(int i =0; i<numOfBots; i++){
            this.gamblers.add(new Gambler(this.deck)); // add a new bot
        }
    }
    public void initiateGame(){
        for(int i=0; i <= 1; i++) {
            for (CardPlayer player : this.gamblers){ // deal 2 cards to initialize the game
                player.getActiveHand().getCardFromDeck(this.deck);
                this.runningCount++;
            }
            this.dealer.getHand().getCardFromDeck(this.deck);
            this.runningCount++;
        }
    }

    public void botsPlay(){
        for(Gambler player: this.gamblers){
            if(player.getName() == null){
                player.autoPlay(this.dealer);
            }
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
        for (Gambler player :this.gamblers){
            player.resetHands();
            dealer.resetHand();
            this.runningCount = 0;
        }
    }
    public int getRunningCount(){
        return this.runningCount;
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


    public static final Parcelable.Creator<CountingGame> CREATOR = new Parcelable.Creator<CountingGame>() {
        @Override
        public CountingGame createFromParcel(Parcel in) {
            return new CountingGame(in);
        }

        @Override
        public CountingGame[] newArray(int size) {
            return new CountingGame[size];
        }
    };

}
