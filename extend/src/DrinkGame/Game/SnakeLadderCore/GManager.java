package DrinkGame.Game.SnakeLadderCore;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.Util;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class GManager {
    private List<VObject> list = new ArrayList<>();
    private IGroup board;
    private Map<Integer,VObject> map = new HashMap<>();
    private final List<Integer> textPosList = GetTextPosList();
    public GManager(IGroup board){
        this.board = board;

        InitTextPos();
        MakeNew();

        GDX.Func1<Integer,Vector2> toSlot = board.FindITable("table").iParam.Get("ToSlot");
        for (VObject ob : list)
            map.put(toSlot.Run(ob.from),ob);
    }
    private void InitTextPos(){
        ITable table = board.FindITable("table");
        GDX.Func1<IActor,Integer> getCell = table.iParam.Get("GetCell");
        for (int slot : textPosList){
            int id = textPosList.indexOf(slot)+1;
            IActor iSlot = getCell.Run(slot);
            iSlot.Run("text"+id);
            iSlot.Run("text");
        }
    }
    private void MakeNew(){
        NewSnake(4,MathUtils.random(1,2));
        NewSnake(5,MathUtils.random(1,2));
        NewSnake(6,MathUtils.random(1,2));
        NewLadder(2,MathUtils.random(2,4));
        NewSnake(7,MathUtils.random(3,5));
        NewLadder(1,MathUtils.random(3,5));
        NewLadder(3,MathUtils.random(2,3));
        Util.Repeat(MathUtils.random(2,3),()->NewSnake(MathUtils.random(1,3),1));
    }
    private void NewLadder(int id,int amount){
        for (int i=0;i<amount;i++){
            IActor iActor = Asset.i.GetObject("ladder"+id).Clone();
            VObject vObject = new VObject(iActor,board.FindITable("table"));
            vObject.check = this::Check;
            if (vObject.Create()) list.add(vObject);
            else vObject.Remove();
        }
    }
    private void NewSnake(int id,int amount){
        for (int i=0;i<amount;i++){
            IActor iActor = Asset.i.GetObject("snake"+id).Clone();
            VObject vObject = new VSnake(iActor,board.FindITable("table"));
            vObject.check = this::Check;
            if (vObject.Create()) list.add(vObject);
            else vObject.Remove();
        }
    }
    private boolean Check(VObject ob){
        if (textPosList.contains(ob.GetSlot())) return false;
        for (VObject i : list)
            if (i.Collision(ob) || ob.Collision(i)) return false;
        return true;
    }
    public VObject GetVObject(int slot){
        return map.get(slot);
    }

    private List<Integer> GetTextPosList(){
        List<Integer> list = new ArrayList<>();
        Util.For(1,98, list::add);
        Collections.shuffle(list);
        return Arrays.asList(list.get(0),list.get(1),list.get(2),list.get(3));
    }
}
