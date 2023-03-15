package Blackjack.Model;

import java.util.ArrayList;
import java.util.List;

public class CardSet {

    private List<Card> set = new ArrayList<>();
    public void Add(Card card){
        set.add(card);
    }
    public List<Integer> GetPoints()
    {
        return null;
    }
    public int GetScore()
    {
        return 0;
    }
}
