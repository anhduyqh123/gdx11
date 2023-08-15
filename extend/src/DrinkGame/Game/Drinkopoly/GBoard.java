package DrinkGame.Game.Drinkopoly;

import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class GBoard {
    protected final IGroup board;
    protected final List<GPlayer> players = new ArrayList<>();
    protected final HashSet<Integer> topList = new HashSet<>();
    public GDX.Runnable1<List<Integer>> endGame;
    protected int turnID;
    public GBoard(IGroup board){
        this.board = board;
        Config.i.SetRun1("jumpDone",this::JumpDone);
        board.iParam.Set("CheckAtSlot",(GDX.Runnable1<Integer>)this::CheckAtSlot);
        Config.i.SetRun1("startRoll",a->board.FindIActor("boxes").RunAction("reset"));
        Config.i.SetRun1("rollDone",a->GetPlayer().RollDone());
        board.RunAction("show");
        board.FindIActor("boxes").RunAction("reset");
    }
    protected GPlayer NewGPlayer(int id){
        return new GPlayer(id,board);
    }
    public void InitPlayer(int num){
        Util.For(1,num, id->players.add(NewGPlayer(id)));
    }
    protected GPlayer GetPlayer(){
        return players.get(turnID);
    }
    public void Start(){
        turnID = 0;
        Turn();
    }
    private void Turn(){
        GetPlayer().Turn(this::NextTurn);
    }
    private void NextTurn(){
        if (GetPlayer().IsFinish()) topList.add(GetPlayer().id);
        List<GPlayer> onWay = GetPlayerOnWay();
        if (onWay.size()==1){
            topList.add(onWay.get(0).id);
            endGame.Run(new ArrayList<>(topList));
            return;
        }
        turnID++;
        if (turnID>=players.size()) turnID = 0;
        Turn();
    }
    private List<GPlayer> GetPlayerOnWay(){
        List<GPlayer> list = new ArrayList<>();
        for (GPlayer pl : players)
            if (!pl.IsFinish()) list.add(pl);
        return list;
    }
    protected void JumpDone(IActor iActor){
        GAudio.i.PlaySound(GetPlayer().IsFinish()?"key_taken":"completed");
        GetPlayer().CheckSlot();
        GetPlayer().NextTurn();
    }
    private void CheckAtSlot(int slot){
        List<IActor> list = new ArrayList<>();
        for (GPlayer pl : players)
            if (pl.slot==slot) list.add(pl.GetValue());
        if (list.size()==0) return;
        if (list.size()==1){
            list.get(0).RunAction("sort");
            return;
        }
        Util.For(list, a->a.GetActor().toFront());
        Collections.reverse(list);
        Util.ForIndex(list,i-> list.get(i).RunAction("sort"+i));
    }
}
