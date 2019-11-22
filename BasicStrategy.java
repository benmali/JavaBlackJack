package com.example.blackjack;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import androidx.appcompat.app.AppCompatActivity;
import blackjackgame.Card;
import blackjackgame.Gambler;
import blackjackgame.Graphics;
import blackjackgame.Hand;
import blackjackgame.Matrix;
import blackjackgame.RunGame;
import blackjackgame.Statistics;

public class BasicStrategy extends AppCompatActivity implements RewardedVideoAdListener {
    ImageButton btnBet100,btnBet500,btnBet1000;
    TextView tvBank,tvDealerHand,tvPlayerHand;
    Button btnBasicH,btnBasicS, btnBasicSp, btnBasicD,btnGetTip,btnPlayAgainB;
    BasicView view;
    RunGame game = new RunGame();
    Graphics graphics = new Graphics();
    Statistics stats = new Statistics();
    Resources res;
    Gambler player = game.getPlayers().get(0);
    Context context;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private RewardedVideoAd mRewardedVideoAd;
    int tipCounter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_strategy);
        res = getResources();
        context = getApplicationContext();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        view = findViewById(R.id.viewBasic);
        view.getScreenHeight(height); // parsing screen size to view
        view.getScreenWidth(width);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        tvBank = findViewById(R.id.tvBank);
        tvDealerHand=  findViewById(R.id.tvDealerHand);
        tvPlayerHand = findViewById(R.id.tvPlayerHand);
        btnGetTip = findViewById(R.id.btnGetTip);
        btnPlayAgainB = findViewById(R.id.btnPlayAgainB);
        btnBet100 = findViewById(R.id.btnBet100);
        btnBet500 = findViewById(R.id.btnBet500);
        btnBet1000 = findViewById(R.id.btnBet1000);
        btnBasicD = findViewById(R.id.btnBasicD);
        btnBasicH = findViewById(R.id.btnBasicH);
        btnBasicS = findViewById(R.id.btnBasicS);
        btnBasicSp = findViewById(R.id.btnBasicSp);
        btnBasicD.setVisibility(View.GONE);
        btnBasicH.setVisibility(View.GONE);
        btnBasicSp.setVisibility(View.GONE);
        btnBasicS.setVisibility(View.GONE);
        btnPlayAgainB.setVisibility(View.GONE);
        btnGetTip.setVisibility(View.GONE);


        btnPlayAgainB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.cleanPlayerCards();
                view.cleanDealerCards();
                view.resetValueIcons();
                btnBet100.setVisibility(View.VISIBLE);
                btnBet500.setVisibility(View.VISIBLE);
                btnBet1000.setVisibility(View.VISIBLE);
                btnBasicD.setVisibility(View.GONE);
                btnBasicH.setVisibility(View.GONE);
                btnBasicSp.setVisibility(View.GONE);
                btnBasicS.setVisibility(View.GONE);
                btnPlayAgainB.setVisibility(View.GONE);
                btnGetTip.setVisibility(View.GONE);
                tvDealerHand.setVisibility(View.GONE);
                tvPlayerHand.setVisibility(View.GONE);
            }
        });
        btnBasicH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = player.getActiveHand();
                player.hit();
                view.setPlayerHandValue(view.getValueIcon(player,res)); // displays value icon after all cards are out
                tvPlayerHand.setText("Active hand value is " + hand.getValue());
                if(player.getHands().get(0) == hand){
                    view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),0);
                }
                else{
                    view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),2);
                }

                if(player.getActiveHand() == null){
                    if(player.getLivingHands().size() > 0){ //makes sure if another hand is alive in split
                        showDealerCards(game,res);

                    }
                    else{

                        showDealerHand();
                    }
                    player.decideState(game.dealer,stats);

                }
            }
        });
        btnBasicS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = player.getActiveHand();
                if (hand != null) {
                    player.stand(hand);
                }

                if (player.getActiveHand() == null) {
                    showDealerCards(game, res);
                    player.decideState(game.dealer,stats);

                }

            }
        });
        btnBasicSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.splitCards();
                view.cleanPlayerCards();
                view.drawCards(player,res);
                tvPlayerHand.setText("Active hand value is " + player.getActiveHandValue());

            }
        });
        btnBasicD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = player.getActiveHand();
                int handIndex = player.getHands().indexOf(hand);
                if(hand!=null) {
                    player.playerDouble();
                    tvPlayerHand.setText("Active hand value is " + hand.getValue());

                }
                switch (handIndex){
                    case 0:
                        view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),0 );
                        break;
                    case 1:
                        view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),2 );
                        break;

                }
                if(player.getActiveHand() == null){
                    if(player.getLivingHands().size() > 0){ //makes sure if another hand is alive in split
                        showDealerCards(game,res);

                    }
                    else{

                        showDealerHand();
                        graphics.showToastBurned(context);
                    }
                    player.decideState(game.dealer,stats);

                }
            }
        });
        view = findViewById(R.id.viewBasic);

        btnBet100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(200,stats);
            }
        });

        btnBet500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(500,stats);
            }
        });

        btnBet1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(1000,stats);
            }
        });

        btnGetTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipCounter<0){
                    goToActivity(PopCommerc.class);
                    return;
                }
                tipCounter --;
                if(player.getActiveHand()!=null){
                    Matrix tipsMatrix = new Matrix(player,game.dealer);
                    graphics.showToastTip(context,tipsMatrix.convertCodeToTip(tipsMatrix.decisionMatrix()));
                }

            }
        });
    }

    private void goToActivity(Class cls){
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);

    }
    private void main(int bet, Statistics stats){
        game.resetGame();
        this.game.initiateGame(); // if checkBet successful, calling main method
        //game.getPlayers().get(0).getActiveHand().getSplitCards();
        //view.setPlayerHandValue(view.getValueIcon(player,res));
        game.getPlayers().get(0).setBet(bet);
        graphics.displayFirstCards(game.getPlayers(),game.dealer,view,res,lock,condition);
        btnBet100.setVisibility(View.GONE);
        btnBet500.setVisibility(View.GONE);
        btnBet1000.setVisibility(View.GONE);
        btnBasicD.setVisibility(View.VISIBLE);
        btnBasicH.setVisibility(View.VISIBLE);
        btnBasicSp.setVisibility(View.VISIBLE);
        btnBasicS.setVisibility(View.VISIBLE);
        btnPlayAgainB.setVisibility(View.VISIBLE);
        btnGetTip.setVisibility(View.VISIBLE);
        tvPlayerHand.setText("Active hand value is " + player.getActiveHand().getValue());
        tvBank.setText("Your bank is " + player.getBank());
        tvDealerHand.setText("Dealer Face Up card is " + game.dealer.getFaceUpCard().getValue());
        tvDealerHand.setVisibility(View.VISIBLE);
        tvPlayerHand.setVisibility(View.VISIBLE);

    }

    public void showDealerHand(){ // when player busts
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    view.cleanDealerCards();
                    for(Card card : game.dealer.getCards()){
                        view.draw(card.getCardImage(res),1);
                        Thread.sleep(800);
                    }
                    view.setDealerHandValue(view.getValueIcon(game.dealer,res));


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDealerHand.setText("Dealer Hand value is " + game.dealer.getHandValue());
                        tvBank.setText("Your bank is " + player.getBank());

                    }
                });
            }
        }).start();
    }


    public void showDealerCards(final RunGame game, final Resources res){ // player has a valid hand
        //must run on a different thread than UI thread for sleep
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    view.cleanDealerCards();
                    game.dealer.openCards(game.deck);
                    for(Card card : game.dealer.getCards()){
                        view.draw(card.getCardImage(res),1);
                        Thread.sleep(800);
                    }
                    view.setDealerHandValue(view.getValueIcon(game.dealer,res));


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDealerHand.setText("Dealer Hand value is " + game.dealer.getHandValue());
                        tvBank.setText("Your bank is " + player.getBank());

                    }
                });
            }
        }).start();
    }

    public void checkBet(int bet, Statistics stats){
        if(game == null) {
            if(bet<=5000){
                main(bet, stats);
            }
            else{
                graphics.showToastNotEnoughMoney(context,game);
            }
        }
        else{
            if(game.getPlayers().get(0).getBank()>=bet){
               main( bet, stats);
            }
            else{
                graphics.showToastNotEnoughMoney(context,game);
            }
        }
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
