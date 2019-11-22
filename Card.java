package blackjackgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private int suit;
    private int value;


    public Card(int suit, int value)
    {
        this.suit = suit;
        this.value = value;
    }

    public int getCount(){

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


    public int getValue()
    {
        if(this.value >10){
            return 10;
        }
        return this.value;
    }
    public int getValueDisplay(){
        return this.value;
    }
    public Bitmap getCardImage(Resources res){
        String card = getCardText();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + card, null, "com.example.blackjack"));

    }


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
        String card = valueText + "_of_"+ suit;
        return card;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.suit);
        dest.writeInt(this.value);
    }

    protected Card(Parcel in) {
        this.suit = in.readInt();
        this.value = in.readInt();
    }



    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}