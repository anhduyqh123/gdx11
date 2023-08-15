package GDX11.IObject;

import GDX11.IObject.IActor.IActor;
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
    private Vector2 GetBasePosition(IActor iActor){
        float x0 = iActor.GetGlobalNum(x).floatValue();
        float y0 = iActor.GetGlobalNum(y).floatValue();
        return new Vector2(x0,y0);
    }
    private Vector2 GetBasePosition(){
        return GetBasePosition(GetIActor());
    }
    public Vector2 GetPosition()
    {
        if (coordActor.equals("")) return GetBasePosition();
        if (coordActor.contains(":")){
            String[] arr = coordActor.split(":");
            Vector2 pos0 = GetCoord(arr[0]);
            Vector2 pos1 = GetCoord(arr[1]);
            return new Vector2(pos0.x,pos1.y);
        }
        return GetCoord(coordActor);
    }
    private Vector2 GetCoord(String coord){
        if (coord.equals("")) return GetBasePosition();
        if (coord.equals("stage")) return GetActor().getParent().stageToLocalCoordinates(GetBasePosition());
        IActor coordIActor = GetIActor().IParentFind(coordActor);
        return coordIActor.GetActor().localToActorCoordinates(GetActor().getParent(),GetBasePosition(coordIActor));
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
