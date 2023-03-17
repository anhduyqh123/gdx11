package Blackjack.Controller;

import GDX11.IObject.IActor.IGroup;


public class GPlayer extends GBot {
    private IGroup iBt;
    private IGroup iNoti;

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
    public void Reset()
    {
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
