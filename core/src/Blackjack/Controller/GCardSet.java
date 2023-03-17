package Blackjack.Controller;

import Blackjack.Model.Card;
import Blackjack.Model.CardSet;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Scene;

public class GCardSet {
    public CardSet set = new CardSet();
    public IGroup iGroup;
    private Runnable next;
    public Runnable onTurn = ()->{},onReview;
    public GDX.Runnable1<String> event;
    public GDX.Func<GCardSet> newGCardSet;
    public boolean reviewed;

    public GCardSet(IGroup iGroup)
    {
        this.iGroup = iGroup;
    }
    public void Turn(Runnable next)
    {
        iGroup.FindIActor("score").RunAction("on");
        this.next = next;
        if (set.Size()<2) GetCard(()->Turn(next));
        else {
            if (set.IsBlackjack()) OnEvent("blackjack");
            else onTurn.run();
        }
    }
    public void GetCard()
    {
        GetCard(null);
    }
    public void GetCard(boolean showCard)
    {
        GetCard(showCard,null);
    }
    public void GetCard(Runnable done)
    {
        GetCard(true,done);
    }
    public void GetCard(boolean showCard,Runnable done)
    {
        Card card = GDeck.i.PopCard();
        IActor view = GDeck.i.GetView(card);
        set.Add(card);
        Scene.AddActorKeepTransform(view.GetActor(),iGroup.FindActor("gr"));
        view.iRun.SetRun("moveLocal", ()->{
            if (showCard) GDeck.i.GetView(card).RunAction("upCard");
            MoveDone();
            if (done!=null) done.run();
        });
        view.RunAction("moveLocal");
    }
    protected void MoveDone()
    {
        iGroup.FindILabel("lb").SetText(set.GetViewScore());
        set.For(c->GDeck.i.GetView(c).RunAction("refreshPos"));
    }
    public boolean CanSplit()
    {
        return true;
        //return set.CanSplit();
    }
    public boolean CanDouble()
    {
        return set.Size()==2;
    }
    public void Split()
    {
        Split0(()->{
            GCardSet newSet = newGCardSet.Run();
            newSet.set.Add(set.Pop());
            RefreshCards();
            newSet.RefreshCards();
            newSet.RunAction("gray");
            iGroup.Run(()->Turn(next),1f);
        });
    }
    public void Stand()
    {
        iGroup.FindILabel("lb").SetText(set.GetScore());
        next.run();
    }
    public void Hit()
    {
        GetCard();
        iGroup.Run(()->{
            int score = set.GetScore();
            if (score>21) OnEvent("bust");
            if (score==21) Stand();
            if (score<21) Turn(next);
        },1f);
    }
    public void Double()
    {
        GetCard();
        iGroup.Run(()->{
            int score = set.GetScore();
            if (score>21) OnEvent("bust");
            else Stand();
        },1f);
    }
    private void OnEvent(String name)
    {
        event.Run(name);
        iGroup.Run(()->{
            onReview.run();
            next.run();
        },1.6f);
    }

    //Exx
    private void Split0(Runnable done)
    {
        RunAction("split0");
        iGroup.Run(done,0.1f);
    }
    private void RefreshCards()
    {
        iGroup.FindILabel("lb").SetText(set.GetViewScore());
        set.For(c->{
            Scene.AddActorKeepTransform(GDeck.i.GetView(c).GetActor(),iGroup.FindActor("gr"));
            GDeck.i.GetView(c).RunAction("refreshPos");
        });
    }
    public void RunAction(String color)
    {
        set.For(c->GDeck.i.GetView(c).RunAction(color));
    }

    //Event
    public void Push()
    {
        reviewed = true;
        event.Run("push");
    }
    public void Win()
    {
        reviewed = true;
        event.Run("won");
    }
    public void Lose()
    {
        reviewed = true;
        event.Run("dealer_win");
    }
}
