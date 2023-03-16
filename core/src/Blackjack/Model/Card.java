package Blackjack.Model;

import GDX11.GDX;

public class Card {
    public String key;
    public int number;
    public Card(String key)
    {
        this.key = key;
        String[] arr = key.split("_");
        GDX.Try(()->number = Integer.parseInt(arr[0]));
    }
    public int GetScore()
    {
        return number<10?number:10;
    }
}
