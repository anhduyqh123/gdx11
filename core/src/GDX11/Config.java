package GDX11;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Config implements Param {
    public static GDX.Func2<String,String,String> getRemote;
    public static Config i = new Config();

    public static void Init(String stData)
    {
        i.Install(LoadData(stData));
    }
    public static void Init()
    {
        Init(GDX.GetString("config.json"));
    }

    private final Map<String,Object> map = new HashMap<>();
    private final Map<String,Runnable> event = new HashMap<>();

    private void Install(JsonValue js)
    {
        Util.For(js,i->{
            if (i.isObject()){
                Install(i);
                return;
            }
            if (i.isArray()) return;
            //map.put(i.name,Json.ToBaseType(i));
            GetMap().put(i.name,ToBaseType(i.name,i.asString()));
        });
    }

    @Override
    public Map<String, Object> GetMap() {
        return map;
    }

    @Override
    public Map<String, Runnable> GetEventMap() {
        return event;
    }

    public <T> T GetRemote(String name,T value0)
    {
        String stValue = getRemote.Run(name,"");
        if (stValue.equals("")) return Get(name,value0);
        return Reflect.ToBaseType(stValue,Get(name,value0));
    }
    public static <T> T GetPref(String name,T value0)
    {
        String str = GDX.i.GetPrefString(name,"");
        return str.equals("")?value0:Reflect.ToBaseType(str,value0);
    }
    public static <T> T GetPref(String name)
    {
        return GetPref(name,i.Get(name));
    }
    public static void SetPref(String name,Object value0)
    {
        GDX.i.SetPrefString(name,value0+"");
    }

    //static
    private static JsonValue LoadData(String data) {
        return GDX.Try(()-> Json.StringToJson(data),
                ()->new JsonValue(JsonValue.ValueType.object));
    }
    public static Object ToBaseType(String key,String stValue) {
        return GDX.Try(()->{
            if (key.startsWith("i_")) return Integer.parseInt(stValue);
            if (key.startsWith("f_")) return Float.parseFloat(stValue);
            if (key.startsWith("v2_") || key.startsWith("v3_")) return ParseVector(stValue);
            if (key.startsWith("v4_")) return new GDX.Vector4(stValue);
            if (key.startsWith("cl_")) return Color.valueOf(stValue);
            return Json.ToBaseType(stValue);
        },()->stValue);
    }
    //vector
    private static Vector ParseVector(String value){//(1,2)
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
    }

}
