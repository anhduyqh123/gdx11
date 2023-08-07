package DrinkGame.Game.SnakeLadder;

import GDX11.*;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.IObject.IComponent.IShape.IPolygon;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VObject {
    protected IActor ob;
    protected ITable table;
    public GDX.Func1<Boolean,VObject> check;
    public Vector2 from,to;
    protected int num;
    protected Group grBoard;
    protected Polygon shape;
    public VObject(IActor ob, ITable table){
        this.ob = ob;
        this.table = table;
        num = ob.iParam.Get("num");
        grBoard = table.GetIParent().FindActor("shape");
        ob.SetIRoot(grBoard);
        ob.Refresh();
    }
    protected void SetDirect(int id){
        Vector2 pos = table.Get(from).GetLocalPosition(Align.center);
        table.Get(from).GetActor().localToActorCoordinates(grBoard,pos);
        ob.SetPosition(pos, Align.bottom);
        ob.Run("direct"+ id);
        shape = new Polygon(Util.GetVertices(GetIShape().GetStagePoints()));
    }
    public boolean Create(){
        for (Vector2 p : GetPosList())
            if (Create(p)){
                //shape = new Polygon(Util.GetVertices(GetIShape().GetStagePoints()));
                return true;
            }
        return false;
    }
    private List<Vector2> GetPosList(){
        List<Vector2> list = new ArrayList<>();
        for (int i=0;i<table.column;i++)
            for (int j=0;j<table.column;j++) list.add(new Vector2(i,j));
        list.remove(0);
        Collections.shuffle(list);
        return list;
    }
    private List<Integer> GetIDList(){
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<=num;i++) list.add(i);
        Collections.shuffle(list);
        return list;
    }
    private boolean Create(Vector2 pos0){
        if (ToSlot(pos0)>=99) return false;
        for (int id : GetIDList())
            if (Valid(id,pos0)) return true;
        return false;
    }
    private boolean Valid(int index, Vector2 pos0){
        Vector2 dir = Param.ParseVector(ob.iParam.Get("direct"+index));
        Vector2 pos = dir.add(pos0);
        if (!InBoard(pos)) return false;
        from = pos0;to = pos;
        SetDirect(index);
        if (!ValidShape()) return false;
        if (check.Run(this)) return true;
        return false;
    }
    private boolean ValidShape(){
        Actor a = table.GetActor();
        for (Vector2 p : GetIShape().GetStagePoints()){
            a.stageToLocalCoordinates(p);
            if (p.x<0||p.y<0) return false;
            if (p.x>a.getWidth()||p.y>a.getHeight()) return false;
        }
        return true;
    }
    private boolean InBoard(Vector2 pos){
        if (pos.x>=table.column||pos.y>=table.column) return false;
        if (pos.x<0||pos.y<0) return false;
        return true;
    }
    public boolean Collision(VObject vb){
        if (from.equals(vb.from)) return true;
        if (from.equals(vb.to)) return true;
        for (Vector2 p : vb.GetIShape().GetStagePoints())
            if (shape.contains(p)) return true;
        return false;
    }
    public void Remove(){
        ob.GetActor().remove();
    }
    protected IPolygon GetIShape(){
        return ob.iComponents.GetIComponent("shape");
    }
    public void JumpDone(GPlayer gPlayer){
        Actor actor = gPlayer.actor;
        IPoints points = ob.iComponents.GetIComponent("path");
        Action done = Actions.run(()-> gPlayer.JumpTo(ToSlot(to)));
        actor.addAction(Actions.sequence(GetPathAction(points.GetPoints(actor.getParent())),done));
    }
    protected int ToSlot(Vector2 pos){
        GDX.Func1<Integer,Vector2> toSlot = table.iParam.Get("ToSlot");
        return toSlot.Run(pos);
    }
    private Action GetPathAction(List<Vector2> points){
        SequenceAction se = Actions.sequence();
        Util.For(points,pos->{
            Action ac0 = Actions.moveToAligned(pos.x,pos.y, Align.bottom, 0.2f, Interpolation.fade);
            Action ac1 = Actions.run(()->{
                GAudio.i.PlaySingleSound("drop_ball");
                ob.RunAction("shake");
            });
            se.addAction(ac0);se.addAction(ac1);
        });
        return se;
    }
    public int GetSlot(){
        return ToSlot(from);
    }
}
