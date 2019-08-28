package blackjackgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Hand {
    int value = 0;
    List<Card> cards = new ArrayList<>();
    int bet = 0;
    boolean standFlag = false;
    boolean burnFlag = false;
    boolean blackJack = false;
    int ace_counter = 0;
    HashMap<Integer, Integer> value_dic = new HashMap<>();


    public Hand(int bet, List<Card> cards) {
        this.bet = bet;
        this.cards = new ArrayList<>(cards);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<Card> getCards() {
        return cards;
    }


    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean isstandFlag() {
        return standFlag;
    }

    public void setstandFlag() {
        this.standFlag = true;
    }

    public boolean isburnFlag() {
        return burnFlag;
    }

    public void setburnFlag() {
        this.burnFlag = true;
    }

    public boolean isBlackJack() {
        return blackJack;
    }

    public void setBlackJack() {
        this.blackJack = true;
    }

    public boolean canSplit() {
        ArrayList<Card> cardlst = new ArrayList<>();
        for (Card card : cards) {
            cardlst.add(card);
        }
        if (cardlst.size() == 2 && cards.get(0).getValue() == cards.get(1).getValue()) {
            return true;
        } else {
            System.out.println("Cant split");
            return false;
        }
    }

    public void stand() {
        this.setstandFlag();
    }

    public void win(Gambler player) {
        this.bet *= 2;
        player.incrementBank(this.bet);
        this.setBet(0);
    }

    public void winBlack(Gambler player) {
        this.bet *= 2.5;
        player.incrementBank(this.bet);
        this.bet = 0;
    }

    public void tie(Gambler player) {
        player.incrementBank(this.bet);
        this.bet = 0;
    }

    public Card getCard(Deck deck) {
        Card drawn_card = deck.draw();
        cards.add(drawn_card);
        if (drawn_card.getValue() == 1) {
            this.ace_counter += 1;
        }
        this.setValue(this.calculateHandValue());
        return drawn_card;
    }


    private int calculateHandValue() //working
    {
        int sum; // variable for summing all values in hand
        ArrayList<Integer> value_list = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : this.cards) // extracting all card values and adds them to a list
        {
            if (card.getValue() >= 10) // adds royalty and 10 to value list
            {
                value_list.add(10);

            } else // adds regular numbers including aces
            {
                value_list.add(card.getValue());
            }
        }
        Collections.sort(value_list); // sorting list to decide if ace value is 1 or 11
        for (int value : value_list) // iterating over the values list, has to be sorted to
        {                            //decide if ace is 1 or 11

            if (value != 1) {
                values.add(value);
            } else // Ace cases
            {
                sum = sum(values);
                if (sum + 11 > 21) {
                    values.add(1);
                } else {
                    values.add(11);
                }
            }
        }

        sum = sum(values);

        if (sum == 21) {
            if (this.cards.size() == 2) // BlackJack case
            {
                this.setBlackJack();
            }
            this.setstandFlag();
            return sum;
        }

        if (sum > 21) {
            this.setburnFlag();
            return sum;
        }
        return sum;
    }


    private int sum(List <Integer> list) {
        int summ = 0;
        for (int i : list)
            summ += i;
        return summ;
    }
    public boolean isAlive(){
        return !isburnFlag();
    }

    public void multiplyBet(){
        this.bet *= 2;
    }
    public void multiplyBetBlack(){
        this.bet *= 2.5;
    }



    }


