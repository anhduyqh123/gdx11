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
        float x0 = GetIActor().iParam.GetValueFromString(x);
        float y0 = GetIActor().iParam.GetValueFromString(y);
        Vector2 pos = new Vector2(x0,y0);
        int al = IParam.GetAlign(align);

        if (coordinatesActor.equals("")) GetIActor().SetPosition(pos,al);
        else {
            if (coordinatesActor.equals("stage")) GetIActor().SetStagePosition(pos,al);
            else {
                Actor other = GetIActor().IRootFind(coordinatesActor).GetActor();
                other.localToActorCoordinates(GetIActor().GetActor().getParent(),pos);
                GetIActor().SetPosition(pos,al);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
