package com.example.blackjackv2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Card  {
    private int suit;
    private int value;


    public Card(int suit, int value)
    {
        this.suit = suit;
        this.value = value;
    }
    //copy constructor
    public Card(Card card){
        this.suit = card.getSuit();
        this.value = card.value;
    }

    public int getCountValue(){
        if(value>=2 && value<=6){
            return 1;
        }
        if(value>=7&& value <=9){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int getSuit()
    {
        return this.suit;
    }

    //returns numerical value of card
    public int getValue(){

        if(this.value >10){
            return 10;
        }
        return this.value;
    }

    //method to return display value of card (jack,queen,king)
    public int getValueDisplay(){
        return this.value;
    }

    public Bitmap getCardImage(Resources res){
        String card = getCardText();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + card, null, "com.example.blackjackv2"));

    }

    //method to fetch card images
    private String getCardText(){
         int value = this.getValueDisplay();
         int suitCode = this.suit;

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
        return valueText + "_of_"+ suit;
    }


}