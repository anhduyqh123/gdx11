package Extend;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IAction.IAction;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Map;

public class IPutEvent extends IAction {
    public enum Type{
        Local,
        Global
    }
    public boolean func = false;
    public String value = "";
    public Type type = Type.Local;
    private transient Map<String,Object> map;

    private void InitMap()
    {
        if (type==Type.Local) map = GetIActor().iParam.GetData();
        else map = Config.map;
    }

    @Override
    public void Run() {
        InitMap();
        switch (value)
        {
            case "pos":
                GDX.Func<Object> fc = ()->new Vector2(GetActor().getX(),GetActor().getY());
                map.put(name, func ?fc:fc.Run());
                break;
            case "size":
                fc = ()-> new Vector2(GetActor().getWidth(),GetActor().getHeight());
                map.put(name, func ?fc:fc.Run());
                break;
            case "bound":
                fc = ()-> new GDX.Vector4(GetActor().getX(),GetActor().getY(),GetActor().getWidth(),GetActor().getHeight());
                map.put(name, func ?fc:fc.Run());
                break;
            case "x":
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getX():GetActor().getX());
                break;
            case "y":
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getY():GetActor().getY());
                break;
            case "width":
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getWidth());
                break;
            case "height":
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getHeight());
                break;
            case "screenshot":
                map.put(name, Util.GetScreenshot());
                break;
            case "buffer":
                map.put(name, Util.GetTextureRegion(GetActor()));
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
