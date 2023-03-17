package Blackjack.Controller;

import Blackjack.Model.Card;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;

import java.util.Arrays;

public class GBoard {
    private GDealer dealer;
    private GPlayer player;
    private IGroup game;
    private GDeck deck;

    public GBoard(IGroup game)
    {
        this.game = game;
        deck = new GDeck(game.FindIGroup("deck"));
        dealer = new GDealer(game.FindIGroup("dealer"));
        dealer.doneCheck = ()->player.Turn(()->dealer.Turn(this::EndGame));
        dealer.getPlayers = ()-> Arrays.asList(player);
        player = new GPlayer(game.FindIGroup("player"));
        player.onReview = dealer::Review;
        player.InitControl(game.FindIGroup("table"),game.FindIGroup("noti"));

        deck.Reset();
    }
    private void Reset()
    {
        dealer.Reset();
        player.Reset();
    }
    public void Start()
    {
        Reset();
        game.Run(this::DealCard,1f);
    }
    public void DealCard()
    {
        DealCard(player,0,2*2);
    }
    private void DealCard(GBot pl,float delay,int count)
    {
        if (count<=0) return;
        game.Run(pl::TakeCard,delay);
        DealCard(pl.equals(player)?dealer:player,delay+1f,count-1);
    }
    private void EndGame()
    {
        GDX.Log("endGame");
        game.Run(this::Deck0,1f);
    }
    private void Deck0()
    {
        Util.ForIndex(deck.popCards,i->{
            Card card = deck.popCards.get(i);
            deck.GetView(card).RunAction("deck0");
            game.Run(()-> deck.GetView(card).RunAction("toDeck0"),i*0.2f);
        });
        game.Run(this::NextRound,deck.popCards.size()*0.2f+1f);
    }
    private void NextRound()
    {
        deck.Clear();
        Start();
    }
}
