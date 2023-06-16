package GDX11.IObject.IAction;

import GDX11.Config;
import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Map;

public class IPutParam extends IAction{
    public enum Type{
        Local,
        Global
    }
    public enum Kind{
        Pos,Size,Bound,X,Y,Width,Height,Screenshot,Buffer
    }

    public Type type = Type.Local;
    public boolean func = false;
    public String param = "";
    public Kind kind = Kind.Pos;
    @Override
    public void Run() {
        Map<String,Object> map = type==Type.Local?GetIActor().iParam.GetMap(): Config.i.GetMap();
        switch (kind)
        {
            case Pos:
                GDX.Func<Object> fc = ()->new Vector2(GetActor().getX(),GetActor().getY());
                map.put(name, func ?fc:fc.Run());
                break;
            case Size:
                fc = ()-> new Vector2(GetActor().getWidth(),GetActor().getHeight());
                map.put(name, func ?fc:fc.Run());
                break;
            case Bound:
                fc = ()-> new GDX.Vector4(GetActor().getX(),GetActor().getY(),GetActor().getWidth(),GetActor().getHeight());
                map.put(name, func ?fc:fc.Run());
                break;
            case X:
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getX():GetActor().getX());
                break;
            case Y:
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getY():GetActor().getY());
                break;
            case Width:
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getWidth());
                break;
            case Height:
                map.put(name, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getHeight());
                break;
            case Screenshot:
                map.put(name, Util.GetScreenshot());
                break;
            case Buffer:
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