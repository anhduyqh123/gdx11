package GDX11;

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
            map.put(i.name,Json.ToBaseType(i));
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
}
