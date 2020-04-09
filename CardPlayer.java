package com.example.blackjackv2;

public abstract class CardPlayer {
    abstract protected Hand getActiveHand();//get current hand
    abstract public boolean shouldStop();//method for checking if any hands are left to play
    abstract protected void hit();//get current hand

    public int getActiveHandValue(){
        Hand activeHand = this.getActiveHand();
        if (activeHand != null) {
            return activeHand.getValue();
        }
        return Integer.parseInt(null);
    }



}
