package blackjackgame;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    List cards = new ArrayList<>();
    public Deck(){

        for(int i = 1; i <=13; i ++ )
        {
            Card card = new Card("Hearts", i);
            cards.add(card);
        }

    }
    public Card draw(){
        Card card = new Card("type",1);
        return card;
    }
}
