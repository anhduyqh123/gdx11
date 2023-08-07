package GDX11.IObject.IAction;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IImage;
import GDX11.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        Pos,Size,Bound,X,Y,Width,Height,Rotate,Region,Screenshot,Buffer
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
                map.put(param, func ?fc:fc.Run());
                break;
            case Size:
                fc = ()-> new Vector2(GetActor().getWidth(),GetActor().getHeight());
                map.put(param, func ?fc:fc.Run());
                break;
            case Region:
                IImage img = GetIActor(); TextureRegion tr = img.GetTexture();
                fc = ()-> new GDX.Vector4(tr.getU(),tr.getV(),tr.getU2(),tr.getV2());
                map.put(param, func ?fc:fc.Run());
                break;
            case X:
                map.put(param, func ?(GDX.Func<Object>) () -> GetActor().getX():GetActor().getX());
                break;
            case Y:
                map.put(param, func ?(GDX.Func<Object>) () -> GetActor().getY():GetActor().getY());
                break;
            case Width:
                map.put(param, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getWidth());
                break;
            case Height:
                map.put(param, func ?(GDX.Func<Object>) () -> GetActor().getWidth():GetActor().getHeight());
                break;
            case Rotate:
                map.put(param, func ?(GDX.Func<Object>) () -> GetActor().getRotation():GetActor().getRotation());
                break;
            case Bound:
                fc = ()-> new GDX.Vector4(GetActor().getX(),GetActor().getY(),GetActor().getWidth(),GetActor().getHeight());
                map.put(param, func ?fc:fc.Run());
                break;
            case Screenshot:
                map.put(param, Util.GetScreenshot());
                break;
            case Buffer:
                map.put(param, Util.GetTextureRegion(GetActor()));
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
