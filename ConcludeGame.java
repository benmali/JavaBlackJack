package com.example.blackjack;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import blackjackgame.Gambler;
import blackjackgame.Graphics;
import blackjackgame.RunGame;
import blackjackgame.Statistics;

public class ConcludeGame extends AppCompatActivity {
    TextView tvTest,tvHandValue;
    ImageView ivCard6, ivCard7, ivCard8, ivCard9, ivCard10;
    Button btnRestart;

    Graphics graphics = new Graphics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_conclude_game);
        //getSupportActionBar().hide(); // hides action bar
        Intent intent = getIntent();
        final RunGame game = intent.getParcelableExtra("game");
        final Statistics stats = intent.getParcelableExtra("stats");
        ArrayList<Gambler> players = intent.getParcelableArrayListExtra("players");
        game.setPlayers(players);
        game.concludeGame(stats);
        Resources res = getResources();
        tvTest = findViewById(R.id.tvTest);
        tvTest.setVisibility(View.INVISIBLE);
        tvHandValue = findViewById(R.id.tvHandValue);
        btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setVisibility(View.INVISIBLE);
        ivCard6 = findViewById(R.id.ivCard6);
        ivCard7 = findViewById(R.id.ivCard7);
        ivCard8 = findViewById(R.id.ivCard8);
        ivCard9 = findViewById(R.id.ivCard9);
        ivCard10 = findViewById(R.id.ivCard10);
        graphics.createImage(game,0,ivCard6,res);
        graphics.showCardsWithSleep(game,1,ivCard7,800,res);
        String test1 = String.valueOf(game.getDealerHandValue());
        test1 += " - Dealer";
        String handValue = String.valueOf(game.getPlayers().get(0).getHands().get(0).getValue());
        handValue += " - Player";
        //Canvas canvas = new Canvas();
        //Paint paint = new Paint();
        //canvas.drawBitmap(newimg,100,100, paint);
        if (game.dealer.getCards().size() == 2) {
            graphics.showTextWithSleep(tvTest,test1,900);
            enableButton(800);

        }

        if (game.dealer.getCards().size() == 3) {
           graphics.showCardsWithSleep(game,2,ivCard8,1600,res);
           graphics.showTextWithSleep(tvTest,test1,1600);
           enableButton(1600);


        }
        if (game.dealer.getCards().size() == 4) {
            graphics.showCardsWithSleep(game,2,ivCard8,1600,res);
            graphics.showCardsWithSleep(game,3,ivCard9,2400,res);
            graphics.showTextWithSleep(tvTest,test1,2400);
            enableButton(2400);


        }
        if (game.dealer.getCards().size() == 5) {
            graphics.showCardsWithSleep(game,2,ivCard8,1600,res);
            graphics.showCardsWithSleep(game,3,ivCard9,2400,res);
            graphics.showCardsWithSleep(game,4,ivCard10,3000,res);
            graphics.showTextWithSleep(tvTest,test1,3000);
            enableButton(3000);

        }
        tvHandValue.setText(handValue);
        tvHandValue.setVisibility(View.VISIBLE);

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.resetGame();
                restart(game,stats);
            }
        });
    }
    public void restart(RunGame game,Statistics stats){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game",game);
        intent.putExtra("stats",stats);
        intent.putParcelableArrayListExtra("players", game.getPlayers());
        this.startActivity(intent);
    }

    public void enableButton(final int time){ // method to enable button with delay
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
                        btnRestart.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}
