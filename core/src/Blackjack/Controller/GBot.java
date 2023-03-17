package Blackjack.Controller;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;

import java.util.ArrayList;
import java.util.List;

public class GBot {
    protected List<GCardSet> gSetList = new ArrayList<>();
    protected int iSet = 0;
    protected IGroup iGroup;
    protected Runnable next;
    public GDX.Runnable1<GCardSet> onReview;

    public GBot(IGroup iGroup)
    {
        this.iGroup = iGroup;
    }
    protected void InitSet()
    {
        gSetList.clear();
        GCardSet set = NewCardSet(iGroup.FindIGroup("cardSet"));
        set.newGCardSet = ()->{
            GCardSet newSet = NewCardSet(iGroup.FindITable("table").NewChildFrom(0));
            gSetList.add(newSet);
            return newSet;
        };
        gSetList.add(set);
    }
    protected GCardSet NewCardSet(IGroup iGroup)
    {
        GCardSet gCardSet = new GCardSet(iGroup);
        gCardSet.onReview = ()->onReview.Run(gCardSet);
        return gCardSet;
    }
    public void Reset()
    {
        InitSet();
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
        if (iSet>=0) GetCardSet().RunAction("gray");
        iSet++;
        if (iSet>= gSetList.size()){
            for (GCardSet set : gSetList){
                if (!set.reviewed)
                    set.RunAction("white");
            }
            NextTurn();
        }
        else{
            InitCardSet(GetCardSet());
            GetCardSet().RunAction("white");
            GetCardSet().Turn(this::NextCard);
        }
    }
    protected void InitCardSet(GCardSet cardSet)
    {
    }
    protected void NextTurn()
    {
        next.run();
    }
    public void TakeCard()
    {
        gSetList.get(0).GetCard();
    }
    protected GCardSet GetCardSet()
    {
        return gSetList.get(iSet);
    }
    public void EndGame()
    {
        Util.For(gSetList,set->set.RunAction("white"));
    }
}
