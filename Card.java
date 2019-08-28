package blackjackgame;

public class Card {
    String color;
    int value;

    public Card(String type, int value)
    {
        this.color = type;
        this.value = value;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}