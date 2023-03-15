package Blackjack.Controller;

import GDX11.IObject.IActor.IGroup;

public class GBoard {
    private GDealer dealer;
    private GPlayer player;
    private IGroup game;

    public GBoard(IGroup game)
    {
        this.game = game;
        new GDeck(game.FindIGroup("deck"));
        dealer = new GDealer(game.FindIGroup("dealer"));
        player = new GPlayer(game.FindIGroup("player"));
    }
    private void Reset()
    {
        GDeck.i.Reset();
        dealer.Reset();
        player.Reset();
    }
    public void Start()
    {
        Reset();
        game.Run(this::DealCard,3);
    }
    public void DealCard()
    {
        DealCard(player,0,2*2,()->player.Turn());
    }
    private void DealCard(GPlayer pl,float delay,int count,Runnable done)
    {
        game.Run(pl::TakeCard,delay);
        if (count<=1) game.Run(done,delay);
        else
            DealCard(pl.equals(player)?dealer:player,delay+1f,count-1,done);
    }
}
