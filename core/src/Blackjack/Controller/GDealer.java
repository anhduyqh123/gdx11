package Blackjack.Controller;

import Blackjack.Model.Card;
import Blackjack.Model.CardSet;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;

import java.util.ArrayList;
import java.util.List;

public class GDealer extends GBot{
    private CardSet model;
    public GDX.Func<List<GBot>> getPlayers;
    private List<GCardSet> setList = new ArrayList<>();
    public Runnable doneCheck,endGame;
    public GDealer(IGroup iGroup) {
        super(iGroup);
    }

    @Override
    protected void InitSet() {
        super.InitSet();
        model = gSet.set;
    }

    @Override
    public void TakeCard(Runnable done) {
        if (model.Size()==0) gSet.GetCard(done);
        else gSet.GetCard(false,()->{
            done.run();
            Card card0 = model.Get(0);
            if (card0.GetScore()>1 && card0.GetScore()<10)
            {
                Check();
                return;
            }
            Card card1 = model.Get(1);
            GDeck.i.GetView(card1).iEvent.SetRun("check",this::Check);
            iGroup.Run(()->GDeck.i.GetView(card1).RunAction("check"),1f);
        });
    }
    private void Check()
    {
        if (model.IsBlackjack()) Turn();
        else doneCheck.run();
    }

    @Override
    public void Turn(Runnable next) {
        super.Turn(next);
        Turn();
    }
    private void Turn()
    {
        Card card1 = model.Get(1);
        GDeck.i.GetView(card1).RunAction("upCard");
        LoadSetList();
        if (setList.size()==0) next.run();
        else iGroup.Run(this::CheckReview,1f);
    }
    private void CheckReview()
    {
        if (model.GetScore()<17) GetCardSet().GetCard(()->iGroup.Run(this::CheckReview,0.8f));
        else Review();
    }
    private void LoadSetList()
    {
        setList.clear();
        Util.For(getPlayers.Run(),p->{
            for (GCardSet set : p.gSetList)
                if (!set.reviewed) setList.add(set);
        });
    }
    private void Review()
    {
        float time = 2f;
        Util.For(setList,set->set.RunAction("gray"));
        Util.ForIndex(setList,i-> iGroup.Run(()->{
            Review(setList.get(i));
            iGroup.Run(()->setList.get(i).RunAction("gray"),1f);
        },i*time));
        iGroup.Run(this::End,setList.size()*time+1f);
    }
    public void Review(GCardSet gSet)
    {
        gSet.RunAction("white");
        if (BlackJackCase(gSet)) return;
        if (BustCase(gSet)) return;
        int score0 = model.GetScore();
        int score1 = gSet.set.GetScore();
        if (score0==score1) gSet.Push();
        if (score0>score1) gSet.Lose();
        if (score0<score1) gSet.Win();
    }
    private boolean BlackJackCase(GCardSet gSet)
    {
        boolean blackjack0 = model.IsBlackjack();
        boolean blackjack1 = gSet.set.IsBlackjack();
        if (!blackjack0 && !blackjack1) return false;
        if (blackjack0 && blackjack1) gSet.Push();
        if (blackjack0) gSet.Lose();
        if (blackjack1) gSet.Win();
        return true;
    }
    private boolean BustCase(GCardSet gSet)
    {
        boolean bust0 = model.Bust();
        boolean bust1 = gSet.set.Bust();
        if (!bust0 && !bust1) return false;
        if (bust0 && bust1) gSet.Push();
        if (bust0) gSet.Win();
        if (bust1) gSet.Lose();
        return true;
    }
    private void End()
    {
        Util.For(getPlayers.Run(), GBot::EndGame);
        endGame.run();
    }
}
