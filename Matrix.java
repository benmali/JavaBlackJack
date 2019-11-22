package blackjackgame;

public class Matrix {
    private Card dealerCard;
    private Gambler player;
    private int playerHandValue;
    int [] cardValues;

    public Matrix(Gambler player,Dealer dealer){
        this.player = player;
        this.cardValues = getCardValues();
        this.dealerCard = dealer.getFaceUpCard();
        this.playerHandValue = player.getActiveHandValue();

    }
    public int[] getCardValues(){
        int [] cardValues = new int[2];
        cardValues[0] = this.player.getActiveHand().getCards().get(0).getValue();

        cardValues[1] = this.player.getActiveHand().getCards().get(1).getValue();
        if(cardValues[0] >= 10){
            cardValues[0] = 10;
        }
        if(cardValues[1] >= 10){
            cardValues[1] = 10;
        }
        return cardValues;
    }
    public boolean hasAce(){
        int [] cards = getCardValues();
        boolean gotAce = false;
        for( int card: cards){
            if(card == 1){
                gotAce = true;
            }
        }
        return gotAce;
    }
    public int getAceIdx(){
        int counter = 0;
        if(hasAce()){
            int [] cards = getCardValues();
            for(int card:cards){
                if(card == 1){
                    return counter;
                }
                counter ++;
            }
        }
        return 9999;
    }

    public String convertCodeToTip(int actionCode){
        String decision = "";
        switch (actionCode){
            case 1:
                decision = "Hit";
                break;
            case 2:
                decision = "Stand";
                break;
            case 3:
                decision = "Double Down";
                break;
            case 4:
                decision = "Split";
                break;
        }
        return decision;
    }
    
    private int[][] mainStrategyTable = new int[][] {
            //                                Dealers Card Up
            //                0  1  2  3  4  5  6  7  8  9
            /*Your Hand       2  3  4  5  6  7  8  9 10  A   */
            /*<=8*/    /*0*/ {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            /*9*/      /*1*/ {1, 3, 3, 3, 3, 1, 1, 1, 1, 1},
            /*10*/     /*2*/ {3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
            /*11*/     /*3*/ {3, 3, 3, 3, 3, 3, 3, 3, 3, 1},
            /*12*/     /*4*/ {1, 1, 2, 2, 2, 1, 1, 1, 1, 1},
            /*13*/     /*5*/ {2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
            /*14*/     /*6*/ {2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
            /*15*/     /*7*/ {2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
            /*16*/     /*8*/ {2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
            /*17+*/    /*9*/ {2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            /*A2*/     /*10*/{1, 1, 1, 3, 3, 1, 1, 1, 1, 1},
            /*A3*/     /*11*/{1, 1, 1, 3, 3, 1, 1, 1, 1, 1},
            /*A4*/     /*12*/{1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
            /*A5*/     /*13*/{1, 1, 3, 3, 3, 1, 1, 1, 1, 1},
            /*A6*/     /*14*/{3, 3, 3, 3, 3, 1, 1, 1, 1, 1},
            /*A7*/     /*15*/{2, 3, 3, 3, 3, 2, 2, 1, 1, 1},
            /*A8*/     /*16*/{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            /*A9*/     /*17*/{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            /*2-2*/	   /*18*/{4, 4, 4, 4, 4, 4, 1, 1, 1, 1},
            /*3-3*/	   /*19*/{4, 4, 4, 4, 4, 4, 1, 1, 1, 1},
            /*4-4*/	   /*20*/{1, 1, 1, 4, 4, 1, 1, 1, 1, 1},
            /*5-5*/	   /*21*/{3, 3, 3, 3, 3, 3, 3, 3, 1, 1},
            /*6-6*/	   /*22*/{4, 4, 4, 4, 4, 1, 1, 1, 1, 1},
            /*7-7*/	   /*23*/{4, 4, 4, 4, 4, 4, 1, 1, 1, 1},
            /*8-8*/	   /*24*/{4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
            /*9-9*/	   /*25*/{4, 4, 4, 4, 4, 2, 4, 4, 2, 2},
            /*10-10*/  /*26*/{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
            /*A-A*/	   /*27*/{4, 4, 4, 4, 4, 4, 4, 4, 4, 4}};
    /*
     * 1 - Hit     2 - Stand     3 - Double Down     4 - Split
     */

    private int [][] hardHandStrategy = new int[][]{
     //dealer card  2 3 4 5 6 7 8 9 10 A
            /*8*/  {1,1,1,1,1,1,1,1,1,1},
            /*9*/  {1,1,1,1,1,1,1,1,1,1},
            /*10*/ {1,1,1,1,1,1,1,1,1,1},
            /*11*/ {1,1,1,1,1,1,1,1,1,1},
            /*12*/ {1,1,2,2,2,1,1,1,1,1},
            /*13*/ {2,2,2,2,2,1,1,1,1,1},
            /*14*/ {2,2,2,2,2,1,1,1,1,1},
            /*15*/ {2,2,2,2,2,1,1,1,1,1},
            /*16*/ {2,2,2,2,2,1,1,1,1,1},
            /*17*/ {2,2,2,2,2,2,2,2,2,2}
    };

    private int [][] softHandStrategy = new int[][]{

            //dealer card                2 3 4 5 6 7 8 9 10 A
                                /*A,2*/ {1,1,1,1,1,1,1,1,1,1},
                                /*A,3*/ {1,1,1,1,1,1,1,1,1,1},
                                /*A,4*/ {1,1,1,1,1,1,1,1,1,1},
                                /*A,5*/ {1,1,1,1,1,1,1,1,1,1},
                                /*A,6*/ {1,1,1,1,1,1,1,1,1,1},
                                /*A,7*/ {2,2,2,2,2,2,2,1,1,1},
                                /*A,8*/ {2,2,2,2,2,2,2,2,2,2},
                                /*A,9*/ {2,2,2,2,2,2,2,2,2,2}
    };

    public int decisionMatrix() {

            int column = this.dealerCard.getValue();
            int row;
            if(column!=1){ // not Ace - sets variable to index in table
                column -=2;
            }
            else {
                column +=8; //sets to index of ace at idx 9
            }
        if (player.getActiveHand().getCards().size() == 2){
        if (hasAce()) { // soft scenarios
            int aceIdx = getAceIdx();
            int otherCard = this.cardValues[1 - aceIdx];
            if(otherCard!=1){
                row = otherCard+8;
            }
            else{
                row = otherCard+26;
            }
            return mainStrategyTable[row][column];
        }
        if(cardValues[0] == cardValues[1]){ // split
            row =cardValues[0]+ 16; //sets index to split row scenarios
            return mainStrategyTable[row][column];
        }
        else {
            row = this.playerHandValue - 8;
            if (row < 0) {
                row = 0;
            }
            if (row > 9) {
                row = 9;
            }
            return mainStrategyTable[row][column];
        }
        }
            else{ // if player has more than 2 cards in hand
                if(player.getActiveHand().checkIsSoft()){
                    row = player.getActiveHandValue() - 13; // -11 to decrease ace and -2 to get to row 0
                    return softHandStrategy[row][column];
                }
                else{
                    row = player.getActiveHandValue() - 8; // using getHandValue to get updated value
                    if(row<0){
                        row =0;
                    }
                    if(row>9){
                        row =9;
                    }
                    return hardHandStrategy[row][column];
                }
            }

    }
    }

