package com.example.blackjackv2;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private int size;


    public Deck() { // 6 deck of cards, standard blackjack game
        initiateDeck();
    }


    public Card draw() { // for card counting mode
        if (size == 0) {
            initiateDeck();
        }
        if (size == 1) {
            size -= 1;
            Card card = cards.get(0);
            cards.clear();
            return card;
        }

        Random index = new Random();
        int randIndex = index.nextInt(size - 1); // generates random  index
        size -= 1;
        return cards.remove(randIndex);
    }


    private void initiateDeck(){
        //create decks of cards, standard BlackJack game
        if(size!=0) {
            cards.clear();
        }
            for (int k = 0; k < 6; k++) {
                for (int j = 0; j <= 3; j++) {
                    for (int i = 1; i <= 13; i++) {
                        Card card = new Card(j, i);
                        cards.add(card);
                    }
                }
            }
            size = 312;
        }

    public int getCurrentSize() {
        return size;
    }
}
