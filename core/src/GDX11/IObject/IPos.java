package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPos {
    public String coordinatesActor = "";
    public String x = "0";
    public String y = "0";
    public String align = "";
    protected GDX.Func<IActor> getIActor;
    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public IActor GetIActor()
    {
        return getIActor.Run();
    }
    public void Refresh()
    {
        GetIActor().SetPosition(GetPosition(),GetAlign());
    }
    public Vector2 GetPosition()
    {
        Actor actor = GetIActor().GetActor();
        float x0 = GetIActor().iParam.GetValueFromString(x);
        float y0 = GetIActor().iParam.GetValueFromString(y);
        Vector2 pos = new Vector2(x0,y0);
        if (coordinatesActor.equals("")) return pos;
        if (coordinatesActor.equals("stage")) return actor.getParent().stageToLocalCoordinates(pos);
        Actor other = GetIActor().IRootFind(coordinatesActor).GetActor();
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

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
