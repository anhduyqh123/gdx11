package DrinkGame.Game;

import DrinkGame.Base.BaseGame;
import GDX11.Actions.CountAction;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Util;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.ArrayList;
import java.util.List;

public class Hammer extends BaseGame {
    private final IGroup board = game.FindIGroup("board");
    private final ITable table = board.FindITable("table");
    private IActor preIActor;
    private int index = -1,id=-1;
    public Hammer() {
        super("hammer");
        board.Run("reset");

        Config.i.SetRun1("start",a->{
            table.Run("reset");
            int max = MathUtils.random(40,50);
            Action ac = CountAction.Get(f->{
                int i = f.intValue();
                if (i!=index){
                    index = i;
                    Result(index>=max);
                    if (index>=max) board.FindIActor("bt").RunAction("reset");
                }
            },0,max,MathUtils.random(7,10), Interpolation.circleOut);
            a.GetActor().addAction(ac);
        });
    }
    private void Result(boolean done){
        if (preIActor!=null) preIActor.RunAction("off");
        id = NewID();
        IGroup child = table.GetIChild(id);
        GAudio.i.PlaySingleSound("pong",0.02f);
        child.RunAction("on");
        preIActor = child;
        if (done){
            GAudio.i.PlaySound("completed");
            child.RunAction("result");
        }
    }
    private int NewID(){
        List<Integer> list = new ArrayList<>();
        Util.For(0,9,list::add);
        list.remove((Object)id);
        return list.get(MathUtils.random(0,list.size()-1));
    }
}
