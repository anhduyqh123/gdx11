package DrinkGame.Game.SnakeLadderCore;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.*;

public class GBoard {
    private IGroup board;
    private GManager manager;
    private List<GPlayer> players = new ArrayList<>();
    private final Map<Integer,IActor> cellMap = new HashMap<>();
    private int turnID;
    private final HashSet<Integer> topList = new HashSet<>();
    public GDX.Runnable1<List<Integer>> endGame;
    public GBoard(IGroup board){
        this.board = board;
        board.FindIGroup("dice").FindIActor("value").
                iParam.SetRun("rollDone",()->GetPlayer().RollDone());
        board.iParam.SetRun("jumpDone",this::JumpDone);
        InitTable();
        manager = new GManager(board);
        Util.For(1,4,i->board.FindActor("player"+i).setVisible(false));
    }
    private void InitTable(){
        ITable table = board.FindITable("table");
        Util.For(0,99,i->{
            int x = i%10;
            int y = i/10;
            int x0 = y%2==0?x:9-x;
            int id = (9-y)*10+x0;
            Actor actor = table.GetTable().getChild(id);
            IGroup iSlot = IActor.GetIActor(actor);
            if (i%2==0) iSlot.Run("brown");
            iSlot.FindILabel("lb").SetText(i);
            cellMap.put(i,iSlot);
        });
        table.iParam.Set("CheckAtSlot",(GDX.Runnable1<Integer>)this::CheckAtSlot);
        table.iParam.Set("GetCell",(GDX.Func1<IActor,Integer>) cellMap::get);
        table.iParam.Set("ToSlot",(GDX.Func1<Integer,Vector2>) this::ToSlot);
    }
    private int ToSlot(Vector2 pos){
        int x = (int)pos.x;int y = (int) pos.y;
        x = y%2==0?x:9-x;
        return y*10+x;
    }
    private void NewPlayer(int id){
        GPlayer gPlayer = new GPlayer(id,board);
        players.add(gPlayer);
    }
    public void InitPlayer(int num){
        Util.For(1,num, this::NewPlayer);
    }
    public void BotMode(){
        for (GPlayer pl : players)
            if (players.indexOf(pl)>0) pl.BotMode();
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
    private GPlayer GetPlayer(){
        return players.get(turnID);
    }
    private void JumpDone(){
        VObject vObject = manager.GetVObject(GetPlayer().slot);
        if (vObject==null){
            GetPlayer().CheckSlot();
            GetPlayer().NextTurn();
        }
        else vObject.JumpDone(GetPlayer());
    }
    //Table
    private void RefreshView(){
        List<GPlayer> list = new ArrayList<>(players);
        Collections.sort(list,(o1,o2)->{
            if (o1.slot>o2.slot) return -1;
            if (o1.slot<o2.slot) return 1;
            return 0;
        });
        Util.For(list,a->a.actor.toFront());
    }
    private void CheckAtSlot(int slot){
        RefreshView();
        List<IActor> list = new ArrayList<>();
        for (GPlayer pl : players)
            if (pl.slot==slot) list.add(pl.iGroup.FindIActor("value"));
        if (list.size()==0) return;
        if (list.size()==1){
            list.get(0).RunAction("sort");
            return;
        }
        Util.For(list,a->a.GetActor().toFront());
        Collections.reverse(list);
        Util.ForIndex(list,i->list.get(i).RunAction("sort"+i));
    }
}
