package GDX11.IObject;

import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

public class IPos extends IBase {
    public String coordActor = "";
    public String x = "0";
    public String y = "0";
    public String align = "";

    private GDX.Func<Map> onChange;

    public IPos(){}
    public IPos(float x,float y){
        this.x = x+"";
        this.y = y+"";
    }

    public void Refresh()
    {
        GetIActor().SetPosition(GetPosition(),GetAlign());
        //new
        AddChangeEvent("point", this::Refresh);
    }
    public Vector2 GetPosition()
    {
        Actor actor = GetIActor().GetActor();
        float x0 = GetIActor().GetParam(x,0f);
        float y0 = GetIActor().GetParam(y,0f);
        Vector2 pos = new Vector2(x0,y0);
        if (coordActor.equals("")) return pos;
        if (coordActor.equals("stage")) return actor.getParent().stageToLocalCoordinates(pos);
        Actor other = GetIActor().IRootFind(coordActor).GetActor();
        return other.localToActorCoordinates(GetIActor().GetActor().getParent(),pos);
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

    //Event
    private void Install()
    {
        Map map = new HashMap();
        onChange = ()->map;
    }
    private Map<String,Runnable> GetEventMap()
    {
        if (onChange==null) Install();
        return onChange.Run();
    }
    public void AddChangeEvent(String name,Runnable cb)
    {
        GetEventMap().put(name,cb);
    }
    public void OnChange()
    {
        Util.For(GetEventMap().values(), Runnable::run);
    }
}
