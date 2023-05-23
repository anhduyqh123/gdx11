package GDX11;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static GDX.Func2<String,String,String> getRemote;
    public static Map<String,Object> map = new HashMap<>();
    private static JsonValue LoadData(String data)
    {
        return GDX.Try(()-> Json.StringToJson(data),
                ()->new JsonValue(JsonValue.ValueType.object));
    }
    private static void Install(JsonValue js)
    {
        Util.For(js,i->{
            if (i.isObject()){
                Install(i);
                return;
            }
            if (i.isArray()) return;
            //map.put(i.name,Json.ToBaseType(i));
            map.put(i.name,ToBaseType(i.name,i.asString()));
        });
    }

    public static void Init(String stData)
    {
        Install(LoadData(stData));
    }
    public static void Init()
    {
        Init(GDX.GetString("config.json"));
    }

    //Set value
    public static void SetRun(String name,Runnable cb)
    {
        Set(name,cb);
    }
    public static void SetRun(String name, GDX.Runnable1 cb)
    {
        Set(name,cb);
    }
    public static void Set(String name, Object value)
    {
        map.put(name,value);
    }
    public static void SetPref(String name,Object value0)
    {
        GDX.i.SetPrefString(name,value0+"");
    }

    //Get value
    public static <T> T GetPref(String name,T value0)
    {
        String str = GDX.i.GetPrefString(name,"");
        return str.equals("")?value0:Reflect.ToBaseType(str,value0);
    }
    public static <T> T GetPref(String name)
    {
        return GetPref(name,Get(name));
    }
    public static <T> T GetRemote(String name,T value0)
    {
        String stValue = getRemote.Run(name,"");
        if (stValue.equals("")) return Get(name,value0);
        return Reflect.ToBaseType(stValue,Get(name,value0));
    }
    public static <T> T Get(String name, T value0)
    {
        return Get(name)!=null?Get(name):value0;
    }
    public static <T> T Get(String name)
    {
        return (T)map.get(name);
    }
    public static <T> T Get(String name,Class<T> tpye)
    {
        return Get(name);
    }
    public static Runnable GetRun(String name)
    {
        return Get(name);
    }
    public static void Run(String name)
    {
        if (Get(name)!=null) GetRun(name).run();
    }
    public static boolean Has(String name)
    {
        return map.containsKey(name);
    }


    public static Object ToBaseType(String key,String stValue)
    {
        return GDX.Try(()->{
            if (key.startsWith("i_")) return Integer.parseInt(stValue);
            if (key.startsWith("f_")) return Float.parseFloat(stValue);
            if (key.startsWith("v2_") || key.startsWith("v3_")) return ParseVector(stValue);
            if (key.startsWith("cl_")) return Color.valueOf(stValue);
            return Json.ToBaseType(stValue);
        },()->stValue);
    }
    //vector
    private static Vector ParseVector(String value)//(1,2)
    {
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
    }
}
