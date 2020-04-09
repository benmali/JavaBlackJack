package com.example.blackjackv2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class BasicView extends View {


    Graphics graphics = new Graphics();
    ArrayList <Bitmap> dealerCards = new ArrayList<>();
    ArrayList <Bitmap> images = new ArrayList<>();
    ArrayList <Bitmap> images2 = new ArrayList<>();
    Bitmap playerHandValue;
    Bitmap dealerHandValue;


    double width =0;
    double height=0;

    public BasicView(Context context) {
        super(context);
    }

    public BasicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setPlayerHandValue(Bitmap imageOfValue){  // puts icon of value to view
        playerHandValue = GraphicsHelper.resizeImage(imageOfValue,(int)this.height/13);
    }

    public void setDealerHandValue(Bitmap imageOfValue){ // puts icon of value to view
        dealerHandValue = GraphicsHelper.resizeImage(imageOfValue,(int)this.height/13);
    }

    public void resetValueIcons(){
        playerHandValue = null;
        dealerHandValue = null;
    }

    public Bitmap getValueIcon(Dealer dealer, Resources res){  //have to set the value icons names as value2 for example
        String value = "value" + dealer.getHandValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjackv2"));
    }

    public Bitmap getFirstCardValueIcon(Dealer dealer, Resources res){
        String value = "value" + dealer.getFaceUpCard().getValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjackv2"));
    }

    public Bitmap getValueIcon(Gambler player, Resources res){  //have to set the value icons names as value2 for example
        String value = "value" + player.getHands().get(0).getValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjackv2"));
    }

    public void drawCards(Gambler player,Resources res){
        int hand = 0;
        for(int i =0; i<player.getHands().size();i++){
            for (Card card: player.getHands().get(i).getCards()){
                this.draw(card.getCardImage(res),hand);
            }
            hand += 2;
        }
    }

    public void draw(Bitmap image,int handNumber){
        switch (handNumber){
            case 0: // player sits in left sit
                this.images.add(graphics.resizeBitmap(image,(int)this.height/7));
                break;
            case 1:
                this.dealerCards.add(graphics.resizeBitmap(image,(int)this.height/7));
                break;
            case 2: // player's 2nd hand for split
                this.images2.add(graphics.resizeBitmap(image,(int)this.height/7));
                break;

        }

    }
    public void cleanDealerCards(){
        this.dealerCards = new ArrayList<>();
    }

    public void cleanPlayerCards(){
        this.images = new ArrayList<>();
        this.images2 = new ArrayList<>();
    }

    public void getScreenWidth(int width){
        this.width=width;
    }
    public void getScreenHeight(int height){
        this.height = height;
    }



    public void resetView(){
        dealerCards = new ArrayList<>();
        images = new ArrayList<>();

    }
    @Override
    protected void onDraw(Canvas canvas) {

        int count = 0;
        int count2 =0;
        int posX = (int) (this.width/3.737);
        int posM = (int) (this.width/2.8);
        int posY = (int) (this.height/9);
        int posY1 = (int) (this.height/2.7);


        super.onDraw(canvas);

        for(Bitmap dealerCard:this.dealerCards){
            canvas.drawBitmap(dealerCard, (dealerCards.indexOf(dealerCard) * 70) + posM, posY + 200,null);
        }
        for(Bitmap img:this.images){ // draws user on the left - hand 2
            canvas.drawBitmap(img,count*70 + posX,posY1 + 170,null);
            count++;
        }

        for(Bitmap img:this.images2){ // draws user's split hand
            canvas.drawBitmap(img,count2*70 + posX,posY1 + 440,null);
            count2++;
        }

        if (playerHandValue !=null) {
            canvas.drawBitmap(playerHandValue, posX - 110, posY1+120, null);

        }
        if (dealerHandValue !=null){
            canvas.drawBitmap(dealerHandValue, posM -110, posY + 120, null);
        }

        postInvalidate();
}}
