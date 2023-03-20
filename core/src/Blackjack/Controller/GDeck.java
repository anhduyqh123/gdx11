package Blackjack.Controller;

import Blackjack.Model.Card;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IRunnable;
import GDX11.Scene;
import GDX11.Util;

import java.util.*;

public class GDeck {
    public static GDeck i;
    private IGroup iGroup;
    private Map<Card, IImage> map = new HashMap<>();
    private List<Card> cards = new ArrayList<>();
    public List<Card> popCards = new ArrayList<>();
    private Card yellow;

    public GDeck(IGroup iGroup)
    {
        i = this;
        this.iGroup = iGroup;

        Util.For(1,13, i->{
            NewCard(i+"_club");
            NewCard(i+"_diamond");
            NewCard(i+"_heart");
            NewCard(i+"_spade");
        });
        yellow = NewCard("card_yel");
        iGroup.Refresh();
        iGroup.FindActor("img").setVisible(false);
        Shuffle();

        GetView(yellow).SetTexture("card_yel");
        iGroup.iRun.SetRun("startShuffle",this::StartShuffle);
        IRunnable.SetGameRun("cardX",this::AtCard);
    }
    private void AtCard(IActor vCard)
    {
        int index = vCard.GetActor().getZIndex();
        vCard.GetActor().setVisible(index%5==0);
    }
    public void Clear()
    {
        popCards.clear();
    }
    public void Reset()
    {
        popCards.clear();
        Shuffle();

//        Util.Repeat(40,()->{
//            PopCard(card->{
//                GetView(card).Run("deck0");
//            });
//        });
//        Clear();

        iGroup.RunAction("reset");
    }
    private Card NewCard(String id)
    {
        Card card = new Card(id);
        IImage view = iGroup.FindIImage("img").Clone();
        view.name = "i"+map.size();
        iGroup.iMap.Add(view);
        view.iRun.SetRun("showCard",()->view.SetTexture(id));
        map.put(card,view);
        return card;
    }

    private void Shuffle()
    {
        cards.clear();
        cards.addAll(map.keySet());
        Collections.shuffle(cards);
        cards.remove(yellow);
        cards.add(10,yellow);
        Util.ForIndex(cards,i->GetView(cards.get(i)).GetActor().setZIndex(i));
    }
    public IImage GetView(Card card)
    {
        return map.get(card);
    }
    public void PopCard(GDX.Runnable1<Card> cb)
    {
        if (cards.size()<=0) return;
        Card card = cards.get(cards.size()-1);
        cards.remove(card);
        if (card.equals(yellow))
        {
            GetView(yellow).RunAction("yellow");
            iGroup.Run(()->PopCard(cb),1f);
            return;
        }
        popCards.add(card);
        GetView(card).GetActor().setVisible(true);
        cb.Run(card);
    }

    public void ToDeck0(GDX.Runnable done)
    {
        Util.ForIndex(popCards,i->{
            Card card = popCards.get(i);
            GetView(card).Run("deck0");
            iGroup.Run(()-> GetView(card).RunAction("toDeck0"),i*0.2f);
        });
        iGroup.Run(()->CheckReshuffle(done),popCards.size()*0.2f+1f);
        popCards.clear();
    }
    //ShuffleCard
    private void CheckReshuffle(GDX.Runnable done)
    {
        if (cards.contains(yellow)){
            done.Run();
            return;
        }
        iGroup.iRun.SetRun("endShuffle",done);
        ToDeck();
    }
    private void ToDeck()
    {
        cards.clear();
        cards.addAll(map.keySet());
        cards.remove(yellow);

        Util.For(cards,card->{
            GetView(card).Run("deck");
            GetView(card).RunAction("toDeck");
        });
        iGroup.Run(()->iGroup.RunAction("startShuffle"),1f);
    }
    private void StartShuffle()
    {
        DoShuffle(3);
    }
    private void DoShuffle(int count)
    {
        if (count<=0)
        {
            EndShuffle();
            return;
        }
        int mid = cards.size()/2;
        Util.ForIndex(cards,i->{
            Card card = cards.get(i);
            GetView(card).RunAction(i<mid?"left":"right");
        });
        iGroup.Run(()-> {
            Util.ForIndex(cards,i->{
                Card card = cards.get(i);
                GetView(card).RunAction("mid");
            });
            iGroup.Run(()->DoShuffle(count-1),1f);
        },0.6f);
    }
    private void EndShuffle()
    {
        GetView(yellow).Run("deck");
        Shuffle();
        GetView(yellow).RunAction("moveLocal");
        iGroup.Run(()->iGroup.RunAction("endShuffle"),1f);
    }
}
