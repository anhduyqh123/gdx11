package Blackjack.Controller;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;

import java.util.ArrayList;
import java.util.List;

public class GPlayer extends GBot {
    private IGroup iBt;

    public GPlayer(IGroup iGroup)
    {
        super(iGroup);
    }
    public void InitControl(IGroup iBt)
    {
        this.iBt = iBt;
        iBt.FindIActor("btStand").AddClick(()->{
            GetCardSet().Stand();
        });
        iBt.FindIActor("btSplit").AddClick(()->{
            GetCardSet().Split();
        });
        iBt.FindIActor("btHit").AddClick(()->{
            GetCardSet().Hit();
        });
        iBt.FindIActor("btDouble").AddClick(()->{
            GetCardSet().Double();
        });
    }
    public void Reset()
    {
        super.Reset();
        iBt.RunAction("reset");
    }
}
