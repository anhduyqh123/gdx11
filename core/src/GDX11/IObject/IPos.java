package GDX11.IObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPos extends IBase {
    public String coordActor = "";
    public String x = "0";
    public String y = "0";
    public String align = "";

    public IPos(){}
    public IPos(float x,float y){
        this.x = x+"";
        this.y = y+"";
    }

    public void Refresh()
    {
        GetIActor().SetPosition(GetPosition(),GetAlign());
        //new
        //Reflect.AddEvent(this,"iPos",vl->Refresh());
    }
    public Vector2 GetPosition()
    {
        float x0 = GetIActor().GetGlobalNum(x).floatValue();
        float y0 = GetIActor().GetGlobalNum(y).floatValue();
        Vector2 pos = new Vector2(x0,y0);
        if (coordActor.equals("")) return pos;
        Actor actor = GetIActor().GetActor();
        if (coordActor.equals("stage")) return actor.getParent().stageToLocalCoordinates(pos);
        Actor other = GetIActor().IRootFind(coordActor).GetActor();
        return other.localToActorCoordinates(actor.getParent(),pos);
    }
    public void SetPosition(Vector2 pos)//Align always bottom Left
    {
        x = pos.x+"";
        y = pos.y+"";
    }
    public int GetAlign()
    {
        return IParam.GetAlign(align);
    }
}
