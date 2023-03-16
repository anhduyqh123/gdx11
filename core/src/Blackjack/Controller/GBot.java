package Blackjack.Controller;

import GDX11.IObject.IActor.IGroup;

import java.util.ArrayList;
import java.util.List;

public class GBot {
    protected List<GCardSet> gSet = new ArrayList<>();
    protected int iSet = 0;
    protected IGroup iGroup;
    protected Runnable next;
    public GBot(IGroup iGroup)
    {
        this.iGroup = iGroup;
        GCardSet set = new GCardSet(iGroup.FindIGroup("cardSet"));
        gSet.add(set);
    }
    public void Reset()
    {
        iSet=-1;
        iGroup.RunAction("reset");
    }
    public void Turn(Runnable next)
    {
        this.next = next;
        NextCard();
    }
    protected void NextCard()
    {
        iSet++;
        if (iSet>=gSet.size()) NextTurn();
        else GetCardSet().Turn();
    }
    protected void NextTurn()
    {
        next.run();
    }
    public void TakeCard()
    {
        GetCardSet().GetCard();
    }
    protected GCardSet GetCardSet()
    {
        return gSet.get(iSet);
    }
}
