package com.example.blackjack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import blackjackgame.Dealer;
import blackjackgame.Gambler;
import blackjackgame.Graphics;

public class GameView extends View {
    Graphics graphics = new Graphics();
    ArrayList <Bitmap> dealerCards = new ArrayList<>();
    ArrayList <Bitmap> images = new ArrayList<>();
    ArrayList <Bitmap> images2 = new ArrayList<>();
    ArrayList <Bitmap> images3 = new ArrayList<>();
    Bitmap playerHandValue;
    Bitmap dealerHandValue;

    double width =0;
    double height=0;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setPlayerHandValue(Bitmap imageOfValue){  // puts icon of value to view
        playerHandValue = graphics.resizeBitmap(imageOfValue,(int)this.height/13);
    }

    public void setDealerHandValue(Bitmap imageOfValue){ // puts icon of value to view
        dealerHandValue = graphics.resizeBitmap(imageOfValue,(int)this.height/13);
    }


    public void resetValueIcons(){
        playerHandValue = null;
        dealerHandValue = null;
    }

    public Bitmap getValueIcon(Dealer dealer, Resources res){  //have to set the value icons names as value2 for example
        String value = "value" + dealer.getHandValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjack"));
    }

    public Bitmap getFirstCardValueIcon(Dealer dealer, Resources res){
        String value = "value" + dealer.getFaceUpCard().getValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjack"));
    }

    public Bitmap getValueIcon(Gambler player, Resources res){  //have to set the value icons names as value2 for example
        String value = "value" + player.getHands().get(0).getValue() ;// + player.getHands().get(0).getValue();
        return BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + value, null, "com.example.blackjack"));
    }

    public void draw(Bitmap image,int handNumber){
        switch (handNumber){
            case 0:// case represents order of card to deal 0 is firs sit on the right
                this.images2.add(graphics.resizeBitmap(image,(int)this.width/12));
                break;

            case 1:
                this.images3.add(graphics.resizeBitmap(image,(int)this.width/12));
                break;

            case 2: // player sits in left sit
                this.images.add(graphics.resizeBitmap(image,(int)this.width/12));

                break;
            case 4:
                this.dealerCards.add(graphics.resizeBitmap(image,(int)this.width/12));
                break;
        }

    }
    public void cleanDealerCards(){
        this.dealerCards = new ArrayList<>();
    }

    public void getScreenWidth(int width){
        this.width=width;
    }
    public void getScreenHeight(int height){
        this.height = height;
    }
    private double calcWidthFactor(){ // sets the factor of shrinking/stretching horizontally
        return this.width/663;
    }
    private double calcHeightFactor(){ // sets the factor of shrinking/stretching vertically
        return this.height/405;
    }

    public void resetView(){
         dealerCards = new ArrayList<>();
         images = new ArrayList<>();
         images2 = new ArrayList<>();
         images3 = new ArrayList<>();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        int count = 0;
        int count2 = 0;
        int count3 =0;
        int posX = (int) (this.width/3.737);
        int posM = (int) (this.width/2.21);
        int posY = (int) (this.height/9);
        int posX3 = (int) (this.width/1.495);
        int posY1 = (int) (this.height/1.963);
        int posY2 = (int) (this.height/1.8);

        super.onDraw(canvas);

        for(Bitmap dealerCard:this.dealerCards){
            canvas.drawBitmap(dealerCard, (dealerCards.indexOf(dealerCard) * 25) + posM,dealerCards.indexOf(dealerCard)*15 + posY,null);
        }
        for(Bitmap img:this.images){ // draws user on the left - hand 2
            canvas.drawBitmap(img,count*25 + posX,count*15 + posY1,null);
            count++;
        }
        if(this.images2.size() > 0){ //bot 1 - hand 0
            for(Bitmap img2:this.images2){
                canvas.drawBitmap(img2,count2*25 + posX3,count2*15 + posY1,null);
                count2++;
            }
        }
        if(this.images3.size() > 0){ // bot 2 - hand 1
            for(Bitmap img2:this.images3){
                canvas.drawBitmap(img2,count3*25 + posM,count3*15 + posY2,null);
                count3++;
            }//(img2,count3*25 + posX,count3*15 + posY1,null);
        }
        if (playerHandValue !=null) {
            canvas.drawBitmap(playerHandValue, posX - 100, posY1 -60, null);

        }
        if (dealerHandValue !=null){
            canvas.drawBitmap(dealerHandValue, posM -100, posY - 60, null);
        }
        postInvalidate();

        //pos 1  x3, y1
        //pos2 m y2
        //pos 3 x y1
    }
}
