package Blackjack.Controller;

import GDX11.IObject.IActor.IGroup;
import GDX11.Util;


public class GPlayer extends GBot {
    private IGroup iBt,iNoti,iBet;
    private Runnable betDone;
    public GPlayer(IGroup iGroup)
    {
        super(iGroup);
    }

    @Override
    protected GCardSet NewCardSet(IGroup iGroup) {
        GCardSet gCardSet = super.NewCardSet(iGroup);
        gCardSet.event = ev->{
            iNoti.RunAction(ev);
            iNoti.RunAction("noti");
        };
        return gCardSet;
    }

    public void InitControl(IGroup iBt,IGroup iNoti)
    {
        this.iNoti = iNoti;
        this.iBt = iBt;
        iBt.FindIActor("btStand").AddClick(()->{
            GetCardSet().Stand();
            iBt.RunAction("off");
        });
        iBt.FindIActor("btSplit").AddClick(()->{
            GetCardSet().Split();
            iBt.RunAction("off");
        });
        iBt.FindIActor("btHit").AddClick(()->{
            GetCardSet().Hit();
            iBt.RunAction("off");
        });
        iBt.FindIActor("btDouble").AddClick(()->{
            GetCardSet().Double();
            iBt.RunAction("off");
        });
    }
    public void InitBet(IGroup iBet)
    {
        this.iBet = iBet;
        Util.For(0,5,i-> iBet.FindIActor("chip"+i).AddClick(()->Bet(i)));
        iBet.FindIActor("btClear").AddClick(()->{
            betList.clear();
            gSet.Clear();
            iBet.FindActor("table").setVisible(false);
        });
        iBet.FindIActor("btDeal").AddClick(()->{
            iBet.FindActor("table").setVisible(false);
            iBet.RunAction("off");
            betDone.run();
        });
    }
    private void Bet(int index)
    {
        betList.add(index);
        iBet.FindActor("table").setVisible(true);
        gSet.Bet(index);

    }

    @Override
    public void Bet(Runnable done) {
        betDone = done;
        iBet.RunAction("on");
        iBet.FindActor("table").setVisible(betList.size()>0);
        gSet.SetBet(betList);

    }

    public void Reset()
    {
        iBet.RunAction("reset");
        iNoti.RunAction("reset");
        iGroup.FindITable("table").Refresh();
        super.Reset();
        iBt.RunAction("reset");
    }
    protected void InitCardSet(GCardSet cardSet)
    {
        cardSet.onTurn = ()->{
            iBt.RunAction("choice");
            iBt.FindActor("btSplit").setVisible(cardSet.CanSplit());
            iBt.FindActor("btDouble").setVisible(cardSet.CanDouble());
        };
    }
}
