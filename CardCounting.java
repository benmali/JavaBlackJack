package com.example.blackjack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import androidx.appcompat.app.AppCompatActivity;
import blackjackgame.Card;
import blackjackgame.CountingGame;
import blackjackgame.Dealer;
import blackjackgame.Gambler;
import blackjackgame.Graphics;
import blackjackgame.Hand;

public class CardCounting extends AppCompatActivity {
      Button btnPlayAgain,btnGetCount ,btnCountH,btnCountS,btnCountSp,btnCountD;
      TextView tvGetCount,tvCardsOut;
      ImageButton btnBet100,btnBet500,btnBet1000;
      private GameView view;
      private Lock lock = new ReentrantLock();
      private Condition conditionMet = lock.newCondition();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_card_counting);
        //get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        //getSupportActionBar().hide();
        final Resources res = getResources();
        final Graphics graphics = new Graphics();
        view = findViewById(R.id.viewBasic);
        view.getScreenHeight(height); // parsing screen size to view
        view.getScreenWidth(width);
        btnGetCount = findViewById(R.id.btnGetTip);
        tvGetCount = findViewById(R.id.tvGetCount);
        tvCardsOut = findViewById(R.id.tvCardsOut);
        btnCountH = findViewById(R.id.btnBasicH);
        btnCountS = findViewById(R.id.btnBasicS);
        btnCountSp = findViewById(R.id.btnBasicSp);
        btnCountD = findViewById(R.id.btnBasicD);
        btnPlayAgain = findViewById(R.id.btnPlayAgainB);
        btnBet100 = findViewById(R.id.btnBet100);
        btnBet500 = findViewById(R.id.btnBet500);
        btnBet1000 = findViewById(R.id.btnBet1000);
        hideGamebuttons();
        final CountingGame game = new CountingGame(6, 2);

        //game.getPlayers().get(1).getActiveHand().getSplitCards();
        btnGetCount = findViewById(R.id.btnGetTip);
        btnGetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvGetCount.setText("The running count is  " + (game.deck.getRunningCount() - game.dealer.getCards().get(1).getCount()));
                tvCardsOut.setText( "Cards out  " +(312 - game.deck.getCurrentSize()));
            }
        });

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain(graphics,game,res,view,lock,conditionMet);
            }
        });
        btnCountH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = game.getPlayers().get(0).getActiveHand();
                game.getPlayers().get(0).hitCounting();
                if(hand!=null){ // avoids program crushing on null hand
                    view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),2);
                    if( game.getPlayers().get(0).getActiveHand() == null){
                        showDealerCards(game,res);
                    }
                }
                view.setPlayerHandValue(view.getValueIcon(game.getPlayers().get(0),res)); // shows hand value icon
            }
        });

        btnCountS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = game.getPlayers().get(0).getActiveHand();
                if (hand!=null) {
                    game.getPlayers().get(0).stand(hand);

                    showDealerCards(game,res);
                }
            }
        });

        btnCountD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hand hand = game.getPlayers().get(0).getActiveHand();
                if (hand!=null) {
                  game.getPlayers().get(0).playerDoubleCounting();
                    view.draw(hand.getCards().get(hand.getCards().size() - 1).getCardImage(res),2);
                    showDealerCards(game,res);
                    view.setPlayerHandValue(view.getValueIcon(game.getPlayers().get(0),res)); // shows hand value icon
            }}
        });

        btnCountSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.getPlayers().get(0).splitCounting();
            }
        });

        btnBet100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.initiateGame(); // game logic
                initiateGame(graphics,game.getPlayers(),game.dealer,view,res,lock,conditionMet); //showing first cards
                continueGame(game,res,lock,conditionMet);
                showGameButtons();
                hideBetButtons();

            }
        });

        btnBet1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.initiateGame(); // game logic
                initiateGame(graphics,game.getPlayers(),game.dealer,view,res,lock,conditionMet); //showing first cards
                continueGame(game,res,lock,conditionMet);
                showGameButtons();
                hideBetButtons();

            }
        });

        btnBet500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.initiateGame(); // game logic
                initiateGame(graphics,game.getPlayers(),game.dealer,view,res,lock,conditionMet); //showing first cards
                continueGame(game,res,lock,conditionMet);
                showGameButtons();
                hideBetButtons();

            }
        });

    }

    public void initiateGame(Graphics graphics,final ArrayList<Gambler> players, final Dealer dealer, final GameView view, final Resources res, final Lock lock, final Condition condition){
        btnCountH.setEnabled(false);
        btnCountS.setEnabled(false);
        btnCountSp.setEnabled(false);
        btnCountD.setEnabled(false);
        btnPlayAgain.setEnabled(false);
        btnBet100.setVisibility(View.GONE);
        btnBet1000.setVisibility(View.GONE);
        btnBet500.setVisibility(View.GONE);
        graphics.displayFirstCards(players,dealer,view,res,lock,condition);
        view.setPlayerHandValue(view.getValueIcon(players.get(0),res)); // shows hand value icon
    }

    public void drawWithSleep(final int time,final Bitmap img, final int hand){ // method to enable button with delay for bots
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                    view.draw(img, hand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void continueGame( final CountingGame game,final Resources res,final Lock lock,final Condition condition){
        //condition is waiting on another thread to avoid UI thread getting stuck waiting, cards gets displayed on main thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // waits for thread of showing cards to finish, then deals other cards
                       game.botsPlay();
                        int time = 100;
                        int hand = -1; // hand counting starts from -1 to pass on the user, since he's first in players list
                        for(Gambler player :game.getPlayers()) {
                            if(player.getName()==null){ // only do for bots
                                if(player.getHands().get(0).getCards().size()>2) { // if bot took cards
                                    for(int i=2; i<player.getHands().get(0).getCards().size();i++){ // show all new cards beginning at index 2 (3rd card)
                                        Card card = player.getHands().get(0).getCards().get(i);
                                        drawWithSleep(time,card.getCardImage(res),hand);
                                        time +=1000;
                                    }
                                }
                            }
                            hand++;
                        }
                        btnCountH.setEnabled(true);// enabling buttons only after cards were dealt
                        btnCountS.setEnabled(true);
                        btnCountSp.setEnabled(true);
                        btnCountD.setEnabled(true);



                        if(game.getPlayers().get(0).getActiveHand() == null){ // if user has black jack
                            showDealerCards(game,res);
                        }
                    }
                });
            }
        }).start();
    }

    public void showDealerCards(final CountingGame game, final Resources res){
            //must run on a different thread than UI thread for sleep
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    view.cleanDealerCards();
                    game.dealer.openCardsCounting(game.deck);
                    for(Card card : game.dealer.getCards()){
                        view.draw(card.getCardImage(res),4);
                        Thread.sleep(800);
                    }
                    view.setDealerHandValue(view.getValueIcon(game.dealer,res)); // shows hand value icon

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnPlayAgain.setEnabled(true);
                    }
                });
            }
        }).start();
            }

    public void playAgain(Graphics graphics, CountingGame game,Resources res,GameView view,Lock lock, Condition condition){
        view.resetView();
        view.resetValueIcons();
        game.resetGame();
        //game.initiateGame();
        //initiateGame(graphics,game.getPlayers(),game.dealer,view,res,lock,condition);
        //continueGame(game,res,lock,condition);
        showBetButtons();
        hideGamebuttons();


        }

        private void showBetButtons(){
            btnBet100.setVisibility(View.VISIBLE);
            btnBet1000.setVisibility(View.VISIBLE);
            btnBet500.setVisibility(View.VISIBLE);
        }
        private void hideBetButtons(){
            btnBet100.setVisibility(View.GONE);
            btnBet1000.setVisibility(View.GONE);
            btnBet500.setVisibility(View.GONE);

        }

        private void showGameButtons(){
            btnCountH.setVisibility(View.VISIBLE);
            btnCountS.setVisibility(View.VISIBLE);
            btnCountSp.setVisibility(View.VISIBLE);
            btnCountD.setVisibility(View.VISIBLE);
            btnPlayAgain.setVisibility(View.VISIBLE);
            btnGetCount.setVisibility(View.VISIBLE);
        }

        private void hideGamebuttons(){
            btnCountH.setVisibility(View.GONE);
            btnCountS.setVisibility(View.GONE);
            btnCountSp.setVisibility(View.GONE);
            btnCountD.setVisibility(View.GONE);
            btnPlayAgain.setVisibility(View.GONE);
            btnGetCount.setVisibility(View.GONE);
        }



    }



