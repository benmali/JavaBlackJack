package blackjackgame;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

//Class has to be parcelable in order to be transferred to next activity
public class Gambler extends CardPlayer implements Parcelable {
    String name;
    public int bank;
    private ArrayList<Hand> hands = new ArrayList<>();
    private Deck deck;
    private  boolean splitFlag;

    public Gambler(String name, Deck deck, int bank) {
        this.name = name;
        this.deck = deck;
        this.bank = bank;
        Hand hand = new Hand();
        this.hands.add(hand);
        this.splitFlag = false;
    }
// Computer Player
    public Gambler(Deck deck){
        this.deck = deck;
        Hand hand = new Hand();
        this.hands.add(hand);
        this.bank = 5000;
        this.splitFlag = false;
    }
    public String getName(){
        return this.name;
    }

    private void setSplitFlag() {
        this.splitFlag = true;
    }


    @Override
    public Hand getActiveHand() {
        if (!this.hands.get(0).burnFlag && !this.hands.get(0).standFlag) { //hand 1 is active
            return hands.get(0);
        }
        else {
            if (this.hands.size() == 2 && (!this.hands.get(1).burnFlag && !this.hands.get(1).standFlag)) {// hand 2 is active
                return hands.get(1); //return hand 2
            }
        }
            return null;
        }


    public void setBet(int bet){
        Hand hand = getActiveHand();
        hand.setBet(bet);
        this.bank -=bet;
    }

    public int getPlayerBet(){
        return getActiveHand().getBet();
    }

    public void splitCards() {
        int startBet = this.hands.get(0).getBet();
        if(this.hands.size() == 1 && this.canSplit()){ // cant split more than once
        setSplitFlag();
        ArrayList<Card> startCards = this.hands.get(0).cards;
        ArrayList<Card> hand1Cards = new ArrayList<>();
        hand1Cards.add(startCards.get(0));
        ArrayList<Card> hand2Cards = new ArrayList<>();
        hand2Cards.add(startCards.get(1));
        Hand hand1 = new Hand(startBet, hand1Cards);
        Hand hand2 = new Hand(startBet, hand2Cards);
        this.hands = new ArrayList<>();
        this.hands.add(hand1);
        this.hands.add(hand2);
        for (int i = 0; i <= 1; i++) {
            this.hands.get(i).getCard(this.deck);
            this.hands.get(i).bet = startBet;
        }
        this.bank -= startBet;
    }}
    @Override
    public boolean shouldStop() {
        return this.getActiveHand() == null;
    }


    public void autoPlay(Dealer dealer) {
        while(!this.shouldStop()){
            Matrix tipsMatrix = new Matrix(this, dealer); // decision matrix
            int actionCode = tipsMatrix.decisionMatrix();

            switch (actionCode) {
                case 1:
                    this.hitCounting();
                    break;

                case 2:
                    this.stand(this.getActiveHand());
                    break;

                case 3:
                    this.playerDoubleCounting();
                    break;

                case 4:
                    this.splitCounting();
                    break;

            }

        }
    }

    public void hitCounting(){
        Hand hand = this.getActiveHand();
        if (hand != null) {
            hand.getCardFromDeck(this.deck);
            if (hand.getValue() > 21){
                hand.setburnFlag();
            }
            if(hand.getValue() == 21){
                hand.setstandFlag();
            }
        }
    }
    public void splitCounting() {
        int startBet = this.hands.get(0).getBet();
        if(this.hands.size() == 1 && this.canSplit()){ // cant split more than once
            setSplitFlag();
            ArrayList<Card> startCards = this.hands.get(0).cards;
            ArrayList<Card> hand1Cards = new ArrayList<>();
            hand1Cards.add(startCards.get(0));
            ArrayList<Card> hand2Cards = new ArrayList<>();
            hand2Cards.add(startCards.get(1));
            Hand hand1 = new Hand(startBet, hand1Cards);
            Hand hand2 = new Hand(startBet, hand2Cards);
            this.hands = new ArrayList<>();
            this.hands.add(hand1);
            this.hands.add(hand2);
            for (int i = 0; i <= 1; i++) {
                this.hands.get(i).getCardFromDeck(this.deck);
                this.hands.get(i).bet = startBet;
            }
            this.bank -= startBet;
        }}
    public void playerDoubleCounting() {
        Hand activeHand = this.getActiveHand();
        if(activeHand!=null){
        if (activeHand.getCards().size() == 2){
            this.bank -= activeHand.getBet();
            activeHand.multiplyBet();
            this.hitCounting();
            this.stand(activeHand);
        }}}

