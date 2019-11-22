package blackjackgame;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList <Card> cards = new ArrayList<>();
    private int numberOfDecks;
    private int size;
    private int runningCount = 0;
    public Deck(){
        for (int j = 1; j <=4;j++ ){
            for(int i = 1; i <=13; i ++ ){
                Card card = new Card(j, i);
                cards.add(card);
        }}
    }
    public Card draw(){
        Random suit = new Random();
        Random value = new Random();
        int suitOfCard = suit.nextInt(4 - 1);
        int valueOfCard = value.nextInt(14 - 1) + 1;
        return new Card(suitOfCard,valueOfCard);
    }

// Card counting implementation
    public Deck(int decks){ // deck for card counting
        this.numberOfDecks = decks;
        for(int k =0; k <numberOfDecks;k++ ){
            for (int j = 0; j <=3;j++ ){
                for(int i = 1; i <=13; i ++ ){
                    Card card = new Card(j, i);
                    cards.add(card);
                }}
        }
        this.size = numberOfDecks*52;

    }
    public Card drawFromDeck(){ // for card counting mode
        if(this.size == 0){
            shuffleDeck();
            return null;
        }
        if(this.size == 1){
            this.size -= 1;
            //this.runningCount++;
            Card card = cards.remove(0);
            this.runningCount += card.getCount();
            return card;
        }
        Random index = new Random();
        int randIndex = index.nextInt(this.size - 1); // generates random  index
        this.size -= 1;

        Card card = cards.remove(randIndex);
        this.runningCount += card.getCount();
        return card;
    }

    public void shuffleDeck(){ // creates deck of the same size
        cards.clear();
        for(int k =0; k <numberOfDecks;k++ ){
            for (int j = 0; j <=3;j++ ){
                for(int i = 1; i <=13; i ++ ){
                    Card card = new Card(j, i);
                    cards.add(card);
                }}
        }
        this.size = numberOfDecks *52;
    }
    public int getCurrentSize(){
        return cards.size();
    }
    public int getRunningCount(){
        return runningCount;
    }
}
