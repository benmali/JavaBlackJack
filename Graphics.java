package blackjackgame;

import android.content.Context;

import java.util.ArrayList;

public class Graphics {

    public String formatCard(Card card){ // for displaying card on UI
        if (card.value == 1){
            return "Ace";
        }
        if (card.value == 11) {
            return "Jack";
        }
        if (card.value == 12) {
            return "Queen";
        }
        if (card.value == 13) {
                return "King";
        }
        else{
            return null;
        }
    }
    public void showHandValue(Gambler player, Context context){
         Hand activeHand = player.getActiveHand();
            System.out.println(player.getName() + " your hand value is " +activeHand.getValue());
            System.out.println("\n");
        System.out.println("Dealer face up card is" + dealer.getFaceUpCard());
        }



    }

}


