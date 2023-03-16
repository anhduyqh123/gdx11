package Blackjack.Controller;

import Blackjack.Model.Card;
import Blackjack.Model.CardSet;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Scene;

public class GCardSet {
    private CardSet set = new CardSet();
    private IGroup iGroup;

    public GCardSet(IGroup iGroup)
    {
        this.iGroup = iGroup;
    }
    public void Turn()
    {
        iGroup.FindIActor("score").RunAction("on");
    }
    public void GetCard()
    {
        Card card = GDeck.i.PopCard();
        IActor view = GDeck.i.GetView(card);
        set.Add(card);
        Scene.AddActorKeepTransform(view.GetActor(),iGroup.FindActor("gr"));
        view.iRun.SetRun("moveLocal", ()-> MoveDone(card));
        view.RunAction("moveLocal");
    }
    private void MoveDone(Card card)
    {
        iGroup.FindILabel("lb").SetText(set.GetViewScore());
        GDeck.i.GetView(card).RunAction("upCard");
        set.For(c->GDeck.i.GetView(c).RunAction("refreshPos"));
    }
    public boolean CanSplit()
    {
        return set.CanSplit();
    }
    public void Split()
    {

    }
    public void Stand()
    {

    }
    public void Hit()
    {

    }
    public void Double()
    {

    }
}
