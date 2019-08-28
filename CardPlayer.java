package blackjackgame;

public abstract class CardPlayer {
    abstract protected Hand getActiveHand();
    abstract public boolean shouldStop();

    public int getActiveHandValue(){
        Hand activeHand = this.getActiveHand();
        return activeHand.getValue();
    }

}
