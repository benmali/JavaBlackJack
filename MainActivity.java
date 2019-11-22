package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import blackjackgame.Gambler;
import blackjackgame.RunGame;
import blackjackgame.Statistics;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {
    ImageButton btnBet100,btnBet500,btnBet1000;
    TextView tvShowBank;
    private RewardedVideoAd mRewardedVideoAd;
    private Statistics stats;
    private RunGame previousGame;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.info:
                Toast.makeText(this,"Click Watch to renew chips",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stats:
                if(stats!=null) {
                    showToastStats(stats.getStats());
                }
                else{
                    showToastNoStats();
                }
                break;
            case R.id.watch:
                mRewardedVideoAd.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        Intent intent = getIntent();
        previousGame = intent.getParcelableExtra("game");
        stats = intent.getParcelableExtra("stats");
        ArrayList<Gambler> players = intent.getParcelableArrayListExtra("players");
        setContentView(R.layout.activity_main);
        tvShowBank = findViewById(R.id.tvShowBank);
        btnBet100 = findViewById(R.id.btnBet100);
        btnBet500 = findViewById(R.id.btnBet500);
        btnBet1000 = findViewById(R.id.btnBet1000);


        if(previousGame != null){
            previousGame.setPlayers(players);
            String bank = "Your bank is " + previousGame.getPlayers().get(0).getBank();
            tvShowBank.setText(bank);

        }
        else{
            String bank = "Your bank is 5000";
            tvShowBank.setText(bank);
            previousGame = new RunGame();
            stats = new Statistics();

        }



        btnBet100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(previousGame,200,stats);
            }
        });

        btnBet500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(previousGame,500,stats);
            }
        });

        btnBet1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBet(previousGame,1000,stats);
            }
        });


    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    public void main(int bet,RunGame game,Statistics stats)
    {
        game.getPlayers().get(0).setBet(bet);
        Intent intent = new Intent(this,MainGame.class);
        intent.putExtra("game",game);
        intent.putExtra("stats",stats);
        intent.putParcelableArrayListExtra("players", game.getPlayers());
        this.startActivity(intent);
    }

    public void cardCountingMode(){
        Intent intent = new Intent(this, CardCounting.class);
        this.startActivity(intent);
    }
    public void startAd(RunGame game){
        if(game!=null){
            if(game.getPlayers().get(0).getBank() ==0) {
            game.getPlayers().get(0).incrementBank(5000);
            tvShowBank.setText("Your bank is " + game.getPlayers().get(0).getBank());
        }
        }
        else{
            tvShowBank.setText("Your bank is 5000");
        }
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

    }
    public void showToastStats(String stats){
        Toast toast =Toast.makeText(getApplicationContext(),stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }
    public void showToastNoStats(){
        String stats = "No Statistics yet";
        Toast toast =Toast.makeText(getApplicationContext(),stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }
    public void showToastBadInput(){
        String stats = "Bad input - Try Again";
        Toast toast =Toast.makeText(getApplicationContext(),stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }
    public void showToastNotEnoughMoney(RunGame game) {
        if (game ==null){
            String stats = "Not enough money to bet - your bank is 5000" ;
            Toast toast = Toast.makeText(getApplicationContext(), stats, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 320);
            toast.show();
        }
        else{
             int bankOfPlayer= game.getPlayers().get(0).getBank();
            String stats = "Not enough money to bet - your bank is " + bankOfPlayer;
            Toast toast = Toast.makeText(getApplicationContext(), stats, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 320);
            toast.show();
        }

    }
    public void showToastAdLoaded(){
        String stats = "Ad Loaded";
        Toast toast =Toast.makeText(getApplicationContext(),stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
    }

    public void checkBet(RunGame game, int bet, Statistics stats){
        if(game == null) {
            if(bet<=5000){
                main(bet, game, stats);
            }
            else{
                showToastNotEnoughMoney(game);
            }
        }
        else{
            if(game.getPlayers().get(0).getBank()>=bet){
                main(bet, game, stats);
            }
            else{
                showToastNotEnoughMoney(game);
            }
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        showToastAdLoaded();


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
        if (previousGame!=null) {
            this.previousGame.getPlayers().get(0).incrementBank(1000);
            tvShowBank.setText("Your bank is "+ previousGame.getPlayers().get(0).getBank());
        }
        else{
            showToastNoStats();
        }

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
