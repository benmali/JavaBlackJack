package blackjackgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackjack.BasicView;
import com.example.blackjack.GameView;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import androidx.appcompat.app.AppCompatActivity;

public class Graphics extends AppCompatActivity {{}



    public void showTextWithSleep(final TextView tv, final String text, final int time){ // method to call conclude with delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(text);
                        tv.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();// end of thread
    }
    public void showText(final TextView tv, final String text){
        tv.setText(text);
        tv.setVisibility(View.VISIBLE);

    }



    public void showCardsWithSleep(final RunGame game, final int idx, final ImageView iv, final int time, final Resources res){ // method to call conclude with delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createImage(game,idx,iv,res);
                    }
                });
            }
        }).start();// end of thread
    }
    public void createImage(RunGame game,  int i, ImageView iv,Resources res){ // i represents idx of card in hand
        String card = game.getDisplayCardInIdx(game.dealer, i);
        Bitmap cardShow = BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + card, null, "com.example.blackjack"));
        //Bitmap cardShow = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("@drawable/" + card, null, "com.example.blackjack"));
        Bitmap image = resizeBitmap(cardShow, 300);
        iv.setImageBitmap(image);

    }
    //method overload for player
    public void createImage(RunGame game, Hand hand, int i, ImageView iv,Resources res){ // i represents idx of card in hand
        String card = game.getDisplayCardInIdx(hand, i);
        Bitmap cardShow = BitmapFactory.decodeResource(res, res.getIdentifier("@drawable/" + card, null, "com.example.blackjack"));
        Bitmap newimg3 = resizeBitmap(cardShow, 300);
        iv.setImageBitmap(newimg3);

    }

    public Bitmap resizeBitmap(Bitmap getBitmap, int maxSize) {
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x;

        if (width >= height && width > maxSize) {
            x = width / height;
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = height / width;
            height = maxSize;
            width = (int) (maxSize / x);
        }
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }

    public void displayFirstCards(final ArrayList<Gambler> players, final Dealer dealer, final GameView view, final Resources res, final Lock lock, final Condition condition){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    int hand = 0; // sets the hand index for card to be displayed at
                    int cardIndex = 0;
                    Thread.sleep(500);
                    for(int i =0; i<=1;i++){ // do twice, showing first 2 cards
                        for( Gambler player: players){
                            if (player.getName() == null) { // show bots only inside loop of players
                            view.draw(player.getHands().get(0).getCards().get(cardIndex).getCardImage(res), hand);
                            hand++;
                            Thread.sleep(800);
                            }
                        }
                        view.draw(players.get(0).getHands().get(0).getCards().get(i).getCardImage(res), 2);
                        Thread.sleep(800);


                        if(cardIndex ==0) { //makes first dealer card a flipped card
                            view.draw(BitmapFactory.decodeResource(res,res.getIdentifier("@drawable/card_back",
                                    null, "com.example.blackjack")),4);

                            hand = 0;
                            cardIndex ++;
                        }

                        else{
                            view.draw(dealer.getFaceUpCard().getCardImage(res), 4);
                        }

                        Thread.sleep(800);
                    }
                    view.setPlayerHandValue(view.getValueIcon(players.get(0),res)); // displays value icon after all cards are out
                    view.setDealerHandValue(view.getFirstCardValueIcon(dealer,res)); // shows hand value icon

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                         Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            lock.lock();
                            condition.signalAll();
                            lock.unlock();
                        }
                    }
                });
            }
        }).start();

    }

//method overload for BasicStrategy
    public void displayFirstCards(final ArrayList<Gambler> players, final Dealer dealer, final BasicView view, final Resources res, final Lock lock, final Condition condition){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    int hand = 0; // sets the hand index for card to be displayed at
                    int cardIndex = 0;
                    Thread.sleep(500);
                    for(int i =0; i<=1;i++){ // do twice, showing first 2 cards
                        for( Gambler player: players){
                            if (player.getName() == null) { // show bots only inside loop of players
                                view.draw(player.getHands().get(0).getCards().get(cardIndex).getCardImage(res), hand);
                                hand++;
                                Thread.sleep(800);
                            }
                        }
                        view.draw(players.get(0).getHands().get(0).getCards().get(i).getCardImage(res), 0);
                        Thread.sleep(800);


                        if(cardIndex ==0) { //makes first dealer card a flipped card
                            view.draw(BitmapFactory.decodeResource(res,res.getIdentifier("@drawable/card_back",
                                    null, "com.example.blackjack")),1);

                            hand = 0;
                            cardIndex ++;
                        }
                        else{
                            view.draw(dealer.getFaceUpCard().getCardImage(res), 1);
                        }
                        Thread.sleep(800);

                    }
                    view.setPlayerHandValue(view.getValueIcon(players.get(0),res)); // displays value icon after all cards are out
                    view.setDealerHandValue(view.getFirstCardValueIcon(dealer,res)); // shows hand value icon

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            lock.lock();
                            condition.signalAll();
                            lock.unlock();

                        }
                    }
                });
            }
        }).start();

    }

    public void showToastTip(Context context, String decision){
        Toast toast =Toast.makeText(context,decision,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }

    public void showToastNotEnoughMoney(Context context, RunGame game) {
        if (game ==null){
            String stats = "Not enough money to bet - your bank is 5000" ;
            Toast toast = Toast.makeText(context, stats, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 320);
            toast.show();
        }
        else{
            int bankOfPlayer= game.getPlayers().get(0).getBank();
            String stats = "Not enough money to bet - your bank is " + bankOfPlayer;
            Toast toast = Toast.makeText(context, stats, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 320);
            toast.show();
        }

    }
    public void showToastAdLoaded(Context context){
        String stats = "Ad Loaded";
        Toast toast =Toast.makeText(context,stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }

    public void showToastBlackJack(Context context){
        String burnText = "BlackJack!";
        Toast toast =Toast.makeText(context,burnText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }

    public void showToastBurned(Context context){
        String burnText = "BlackJack!";
        Toast toast =Toast.makeText(context,burnText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }



}
