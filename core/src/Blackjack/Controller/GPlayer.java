package Blackjack.Controller;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;

import java.util.ArrayList;
import java.util.List;

public class GPlayer {
    private List<GCardSet> gSet = new ArrayList<>();
    private int iSet = 0;
    protected IGroup iGroup;

    public GPlayer(IGroup iGroup)
    {
        this.iGroup = iGroup;
        GCardSet set = new GCardSet(iGroup.FindIGroup("cardSet"));
        gSet.add(set);
    }
    public void Reset()
    {

    }
    public void Turn()
    {

        GDX.Log("turn");
    }
    public void TakeCard()
    {
        GetCardSet().GetCard();
    }
    private GCardSet GetCardSet()
    {
        return gSet.get(iSet);
    }
}
