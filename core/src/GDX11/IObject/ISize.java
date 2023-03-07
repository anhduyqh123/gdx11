package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ISize {
    public String width = "0";
    public String height = "0";
    public String scale = "1";//1 or 1:1
    public String rotate = "0";
    public String origin = "";

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
        Actor actor = GetIActor().GetActor();
        SetSize(actor);
        SetOrigin(actor);
        SetScale(actor);
        actor.setRotation(GetIActor().GetValue(rotate));
    }
    private void SetSize(Actor actor)
    {
        SetWidth(actor);
        SetHeight(actor);
    }
    private void SetWidth(Actor actor)
    {
        if (SetWidthByChild(actor)) return;
        actor.setWidth(GetIActor().GetValue(width));
    }
    private void SetHeight(Actor actor)
    {
        if (SetHeightByChild(actor)) return;
        actor.setHeight(GetIActor().GetValue(height));
    }
    private boolean SetWidthByChild(Actor actor)
    {
        if (GetIActor() instanceof IGroup){
            IActor child = GetIActor().GetIGroup().FindIActor(width);
            if (child==null) return false;
            child.iSize.SetWidth(actor);
            return true;
        }
        return false;
    }
    private boolean SetHeightByChild(Actor actor)
    {
        if (GetIActor() instanceof IGroup){
            IActor child = GetIActor().GetIGroup().FindIActor(height);
            if (child==null) return false;
            child.iSize.SetHeight(actor);
            return true;
        }
        return false;
    }
    private void SetOrigin(Actor actor)
    {
        if (origin.contains(";"))
        {
            String[] arr = origin.split(";");
            actor.setOriginX(GetIActor().GetValue(arr[0]));
            actor.setOriginY(GetIActor().GetValue(arr[1]));
        }
        else actor.setOrigin(IParam.GetAlign(origin));
    }
    private void SetScale(Actor actor)
    {
        if (scale.contains(";"))
        {
            String[] arr = origin.split(";");
            actor.setScaleX(GetIActor().GetValue(arr[0]));
            actor.setScaleY(GetIActor().GetValue(arr[1]));
        }
        else actor.setScale(GetIActor().GetValue(scale));
    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
