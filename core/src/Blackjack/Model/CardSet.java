package Blackjack.Model;

import GDX11.GDX;
import GDX11.Util;

import java.util.ArrayList;
import java.util.List;

public class CardSet {

    private List<Card> set = new ArrayList<>();
    public void Add(Card card){
        set.add(card);
    }
    public Card Pop()
    {
        Card card = set.get(set.size()-1);
        set.remove(card);
        return card;
    }
    private int BaseScore()
    {
        if (IsBlackjack()) return 21;
        int score = 0;
        for (Card c : set) score+=c.GetScore();
        return score;
    }
    public int GetScore()
    {
        if (!HasA()) return BaseScore();
        int score1 = BaseScore();
        int score2 = score1+10;
        return score2>21?score1:score2;
    }
    public int Size()
    {
        return set.size();
    }
    public void For(GDX.Runnable1<Card> cb)
    {
        Util.For(set,cb);
    }
    public Card Get(int index)
    {
        return set.get(index);
    }
    public String GetViewScore()
    {
        if (!HasA()) return BaseScore()+"";
        int score1 = BaseScore();
        int score2 = score1+10;
        if (score2>21) return score1+"";
        return score1+"/"+score2;
    }
    private boolean HasA()
    {
        for (Card c : set)
            if (c.number==1) return true;
        return false;
    }
    public boolean CanSplit()
    {
        if (set.size()!=2) return false;
        return Get(0).GetScore()==Get(1).GetScore();
    }
    public boolean IsBlackjack()
    {
        if (set.size()!=2) return false;
        int score1 = Get(0).GetScore();
        int score2 = Get(1).GetScore();
        int min = Math.min(score1,score2);
        int max = Math.max(score1,score2);
        return min==1 && max==10;
    }
    public boolean Bust()
    {
        return GetScore()>21;
    }
}
