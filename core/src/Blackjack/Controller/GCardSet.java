package Blackjack.Controller;

import Blackjack.Model.CardSet;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.Scene;
import GDX11.Util;

import java.util.ArrayList;
import java.util.List;

public class GCardSet {
    private static final int[] coins = {10,20,50,100,200,500};

    public CardSet set = new CardSet();
    public IGroup iGroup;
    private Runnable next;
    public Runnable onTurn = ()->{},onReview;
    public GDX.Runnable1<String> event;
    public GDX.Runnable1<Integer> onWin,onLose,onPush;
    public GDX.Func<GCardSet> newGCardSet;
    public GDX.Func<Boolean> canSlit;
    public boolean reviewed;
    public int bet = 0;

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
        GDeck.i.PopCard(card->{
            IActor view = GDeck.i.GetView(card);
            set.Add(card);
            Scene.AddActorKeepTransform(view.GetActor(),iGroup.FindActor("gr"));
            view.iRun.SetRun("moveLocal", ()->{
                if (showCard) GDeck.i.GetView(card).RunAction("upCard");
                MoveDone();
                if (done!=null) done.run();
            });
            view.RunAction("moveLocal");
        });
    }
    protected void MoveDone()
    {
        iGroup.FindILabel("lb").SetText(set.GetViewScore());
        set.For(c->GDeck.i.GetView(c).RunAction("refreshPos"));
    }
    public boolean CanSplit()
    {
        //return true;
        return canSlit.Run() && set.CanSplit();
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
        IGroup myBet2 = iGroup.FindITable("table").Clone(0);
        myBet2.name = "clone";
        iGroup.FindITable("table").iMap.Add(myBet2);
        myBet2.Refresh();
        myBet2.Run("pos");
        myBet2.FindActor("img").remove();

        iGroup.FindITable("table").RefreshLayout();

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
        set.For(c->GDeck.i.GetView(c).Run(color));
    }
    public void SetBet(List<Integer> list)
    {
        bet=0;
        Clear();
        Util.For(list,i->NewCoin(i,"pos"));
        iGroup.FindIGroup("table").RunAction("rebet");
        RefreshBetText();
    }
    public void Bet(int index)
    {
        NewCoin(index,"drop");
        RefreshBetText();
    }
    private void RefreshBetText()
    {
        iGroup.FindIGroup("myBet").FindILabel("lb").text = bet+"";
        iGroup.FindIGroup("myBet").FindILabel("lb").RefreshContent();
    }
    private IImage NewCoin(int index,String event)
    {
        bet+=coins[index];
        IImage clone = iGroup.FindIGroup("myBet").Clone(0);
        clone.texture = "chip_"+(index+1);
        clone.name = "clone";
        iGroup.FindIGroup("myBet").iMap.Add(clone);
        clone.Refresh();
        clone.RunAction(event);
        return clone;
    }
    public void Clear()
    {
        iGroup.FindIGroup("table").iMap.Remove("clone");
        bet = 0;
        List<IActor> list = new ArrayList<>();
        iGroup.FindIGroup("myBet").ForIChild(ia->{
            if (ia.name.equals("clone")) list.add(ia);
        });
        Util.For(list,ia->iGroup.FindIGroup("myBet").iMap.Remove(ia));
        iGroup.FindIGroup("table").Refresh();
        RefreshBetText();
    }

    //Event
    public void Push()
    {
        reviewed = true;
        event.Run("push");
        iGroup.RunAction("push");
        onPush.Run(bet);
    }
    public void Win()
    {
        reviewed = true;
        event.Run("won");
        iGroup.RunAction("win");
        OnWin();
        onWin.Run(bet);
    }
    public void Lose()
    {
        reviewed = true;
        event.Run("dealer_win");
        iGroup.RunAction("lose");
        onLose.Run(bet);
    }
    private void OnWin()
    {
        IGroup clone = iGroup.Clone("table");
        clone.Refresh();
        clone.FindIGroup("myBet").FindActor("img").remove();
        if (clone.iMap.Size()==2)
            clone.FindIGroup("clone").FindActor("img").remove();
        clone.Run("pos");
        clone.RunAction("win0");
    }
}
