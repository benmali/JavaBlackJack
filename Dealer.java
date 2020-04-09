package com.example.blackjackv2;

import java.util.ArrayList;

public class Dealer extends CardPlayer {
    private Hand hand;
    public Deck deck;


    public Dealer(Deck deck) {
        this.deck = deck;
        this.hand = new Hand();
    }


    public Hand getHand() {
        return hand;
    }
    public int getHandValue(){
        return  this.hand.getValue();
    }
    public ArrayList <Card> getCards(){
        return hand.getCards();
    }
    public Card getFaceUpCard(){ // needs to get card from dealer

        return getActiveHand().getCards().get(0);
    }
    public void resetHand(){
        Hand hand = new Hand();
        this.hand = hand;
    }

    public int openCards(Deck deck) {
        while (!this.shouldStop()) {
            this.hand.getCard(deck);
        }
        return this.hand.getValue();
    }

    @Override
    public Hand getActiveHand() {
        return this.getHand();
    }
    @Override
    public boolean shouldStop() {
        return this.hand.getValue() >= 17;
    }

    @Override
    protected void hit() {
        this.hand.getCard(deck);
    }

    public boolean isBurned(){
        return this.hand.getValue() > 21;
    }

    public boolean hasBlackJack(){
        return (this.hand.getCards().size() == 2 && this.hand.getValue() ==21);
    }






}



