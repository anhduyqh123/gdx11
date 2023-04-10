package GDX11.IObject;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Reflect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ISize extends IBase {
    public String width = "0";
    public String height = "0";
    public String scale = "1";//1 or 1:1
    public String rotate = "0";
    public String origin = "";//center or 100:100

    public void Refresh()
    {
        SetSize();
        SetOrigin(origin);
        SetScale(scale);
        SetRotation(rotate);
        Reflect.AddEvent(this,"iSize",vl->Refresh());
    }
    private void SetSize()
    {
        SetWidth(GetActor());
        SetHeight(GetActor());
    }
    private void SetWidth(Actor actor)
    {
        if (SetWidthByChild(actor)) return;
        actor.setWidth(GetIActor().GetParam(width,0f));
    }
    private void SetHeight(Actor actor)
    {
        if (SetHeightByChild(actor)) return;
        actor.setHeight(GetIActor().GetParam(height,0f));
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
    public void SetOrigin(String origin)
    {
        if (origin.contains(":"))
        {
            String[] arr = origin.split(":");
            GetActor().setOriginX(GetIActor().GetParam(arr[0],0f));
            GetActor().setOriginY(GetIActor().GetParam(arr[1],0f));
        }
        else GetActor().setOrigin(IParam.GetAlign(origin));
    }
    public void SetScale(String scale)
    {
        Vector2 sl = GetScale(scale);
        GetActor().setScale(sl.x,sl.y);
    }
    public Vector2 GetScale(String scale)
    {
        Vector2 v = new Vector2();
        if (scale.contains(":"))
        {
            String[] arr = scale.split(":");
            v.set(GetIActor().GetParam(arr[0],0f),GetIActor().GetParam(arr[1],0f));
        }
        else{
            float x = GetIActor().GetParam(scale,0f);
            v.set(x,x);
        }
        return v;
    }
    public void SetRotation(String rotate)
    {
        GetActor().setRotation(GetIActor().GetParam(rotate,0f));
    }
}
