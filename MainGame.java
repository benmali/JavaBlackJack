package com.example.blackjack;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import blackjackgame.Gambler;
import blackjackgame.Graphics;
import blackjackgame.Hand;
import blackjackgame.Matrix;
import blackjackgame.RunGame;
import blackjackgame.Statistics;

public class MainGame extends AppCompatActivity {
    private RewardedVideoAd mRewardedVideoAd;
    Button btnHit,btnStand,btnSplit,btnDouble,btnTip;
    TextView tvBank,tvDisplayDealer,tvDisplayHandValue,tvGetTip;
    ImageView ivCard1,ivCard2,ivCard3,ivCard4,ivCard5,ivCardS1,ivCardS2,ivCardS3,ivCardS4,ivDealerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_game);
        //getSupportActionBar().hide(); // hides action bar
        final Graphics graphics = new Graphics();
        final Resources res = getResources();
        Intent intent = getIntent();
        RunGame previousGame = intent.getParcelableExtra("game");
        final Statistics stats = intent.getParcelableExtra("stats");
        ArrayList<Gambler> players = intent.getParcelableArrayListExtra("players");
        previousGame.setPlayers(players);
        tvDisplayDealer = findViewById(R.id.tvDisplayDealer);
        final RunGame game =createGame(previousGame);
        Gambler p1 = game.getPlayers().get(0);
        tvBank = findViewById(R.id.tvBank);
        tvGetTip= findViewById(R.id.tvGetTip);
        btnHit=findViewById(R.id.btnHit);
        tvDisplayHandValue = findViewById(R.id.tvDisplayHandValue);
        ivCard1 = findViewById(R.id.ivCard1);
        ivCard2 = findViewById(R.id.ivCard2);
        ivCard3 = findViewById(R.id.ivCard3);
        ivCard4 = findViewById(R.id.ivCard4);
        ivCard5 = findViewById(R.id.ivCard5);
        ivCardS1 = findViewById(R.id.ivCardS1);
        ivCardS2 = findViewById(R.id.ivCardS2);
        ivCardS3 = findViewById(R.id.ivCardS3);
        ivCardS4 = findViewById(R.id.ivCardS4);
        ivDealerCard = findViewById(R.id.ivDealerCard);
        btnStand = findViewById(R.id.btnStand);
        btnSplit = findViewById(R.id.btnSplit);
        btnDouble = findViewById(R.id.btnDouble);
        btnTip = findViewById(R.id.btnTip);
        int handVal = p1.getActiveHandValue();
        String val1 =  String.valueOf(handVal);
        String val2 = "Your cards value is ";
        tvDisplayHandValue.setText(val2 + val1);
        String bank = String.valueOf(p1.getBank());
        tvBank.setText(bank);
        tvBank.setVisibility(View.VISIBLE);
        tvDisplayHandValue.setVisibility(View.VISIBLE);
        tvDisplayDealer.setVisibility(View.VISIBLE);
        //p1.getActiveHand().getSplitCards(); // test line for fixing split
        graphics.createImage(game,p1.getHands().get(0),0,ivCard1,res);
        graphics.createImage(game,p1.getHands().get(0),1,ivCard2,res);
        graphics.createImage(game,game.dealer.getHand(),0,ivDealerCard,res);

        if (p1.getActiveHand().isBlackJack()){
            String blackJack = "You got BlackJack";
            tvDisplayHandValue.setText(blackJack);
            showToastBlackJack();
            concludeWithSleep(game,stats);
        }
        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  { // shows p1 hand value after a hit
                Gambler p1 =game.getPlayers().get(0);
                if(p1.getHands().size() == 1){
                    btnDouble.setEnabled(false);
                    btnSplit.setEnabled(false);
                }
                final Hand currentHand = p1.getActiveHand();
                p1.hit();
                    int handVal = currentHand.getValue();
                    String val2 = "Your cards value is ";
                    String val = String.valueOf(handVal);
                    tvDisplayHandValue.setText(val2 + val);
                    if(currentHand == p1.getHands().get(0)){
                        if (currentHand.getCards().size() >= 3) {
                            graphics.createImage(game,p1.getHands().get(0),2,ivCard3,res);

                        }
                        if (currentHand.getCards().size() >= 4) {
                            graphics.createImage(game,p1.getHands().get(0),3,ivCard4,res);
                        }
                        if (currentHand.getCards().size() >= 5) {
                            graphics.createImage(game,p1.getHands().get(0),4,ivCard5,res);

                        }}
                    if (p1.getHands().size() == 2){
                    if(currentHand == p1.getHands().get(1)){
                       if(currentHand.getCards().size() >= 3) {
                           graphics.createImage(game, currentHand, 2, ivCardS3,res);
                       }
                        if(currentHand.getCards().size() >= 4){
                            graphics.createImage(game, currentHand, 3, ivCardS4,res);
                        }
                }}
                    if (p1.getHands().size() == 1) {

                        if (currentHand.getValue() > 21) {
                            showToastBurn();
                            //concludeWithSleep(game,stats);
                            endGame(game,stats);
                        }
                        if (currentHand.getValue() == 21) {
                            showToast21();
                            concludeWithSleep(game,stats);
                        }
                    }
                    if(p1.getHands().size() == 2){
                        if (currentHand.getValue() >= 21) {
                            showToastBurn();
                            if(!p1.checkHandsToPlay()){ // check if player doesn't have more hands, conclude
                                concludeWithSleep(game,stats);
                            }
                        }
                    }
           }
       });
        btnStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gambler p1 = game.getPlayers().get(0);
                Hand hand = p1.getActiveHand();
                p1.stand( hand);
                getStringHandValue(hand,tvDisplayHandValue);
                if(p1.getHands().size() == 1){
                conclude(game,stats);
                }
                if(p1.getHands().size()== 2){
                    if(p1.getActiveHand() == null){ // no hands left to play - conclude game
                        concludeWithSleep(game,stats);
                    }
            }}
        });
        btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gambler p1 = game.getPlayers().get(0);
                if(p1.canSplit()){
                    p1.splitCards();
                    getStringHandValue(p1.getActiveHand(),tvDisplayHandValue);
                    graphics.createImage(game,p1.getHands().get(1),0,ivCardS1,res);
                    graphics.createImage(game,p1.getHands().get(1),1,ivCardS2,res);
                    graphics.createImage(game,p1.getHands().get(0),1,ivCard2,res);
            }}
        });
        btnDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gambler p1 = game.getPlayers().get(0);
                 Hand hand = p1.getActiveHand(); // must catch hand before action!!
                    p1.playerDouble();
                    String bank = String.valueOf(p1.getBank());
                    tvBank.setText(bank);
                    getStringHandValue(hand,tvDisplayHandValue);
                    if (p1.getHands().size() == 1) {
                        graphics.createImage(game,p1.getHands().get(0),2,ivCard3,res);
                        concludeWithSleep(game,stats);
                    }
                    if (p1.getHands().size() == 2){
                        if (hand == p1.getHands().get(1)){
                            graphics.createImage(game,p1.getHands().get(1),2,ivCardS3,res);
                            concludeWithSleep(game,stats);
                        }
                        else{
                            graphics.createImage(game,p1.getHands().get(0),2,ivCard3,res);
                        }
                    }
                }
        });
        btnTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Matrix tipsMatrix = new Matrix(game.getPlayers().get(0),game.dealer);
                int actionCode = tipsMatrix.decisionMatrix();
                String decision = convertCodeToTip(actionCode);
                showToastTip(decision);
            }
        });
    }

    public RunGame createGame(RunGame newGame){
        newGame.initiateGame();
        int handVl = newGame.getDealerFaceUpCardValue();
        String val = String.valueOf(handVl);
        String val2 = "Dealer Face - up card value is ";
        tvDisplayDealer.setText(val2 + val); // showing dealer face-up card value

        return newGame;
    }

    //graphics related methods to be moved
    public void showToastBurn(){
        String burnText = "You Got Burned!";
        Toast toast =Toast.makeText(getApplicationContext(),burnText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }
    public void showToastBlackJack(){
        String burnText = "BlackJack!";
        Toast toast =Toast.makeText(getApplicationContext(),burnText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }
    public void showToast21(){
        String burnText = "Got 21!";
        Toast toast =Toast.makeText(getApplicationContext(),burnText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }
    public void showToastTip(String decision){
        Toast toast =Toast.makeText(getApplicationContext(),decision,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }

    public  void conclude(RunGame game,Statistics stats){
        Intent intent = new Intent(this, ConcludeGame.class);
        intent.putExtra("game",game);
        intent.putExtra("stats",stats);
        intent.putParcelableArrayListExtra("players", game.getPlayers());
        this.startActivity(intent);
    }
    public void endGame(RunGame game,Statistics stats){
        game.resetGame();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game",game);
        intent.putExtra("stats",stats);
        intent.putParcelableArrayListExtra("players", game.getPlayers());
        this.startActivity(intent);

    }


    public void getStringHandValue(Hand hand, TextView tv){
        String handValue = "Your cards value is ";
        String currentValue = String.valueOf(hand.getValue());
        tv.setText(handValue + currentValue);
    }

    public void concludeWithSleep( final RunGame game,final Statistics stats){ // method to call conclude with delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conclude(game,stats);
                    }
                });
            }
        }).start();// end of thread
    }
private String convertCodeToTip(int actionCode){
        String decision = "";
    switch (actionCode){
        case 1:
            decision = "Hit";
            break;
        case 2:
            decision = "Stand";
            break;
        case 3:
            decision = "Double Down";
            break;
        case 4:
            decision = "Split";
            break;
    }
    return decision;
}

}
