package com.example.blackjackv2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hand {
    private int  value = 0;
    private HashMap<Integer, Integer> valueMap = new HashMap<>();
    private ArrayList<Card> cards = new ArrayList<>();
    private int  bet;
    private boolean standFlag = false;
    private boolean burnFlag = false;
    private boolean blackJack = false;
    private boolean softFlag = false;

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
     return softFlag;
    }


    public Bitmap getHandValueIcon(Resources res){
    return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" +value , null, "com.example.blackjackv2"));
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

    public boolean isStandFlag() {
        return standFlag;
    }

    public void setStandFlag() {
        this.standFlag = true;
    }

    public boolean isBurnFlag() {
        return burnFlag;
    }

    public void setBurnFlag() {
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
            return cards.get(0).getValue() == cards.get(1).getValue();
            }
        return false;
    }


    public Card getCard(Deck deck) {
        Card drawnCard = deck.draw();
        cards.add(drawnCard);
        setValue(calculateHandValue());
        return drawnCard;
    }

    public ArrayList<Card> getCards(){
        return this.cards;

    }


    private int calculateHandValue() {

        //maps size of hand to hand value
        int handValue; // variable for summing all values in hand
        ArrayList<Integer> valueList = new ArrayList<>();
        int handSize = cards.size();
        //calculating first dealt hand,else, get value from hash map and add it
        if (handSize == 0){
            return 0;
        }
        if(handSize ==1){
            return cards.get(0).getValue();
        }
        if (handSize == 2) {
            for (Card card : cards) // extracting card values and adds them to a list
            {
                if (card.getValue() >= 10) // adds royalty and 10 to value list
                {
                    valueList.add(10);
                    continue;
                }
                if (card.getValue() == 1) {
                    valueList.add(11);
                    softFlag = true;

                } else // adds regular numbers not including aces
                {
                    valueList.add(card.getValue());

                }
            }
            handValue = sum(valueList); // getting the sum of the hand, aces counting as 11
            if (handValue == 21) {
                // BlackJack case
                setBlackJack();
                setStandFlag();
                valueMap.put(2, 21);
                return 21;
            }
            return handValue;
        }

        else {
            // getting size of hand, -1 to get the key from hash map

            //add last card in hand to hand value
            handValue = valueMap.get(handSize - 1) + cards.get(cards.size() - 1).getValue();

            if (handValue == 21) {
                setStandFlag();
                return 21;
            }

            if (handValue > 21 && softFlag) {
                handValue -= 10;
                valueMap.put(handSize, handValue);
                softFlag = false;
                return handValue;
            }

            if (handValue > 21) {
                setBurnFlag();
                return handValue;
            }



            else {
                valueMap.put(handSize, handValue);
                return handValue;

            }
        }
    }

    private int sum(ArrayList <Integer> list) {
        int value = 0;
        for (int i : list) {
            value += i;
        }
        return value;
    }
    public boolean isAlive(){
        return !isBurnFlag();
    }

    public void testGetSplitCards(){ // test method to fix split
        Card card1 = new Card(1,2);
        Card card2 = new Card(2,2);
        this.cards = new ArrayList<>();
        this.cards.add(card1);
        this.cards.add(card2);
    }


}





