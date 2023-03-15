package Blackjack.Controller;

import Blackjack.Model.Card;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IRunnable;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.util.*;

public class GDeck {
    public static GDeck i;
    private IGroup iGroup;
    private Map<Card, IImage> map = new HashMap<>();
    private List<Card> cards = new ArrayList<>();

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
        iGroup.Refresh();
        Shuffle();
    }
    public void Reset()
    {
        Shuffle();
        iGroup.RunAction("reset");
    }
    private void NewCard(String id)
    {
        Card card = new Card(id);
        IImage view = iGroup.FindIImage("img").Clone();
        view.name = "i"+map.size();
        iGroup.iMap.Add(view);
        view.iRun.SetRun("showCard",()->view.SetTexture(id));
        map.put(card,view);
    }

    private void Shuffle()
    {
        cards.clear();
        cards.addAll(map.keySet());
        Collections.shuffle(cards);
        Util.ForIndex(cards,i->GetView(cards.get(i)).GetActor().setZIndex(i));
    }
    public IImage GetView(Card card)
    {
        return map.get(card);
    }
    public Card PopCard()
    {
        if (cards.size()<=0) return null;
        Card card = cards.get(cards.size()-1);
        cards.remove(card);
        return card;
    }
}
