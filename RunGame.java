package blackjackgame;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class RunGame {
     private Deck deck;
     private ArrayList <Gambler> gamblers;
     public Dealer dealer;

    public RunGame(ArrayList <Gambler> gamblers,Dealer dealer, Deck deck){
        this.deck = deck;
        this.gamblers = gamblers;
        this.dealer = dealer;

    }
    public static void startGame(){
        System.out.println("test");
    }
    public void intiateGame(){
        ArrayList<Gambler> activePlayers = new ArrayList<>();
        Deck deck = new Deck();
        Dealer dealer = new Dealer(deck);

        for (Gambler player : this.gamblers) {
            if (player.getActiveHand().getBet() > 0){   // player without bet is excluded
                    activePlayers.add(player);
            }
        }

        for(int i=0; i <= 1; i++) {
            for (CardPlayer player : activePlayers) // deal 2 cards to initialize the game
            {
                player.getActiveHand().getCard(this.deck);

            }
        }
        // end of game initialization
        // starting players' turns
        for (CardPlayer player : activePlayers)
        {

            while (!player.shouldStop())
            {
                //needs to display cards here and wait for player action before advancing
            }
        }


    }
    public static void concludeGame(ArrayList <Gambler> activePlayers, Dealer dealer)
    {
        for (Gambler player : activePlayers) {
            player.decideState(dealer); //method decides if player wins or loses
        }
    }

    public static ArrayList <Gambler> getBets( ArrayList <Gambler> activePlayers){
         for(Gambler player : activePlayers){
             Scanner key = new Scanner(System.in);
             int value = key.nextInt();
             player.getActiveHand().setBet(value);
         }
         return activePlayers;
    }

}