    public void hit() {
        Hand hand = this.getActiveHand();
        if (hand != null) {
            hand.getCard(this.deck);
            if (hand.getValue() > 21){
                hand.setburnFlag();
            }
            if(hand.getValue() == 21){
                hand.setstandFlag();
            }
        }
    }

    public void playerDouble() {
        Hand activeHand = this.getActiveHand();
        if(activeHand!=null){
            if (activeHand.getCards().size() == 2){
                this.bank -= activeHand.getBet();
                activeHand.multiplyBet();
                this.hit();
                this.stand(activeHand);
    }}}
    public void stand(Hand hand) {
            hand.setstandFlag();
        }


    public ArrayList<Hand> getHands() {
        return hands;
    }
    public void resetHands(){
         ArrayList<Hand> hands = new ArrayList<>();
         this.hands = hands;
         Hand hand = new Hand();
         hands.add(hand);
    }

    public void decideState(Dealer dealer,Statistics stats){ // function decides which hands win, lose or ties for main
        ArrayList <Hand> livingHands = getLivingHands();
        int dealerHandValue = dealer.getHandValue();
        if(livingHands.size() != 0){ // condition that the player did not burn, el
            if (dealer.isBurned()){ // dealer is burned, all living hands win
            for (Hand hand : livingHands)
            {
                if (hand.isBlackJack()) //hand is blackjack
                {
                    hand.winBlack(this);
                    stats.addGameResult(true);

                }
                else{
                    hand.win(this);
                    stats.addGameResult(true);
                }
            }}
            else {
                for (Hand hand : livingHands){
                    if (dealer.getHand().getValue() < hand.getValue()) // winning scenario
                    {
                    hand.win(this);
                    stats.addGameResult(true);
                    continue;
                    }
                    if (dealerHandValue > hand.getValue()) // losing scenario
                    {
                        hand.setBet(0);
                        System.out.println(this.name + " lost to dealer");
                        stats.addGameResult(false);
                    }

                    if (dealerHandValue == hand.getValue()) //tie scenarios
                    {
                        if (dealer.getHand().isBlackJack()&& !hand.isBlackJack()) // hand loses if dealer has black jack
                        {
                            hand.setBet(0);
                            stats.addGameResult(false);

                        } else {
                            hand.tie(this);
                            stats.addGameResult(false);
                        }
                    }
                }

            }
        }
        else{
            stats.addGameResult(false);

        }}
    public ArrayList <Hand> getLivingHands(){
        ArrayList<Hand> livingHands = new ArrayList<>();
        for (Hand hand : this.hands){
            if (hand.isAlive()){
                livingHands.add(hand);
            }
        }
        return livingHands;
    }

    public boolean checkHandsToPlay(){
        for(Hand hand: hands){
           if(!hand.isstandFlag()&& !hand.isburnFlag()){
               return true;
           }
        }
        return false;
    }
    public boolean canSplit(){
        return this.hands.get(0).canSplit();
    }

    public void  incrementBank(int value){
        this.bank += value;
    }
    public void win(Hand hand){
        int bet = hand.getBet();
        this.bank += bet;
    }
    public int getBank(){
        return this.bank;
    }

    // methods for card counting mode

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.bank);
        dest.writeTypedList(this.hands);
    }

    protected Gambler(Parcel in) {
        this.name = in.readString();
        this.bank = in.readInt();
        this.hands = in.createTypedArrayList(Hand.CREATOR);
        this.deck = new Deck(); //have to parse deck object but deck isn't parcelable
    }

    public static final Parcelable.Creator<Gambler> CREATOR = new Parcelable.Creator<Gambler>() {
        @Override
        public Gambler createFromParcel(Parcel source) {
            return new Gambler(source);
        }

        @Override
        public Gambler[] newArray(int size) {
            return new Gambler[size];
        }
    };
}

