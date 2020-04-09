package com.example.blackjackv2;

import java.sql.SQLException;
import java.util.ArrayList;


public class Gambler extends CardPlayer  {

    private String name;
    private int bank;
    private ArrayList<Hand> hands = new ArrayList<>();
    private Deck deck;

    //Human player
    public Gambler(String name, Deck deck, int bank) {
        this.name = name;
        this.deck = deck;
        this.bank = bank;
        //query DB for money table
        Hand hand = new Hand();
        this.hands.add(hand);
    }

// Computer Player
    public Gambler(Deck deck){
        this.deck = deck;
        Hand hand = new Hand();
        this.hands.add(hand);
        this.bank = 50000;
    }

    public String getName(){
        return this.name;
    }


    @Override
    public Hand getActiveHand() {
        //not burnt and not on stand  - hand 1 is active
        if (!this.hands.get(0).isBurnFlag() && !this.hands.get(0).isStandFlag()) {
            return hands.get(0);
        }
        else {// stand or burn of hand 1, check for another hand
            if (!this.hands.get(1).isBurnFlag() && !this.hands.get(1).isStandFlag()) {// hand 2 is active
                return hands.get(1); //return hand 2
            }
            //no hands are active, return null
            return null;
        }
        }

    public void setBet(int bet){
        Hand hand = getActiveHand();
        hand.setBet(bet);
        this.bank -=bet;
    }

    public void splitCards() {

        if(this.hands.size() == 1 && this.canSplit()){ // cant split more than once
            int startBet = this.hands.get(0).getBet();
            ArrayList<Card> startCards = this.hands.get(0).getCards();
            ArrayList<Card> hand1Cards = new ArrayList<>();
            ArrayList<Card> hand2Cards = new ArrayList<>();
            //creating new hands using the old cards with copy constructors
            hand1Cards.add(new Card(startCards.get(0)));
            hand2Cards.add(new Card(startCards.get(1)));
            Hand hand1 = new Hand(startBet, hand1Cards);
            Hand hand2 = new Hand(startBet, hand2Cards);
            this.hands = new ArrayList<>();
            this.hands.add(hand1);
            this.hands.add(hand2);
            for (int i = 0; i <= 1; i++) {
                this.hands.get(i).getCard(this.deck);
                this.hands.get(i).setBet(startBet);
            }
            this.bank -= startBet;
        }
    }
    @Override
    public boolean shouldStop() {
        return this.getActiveHand() == null;
    }

    @Override
    protected void hit() {
        Hand hand = this.getActiveHand();
        if (hand != null) {
            hand.getCard(this.deck);
            if (hand.getValue() > 21){
                hand.setBurnFlag();
            }
            if(hand.getValue() == 21){
                hand.setStandFlag();
            }
        }

    }

    @Override
    public int getActiveHandValue() {
        return super.getActiveHandValue();
    }


    public void autoPlay(Dealer dealer) {
        while(!this.shouldStop()){
            Matrix tipsMatrix = new Matrix(this, dealer); // decision matrix
            int actionCode = tipsMatrix.decisionMatrix();

            switch (actionCode) {
                case 1:
                    this.hit();
                    break;

                case 2:
                    this.stand();
                    break;

                case 3:
                    this.doubleDown();
                    break;

                case 4:
                    this.splitCards();
                    break;

            }

        }
    }

    public void doubleDown() {
        Hand activeHand = this.getActiveHand();
        if(activeHand!=null){
            if (activeHand.getCards().size() == 2){
                this.bank -= activeHand.getBet();
                activeHand.setBet(activeHand.getBet()*2);
                this.hit();
                this.stand();
    }}}

    public void stand() {
            Hand activeHand = this.getActiveHand();
            if(activeHand!=null){
                activeHand.setStandFlag();
             }
        }

    public void resetHands(){
         ArrayList<Hand> hands = new ArrayList<>();
         this.hands = hands;
         hands.add(new Hand());
    }

    public void decideState(Dealer dealer,DB db){ // function decides which hands win, lose or ties for main
        ArrayList <Hand> livingHands = getLivingHands();
        int dealerHandValue = dealer.getHandValue();
        if(livingHands.size() != 0){ // condition that the player did not burn, el
            if (dealer.isBurned()){ // dealer is burned, all living hands win
                for (Hand hand : livingHands){
                    if (hand.isBlackJack()){ //hand is blackjack
                        this.winBlackHand(hand);
                    }
                    else{
                        this.winHand(hand);
                    }
                    this.registerStats(db,1);//win
                }

            }

            else {// dealer isn't burned, player has living hands
                for (Hand hand : livingHands){
                    if (dealer.getHand().getValue() < hand.getValue()){ // winning scenario

                        if(hand.isBlackJack()){
                           this.winBlackHand(hand);

                        }
                        else{
                            this.winHand(hand);

                        }
                        this.registerStats(db,1);//win
                        continue;
                    }

                    if (dealerHandValue == hand.getValue()){ //tie scenarios
                        if (dealer.getHand().isBlackJack()&& !hand.isBlackJack()){ // hand loses if dealer has black jack
                             this.registerStats(db,2);// lose 21 to BlackJack
                        }
                        else {
                           this.tieHand(hand);
                           this.registerStats(db,3);
                        }
                    }

                }

            }
        }
        //registering burnt hands
        //simplifies condition of dealer burning as no action is needed when player loses other than
        //registering the lose
        if(this.hands.size()>livingHands.size()){
            int k = this.hands.size() - livingHands.size();
            for(int i =0; i<k;i++){
                this.registerStats(db,2);
            }
        }
    }

    public ArrayList <Hand> getLivingHands(){
        ArrayList<Hand> livingHands = new ArrayList<>();
        for (Hand hand : this.hands){
            if (hand.isAlive()){
                livingHands.add(hand);
            }
        }
        return livingHands;
    }

    public boolean canSplit(){
        return this.hands.get(0).canSplit();
    }

    public void  winHand(Hand hand){
        this.bank += hand.getBet()*2;
    }

    public void winBlackHand(Hand hand){
       this.bank+= hand.getBet()*2.5;
    }
    public void tieHand(Hand hand){
        this.bank+=hand.getBet();
    }

    public int getBank(){
        return this.bank;
    }



    private void registerStats(DB db,int actionCode){

        try{
            db.open();
            ArrayList<int[]> balance = db.getPlayerStats();
            int id = balance.get(0)[0];
            int wins = balance.get(0)[1];
            int loses = balance.get(0)[2];
            int ties = balance.get(0)[3];
            switch (actionCode) {
                case 1:
                    db.updatePlayerStats(id,wins+1,loses,ties);//1 win
                    db.close();
                    break;
                case 2:
                    db.updatePlayerStats(id,wins,loses+1,ties);//2 lose
                    db.close();
                    break;
                case 3:
                    db.updatePlayerStats(id,wins,loses,ties+1);//3 tie
                    db.close();
                    break;
            }
        }

        catch (SQLException e){
            e.printStackTrace();
        }

    }



}

