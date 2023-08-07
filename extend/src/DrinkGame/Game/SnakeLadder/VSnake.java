package DrinkGame.Game.SnakeLadder;

import GDX11.Actions.MovePath;
import GDX11.GAudio;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IComponent.IShape.IPoints;
import GDX11.Util;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class VSnake extends VObject{
    public VSnake(IActor ob, ITable table) {
        super(ob, table);
    }
    protected void SetDirect(int id){
        Vector2 pos = table.Get(from).GetLocalPosition(Align.center);
        table.Get(from).GetActor().localToActorCoordinates(grBoard,pos);
        ob.SetPosition(pos, Align.topLeft);
        ob.Run("direct"+ id);
        shape = new Polygon(Util.GetVertices(GetIShape().GetStagePoints()));
    }
    public void JumpDone(GPlayer gPlayer){
        Actor actor = gPlayer.actor;
        IPoints points = ob.iComponents.GetIComponent("path");
        MovePath acPath = MovePath.Get(points.GetPoints(actor.getParent()),Align.bottom,0.1f*points.list.size());
        acPath.isRotate = false;
        Action done = Actions.run(()-> gPlayer.JumpTo(ToSlot(to)));
        actor.addAction(Actions.sequence(acPath,done));
        GAudio.i.PlaySound("snake_roll");
    }
}
