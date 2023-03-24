package Blackjack.Controller;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IEvent;

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
        dealer.endGame = this::EndGame;
        dealer.getPlayers = ()-> Arrays.asList(player);
        player = new GPlayer(game.FindIGroup("player"));
        player.onReview = dealer::Review;
        player.InitControl(game.FindIGroup("table"),game.FindIGroup("noti"));
        player.InitBet(game.FindIGroup("bet"));
        player.InitInsure(game.FindIGroup("insure"));
        deck.Reset();

        IEvent.SetGameRun("handCount",ia->{
            boolean handCount = Config.GetPref("handCount",false);
            dealer.SetHandCount(handCount);
            player.SetHandCount(handCount);
        });
    }
    private void Reset()
    {
        dealer.Reset();
        player.Reset();
    }
    public void Start()
    {
        Reset();
        player.Bet(()->game.Run(this::DealCard,1f));
    }
    public void DealCard()
    {
        DealCard(dealer,2*2);
    }
    private void DealCard(GBot pl,int count)
    {
        if (count<=0) return;
        GBot pl0 = pl.equals(player)?dealer:player;
        pl0.TakeCard(()->{
            game.Run(()->DealCard(pl0,count-1),0.6f);
        });
    }
    private void EndGame()
    {
        GDX.Log("endGame");
        game.Run(()->deck.ToDeck0(this::NextRound),1f);
    }
    private void NextRound()
    {
        deck.Clear();
        Start();
    }
}
