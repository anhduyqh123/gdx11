package GDX11.IObject;

import GDX11.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

public class IParam extends IBase {
    public Map<String,String> dataMap = new HashMap<>();
    protected GDX.Func<Map> getValue0Map;
    protected GDX.Func<Map> getParam;
    private GDX.Func<Map> getEventMap;

    @Override
    public void Dispose() {
        getParam = null;
        getEventMap = null;
        getValue0Map = null;
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
    private void InstallValue0()
    {
        Map<String,Object> map = new HashMap<>();
        for (String key : dataMap.keySet())
            map.put(key,ToBaseValue(Get(key)));
        getValue0Map = ()->map;
    }
    private Object ToBaseValue(String st)
    {
        return GDX.Try(()->Integer.valueOf(st),
                ()->GDX.Try(()->Float.valueOf(st),()->st));
    }
    private Object GetValue0(String name)
    {
        return getValue0Map.Run().get(name);
    }
    public <T> T GetValueOrValue0(String name)
    {
        if (!getValue0Map.Run().containsKey(name)) return (T)Get(name);
        Number value0 = (Number) GetValue0(name);
        Number number = Get(name,value0);
        if (value0 instanceof Integer) return (T)Integer.valueOf(number.intValue());
        return (T)number;
    }
    public Map<String,String> GetData()
    {
        if (getParam==null)
        {
            Map map = new HashMap(dataMap);
            getParam = ()->map;
            InstallValue0();
        }
        return getParam.Run();
    }
    public String Get(String name)
    {
        return GetData().get(name);
    }
    public <T> T Get(String name, T value0)
    {
        String stValue = Get(name);
        return GDX.Try(()->{
            if (value0 instanceof Color) return (T)Color.valueOf(stValue);
            if (value0 instanceof Vector) return (T)GetVector(stValue);
            return (T)GetVariable(stValue);
        },()->value0);
    }
    public <T> void Set(String name, T value)
    {
        try {
            GetData().put(name,value+"");
            RunEvent(name);
        }catch (Exception e){}
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
        if (Has(stValue)) return GetBaseValue(GetData().get(stValue));
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
        return GetBaseValue(stValue);
    }
    private Number GetBaseValue(String stValue)
    {
        if (stValue.contains(".")) return Float.valueOf(stValue);
        return Integer.valueOf(stValue);
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
    //vector
    private static Vector GetVector(String value)//(1,2)
    {
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        if (arr.length==3) return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
        return null;
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
