package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainMenu extends AppCompatActivity implements RewardedVideoAdListener {


    ImageButton btnCardCounting, btnBasicStrategy, btnHowToPlay, btnGetChips, btnCredits,btnStatistics;
    private RewardedVideoAd mRewardedVideoAd;
    private CardView viewCounting, viewBasic,viewChips,viewInfo,viewStats,viewCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        btnCardCounting = findViewById(R.id.btnCardCounting);
        btnBasicStrategy = findViewById(R.id.btnBasicStrategy);
        btnHowToPlay = findViewById(R.id.btnHowToPlay);
        btnGetChips = findViewById(R.id.btnEarnChips);
        btnCredits = findViewById(R.id.btnCredits);
        btnStatistics = findViewById(R.id.btnStatistics);
        viewCounting = findViewById(R.id.viewCounting);
        viewBasic = findViewById(R.id.viewBasicStrategy);
        viewChips = findViewById(R.id.viewChips);
        viewInfo = findViewById(R.id.viewInfo);
        viewStats = findViewById(R.id.viewStats);
        viewCredits = findViewById(R.id.viewCredits);

        btnCardCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(CardCounting.class);
            }
        });

        btnBasicStrategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(BasicStrategy.class);
            }
        });

        btnGetChips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardedVideoAd.show();
                loadRewardedVideoAd();
            }
        });


        viewCounting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(CardCounting.class);
            }
        });

        viewBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(BasicStrategy.class);
            }
        });

        viewChips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardedVideoAd.show();
                loadRewardedVideoAd();
            }
        });
        viewCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Credits.class);
            }
        });
        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Rules.class);
            }
        });

        btnHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Rules.class);
            }
        });
        viewStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastNoStats();
            }
        });
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastNoStats();
            }
        });
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(Credits.class);
            }
        });


    }

    private void goToActivity(Class cls){
        Intent intent = new Intent(this, cls);
        this.startActivity(intent);

    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    public void showToastNoStats(){
        String stats = "No Statistics yet";
        Toast toast =Toast.makeText(getApplicationContext(),stats,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 320);
        toast.show();
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
        /*
        if (previousGame!=null) {
            this.previousGame.getPlayers().get(0).incrementBank(1000);
            tvShowBank.setText("Your bank is "+ previousGame.getPlayers().get(0).getBank());
        }
        else{
            showToastNoStats();
        }
*/
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
