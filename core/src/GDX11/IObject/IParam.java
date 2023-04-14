package GDX11.IObject;

import GDX11.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

public class IParam extends IBase {
    public Map<String,String> dataMap = new HashMap<>();
    private GDX.Func<Map> getParam;
    private GDX.Func<Map> getEventMap;

    @Override
    public void Dispose() {
        getParam = null;
        getEventMap = null;
    }

    private Map<String, Runnable> GetEventMap()
    {
        if (getEventMap==null){
            Map map = new HashMap();
            getEventMap = ()->map;
        }
        return getEventMap.Run();
    }
    private void RunEvent(String name)
    {
        if (getEventMap==null) return;
        if (!GetEventMap().containsKey(name)) return;
        GetEventMap().get(name).run();
    }
    public void AddChangeEvent(String param, Runnable cb)
    {
        GetEventMap().put(param, cb);
    }
    public Map<String,Object> GetData()
    {
        if (getParam==null)
        {
            Map<String,Object> map = new HashMap<>();
            for (String key : dataMap.keySet())
                map.put(key,Config.ToBaseType(key,dataMap.get(key)));
//            for (String key : dataMap.keySet())
//                map.put(key,Json.ToBaseType(dataMap.get(key)));
            getParam = ()->map;
        }
        return getParam.Run();
    }
    public <T> T Get(String name)
    {
        return (T)GetData().get(name);
    }
    public <T> T Get(String name, T value0)
    {
        return Get(name)!=null?Get(name):value0;
    }
    public <T> T Get(String name,Class<T> type)
    {
        return Get(name);
    }
    public void Set(String name, Object value)
    {
        GDX.Try(()->{
            PutData(name,value);
            RunEvent(name);
        });
    }
    private void PutData(String name,Object value)
    {
        Object vl = value;
        if (Has(name))
        {
            Object vl0 = Get(name);
            if (vl0 instanceof Integer && value instanceof Float)//ICount useful
                vl = ((Float) value).intValue();
        }
        GetData().put(name,vl);
    }
    public boolean Has(String name)
    {
        return GetData().containsKey(name);
    }
    //variable
    public Number GetValueFromString(String stValue)
    {
        return new ICalculate(this::GetVariable).Get(stValue);
    }
    private Number GetVariable(String stValue)
    {
        if (stValue.equals("scale")) return Scene.i.scale;
        if (stValue.equals("sw")) return (float) Scene.i.width;
        if (stValue.equals("sh")) return (float) Scene.i.height;
        if (stValue.contains("_")) return GetExtendVariable(stValue);
        return GetActorVariable(stValue,GetActor());
    }
    private Number GetActorVariable(String stValue,Actor actor)
    {
        if (Has(stValue)){
            Object ob = Get(stValue);
            if (ob instanceof GDX.Func) return (Number) ((GDX.Func<?>) ob).Run();
            return (Number) ob;
        }
        if (stValue.equals("pSize")) return actor.getParent().getChildren().size;
        if (stValue.equals("i")) return actor.getZIndex();
        if (stValue.equals("w")) return actor.getWidth();
        if (stValue.equals("h")) return actor.getHeight();
        if (stValue.equals("pw")) return actor.getParent().getWidth();
        if (stValue.equals("ph")) return actor.getParent().getHeight();
        if (stValue.equals("x")) return actor.getX();
        if (stValue.equals("xc")) return actor.getX(Align.center);
        if (stValue.equals("xr")) return actor.getX(Align.right);
        if (stValue.equals("y")) return actor.getY();
        if (stValue.equals("yc")) return actor.getY(Align.center);
        if (stValue.equals("yt")) return actor.getY(Align.top);
        return Json.ToBaseType(stValue);
    }
    private Number GetExtendVariable(String stValue)
    {
        String[] arr = stValue.split("_");
        if (Asset.i.GetNode(arr[0])!=null) return GetTextureParam(arr[0],arr[1]);
        return GetActorVariable(arr[1],GetIActor().IRootFind(arr[0]).GetActor());
    }
    private Number GetTextureParam(String name,String key)
    {
        if (key.equals("w")) return Asset.i.GetTexture(name).getRegionWidth();
        return Asset.i.GetTexture(name).getRegionHeight();
    }

    //Align
    public static int GetAlign(String align)
    {
        if (align.equals("")) return Align.bottomLeft;
        if (align.equals("bottomLeft")) return Align.bottomLeft;
        if (align.equals("bottom")) return Align.bottom;
        if (align.equals("bottomRight")) return Align.bottomRight;
        if (align.equals("left")) return Align.left;
        if (align.equals("center")) return Align.center;
        if (align.equals("right")) return Align.right;
        if (align.equals("topLeft")) return Align.topLeft;
        if (align.equals("top")) return Align.top;
        if (align.equals("topRight")) return Align.topRight;
        return Align.bottomLeft;
    }
}
