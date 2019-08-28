package blackjackgame;

import java.util.ArrayList;
import java.util.List;


public class Gambler extends CardPlayer{
    String name;
    public int bank;
    private ArrayList<Hand> hands = new ArrayList<>();
    private Deck deck;

    public Gambler(String name, Deck deck, int bank) {
        this.name = name;
        this.deck = deck;
        this.bank = bank;
    }
    public String getName(){
        return this.name;
    }

    @Override
    protected Hand getActiveHand() {
        if (this.hands.size() == 0) {
            return null;
        }

        if (this.hands.get(0).burnFlag || this.hands.get(0).standFlag) {
            if (this.hands.size() == 2 && (this.hands.get(1).burnFlag || this.hands.get(1).standFlag)) {
                return hands.get(1);
            } else {
                return null;
            }
        } else {
            return hands.get(0);
        }
    }


    public void splitCards() {
        int startBet = this.hands.get(0).getBet();
        List<Card> startCards = this.hands.get(0).cards;

        List<Card> hand1Cards = new ArrayList<>();
        hand1Cards.add(startCards.get(0));

        List<Card> hand2Cards = new ArrayList<>();
        hand1Cards.add(startCards.get(1));

        Hand hand1 = new Hand(startBet, hand1Cards);
        Hand hand2 = new Hand(startBet, hand2Cards);

        this.hands = new ArrayList<>();
        this.hands.add(hand1);
        this.hands.add(hand2);

        for (int i = 0; i <= 1; i++) {
            this.hands.get(i).getCard(this.deck);
            this.hands.get(i).bet = startBet / 2;
        }
    }
    @Override
    public boolean shouldStop() {
        return this.getActiveHand() == null;
    }
    public void hit() {
        Hand hand = this.getActiveHand();
        if (hand != null) {
            hand.getCard(this.deck);
        }
    }
    public void playerDouble() {
        Hand activeHand = this.getActiveHand();
        this.bank -= activeHand.getBet();
        activeHand.multiplyBet();
        activeHand.getCard(this.deck);
        this.stand();
    }
    public void stand() {
        Hand hand = this.getActiveHand();
        if (hand != null) {
            hand.setstandFlag();
        }
    }
    public void decideState(Dealer dealer){ // function decides which hands win, lose or ties for main
        ArrayList <Hand> livingHands = getLivingHands();
        int dealerHandValue = dealer.getHandValue();
        if (dealer.isBurned()){
            for (Hand hand : livingHands) //
            {
                if (hand.isBlackJack()) //hand is blackjack
                {
                    if (dealer.getHand().isBlackJack()) {
                        hand.tie(this);
                    }
                    else {
                        hand.winBlack(this);
                    }
                }
            }}
            else {
                for (Hand hand : livingHands){
                    if (dealer.getHand().getValue() < hand.getValue()) // winning scenario
                    {
                    hand.win(this);
                    continue;
                    }
                    if (dealerHandValue > hand.getValue()) // losing scenario
                    {
                        hand.setBet(0);
                        System.out.println(this.name + " lost to dealer");
                    }

                    if (dealerHandValue == hand.getValue()) //tie scenarios
                    {
                        if (dealer.getHand().isBlackJack()&& !hand.isBlackJack()) // hand loses if dealer has black jack
                        {
                            hand.setBet(0);

                        } else {
                            hand.tie(this);
                        }
                    }
                }

            }}
    private ArrayList <Hand> getLivingHands(){
        ArrayList<Hand> livingHands = new ArrayList<>();
        for (Hand hand : this.hands){
            if (hand.isAlive()){
                livingHands.add(hand);
            }
        }
        return livingHands;
    }

    public void  incrementBank(int value){
        this.bank += value;
    }

    public void bet(int value,Hand hand){
        hand.setBet(value);
        this.bank -= value;
    }
    public void win(){

        System.out.println(this.name + " Won");
    }

    }

