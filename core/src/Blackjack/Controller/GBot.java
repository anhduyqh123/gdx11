package Blackjack.Controller;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;

import java.util.ArrayList;
import java.util.List;

public class GBot {
    protected List<GCardSet> gSetList = new ArrayList<>();
    protected GCardSet gSet;
    protected int iSet = 0;
    protected IGroup iGroup;
    protected Runnable next;
    public GDX.Runnable1<GCardSet> onReview;
    protected List<Integer> betList = new ArrayList<>();

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
            newSet.SetBet(betList);
            gSetList.add(newSet);
            return newSet;
        };
        gSetList.add(set);
        gSet = gSetList.get(0);
    }
    protected GCardSet NewCardSet(IGroup iGroup)
    {
        GCardSet gCardSet = new GCardSet(iGroup);
        gCardSet.canSlit = ()->gSetList.size()<2;
        gCardSet.onReview = ()->onReview.Run(gCardSet);
        gCardSet.onWin = this::OnWin;
        gCardSet.onLose = this::OnLose;
        gCardSet.onPush = this::OnPush;
        return gCardSet;
    }
    public void Reset()
    {
        InitSet();
        iSet=-1;
        iGroup.RunAction("reset");
    }
    public void Bet(Runnable done)
    {

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
    public void TakeCard(Runnable done)
    {
        gSetList.get(0).GetCard(done);
    }
    protected GCardSet GetCardSet()
    {
        return gSetList.get(iSet);
    }
    public void EndGame()
    {
        Util.For(gSetList,set->set.RunAction("white"));
    }
    protected void OnWin(int bet)
    {

    }
    protected void OnLose(int bet)
    {

    }
    protected void OnPush(int bet)
    {

    }
}
