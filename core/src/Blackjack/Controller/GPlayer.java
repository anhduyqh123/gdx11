package Blackjack.Controller;

import Extend.XItem;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;


public class GPlayer extends GBot {
    public static Runnable onWin,onLose,onPush;

    private IGroup iBt,iNoti,iBet,iInsure;
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
            iBet.Run("x0");
        });
        iBet.FindIActor("btDeal").AddClick(()->{
            iBet.RunAction("off");
            betDone.run();
        });
    }
    public void InitInsure(IGroup iInsure)
    {
        iInsure.Run("reset");
        this.iInsure = iInsure;
        iInsure.FindIActor("btYes").AddClick(()->{
            gSet.Insure();
            insureDone.Run(true);
            iInsure.RunAction("off");
        });
        iInsure.FindIActor("btNo").AddClick(()->{
            insureDone.Run(false);
            iInsure.RunAction("off");
        });
    }
    private void Bet(int index)
    {
        betList.add(index);
        iBet.Run("x1");
        gSet.Bet(index);

    }

    @Override
    public void Bet(Runnable done) {
        betDone = done;
        iBet.RunAction("on");
        iBet.Run(betList.size()>0?"x1":"x0");
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
            if (Config.GetPref("hint",false)) Hint();
        };
    }
    private void Hint()
    {
        if (GetCardSet().CanSplit())
        {
            iBt.FindIActor("btSplit").RunAction("hint");
            return;
        }
        if (GetCardSet().set.GetScore()<17)
        {
            iBt.FindIActor("btHit").RunAction("hint");
            return;
        }
        iBt.FindIActor("btStand").RunAction("hint");
    }

    @Override
    protected void OnLose(int bet) {
        XItem.Get("money").Add(-bet);
        onLose.run();
    }

    @Override
    protected void OnWin(int bet) {
        XItem.Get("money").Add(bet);
        onWin.run();
    }

    @Override
    protected void OnPush(int bet) {
        onPush.run();
    }

    @Override
    public void OnInsure(GDX.Runnable1<Boolean> done) {
        super.OnInsure(done);
        iInsure.RunAction("choice");
    }
}
