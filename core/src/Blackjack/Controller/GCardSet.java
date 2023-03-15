package Blackjack.Controller;

import Blackjack.Model.Card;
import Blackjack.Model.CardSet;
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
    public void GetCard()
    {
        Card card = GDeck.i.PopCard();
        IActor view = GDeck.i.GetView(card);
        set.Add(card);
        Scene.AddActorKeepTransform(view.GetActor(),iGroup.GetGroup());
        view.RunAction("moveLocal");
    }
}
