package GDX11;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static GDX.Func2<Object,String,Object> getRemote = (key, vl0)->vl0;
    public static Config i;
    private JsonValue data;
    private Map<String,Object> map = new HashMap<>();
    public Config(String stData)
    {
        data = LoadData(stData);
        Install(data);
    }
    private JsonValue LoadData(String data)
    {
        return GDX.Try(()-> Json.StringToJson(data),
                ()->new JsonValue(JsonValue.ValueType.object));
    }
    private void Install(JsonValue js)
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

    public static void Init(String data)
    {
        if (i!=null) return;
        i =new Config(data);
    }
    public static void Init()
    {
        Init(GDX.GetString("config.json"));
    }

    //Set value
    public static void Set(String name, Object value)
    {
        i.map.put(name,value);
    }
    public static void SetPref(String name,Object value0)
    {
        GDX.i.SetPrefString(name,value0+"");
    }

    //Get value
    public static <T> T GetPref(String name,T value0)
    {
        T value = GetPref(name);
        return value!=null?value:value0;
    }
    public static <T> T GetPref(String name)
    {
        String str = GDX.i.GetPrefString(name,"");
        T value0 = Get(name);
        return str.equals("")?value0:Reflect.ToBaseType(str,value0);
    }
    public static <T> T GetRemote(String name,T value0)
    {
        return (T)getRemote.Run(name, Get(name,value0));
    }
    public static <T> T Get(String name, T value0)
    {
        return Get(name)!=null?Get(name):value0;
    }
    public static <T> T Get(String name)
    {
        return (T)i.map.get(name);
    }
    public static boolean Has(String name)
    {
        return i.map.containsKey(name);
    }


    public static Object ToBaseType(String key,String stValue)
    {
        if (key.startsWith("i_")) return Integer.parseInt(stValue);
        if (key.startsWith("f_")) return Float.parseFloat(stValue);
        if (key.startsWith("v2_") || key.startsWith("v3_")) return ParseVector(stValue);
        if (key.startsWith("cl_")) return Color.valueOf(stValue);
        return Json.ToBaseType(stValue);//remove in the future
        //return stValue;
    }
    //vector
    private static Vector ParseVector(String value)//(1,2)
    {
        value = value.replace("(","").replace(")","");
        String[] arr = value.split(",");
        if (arr.length==2) return new Vector2(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]));
        if (arr.length==3) return new Vector3(Float.parseFloat(arr[0]),Float.parseFloat(arr[1]),Float.parseFloat(arr[2]));
        return null;
    }
}
