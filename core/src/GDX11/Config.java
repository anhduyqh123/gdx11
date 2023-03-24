package GDX11;

import com.badlogic.gdx.utils.JsonValue;

public class Config {
    public static GDX.Func2<Object,String,Object> getRemote = (key, vl0)->vl0;

    public static JsonValue data = LoadData();

    public static JsonValue LoadData(String data)
    {
        return GDX.Try(()-> Json.StringToJson(data),
                ()->new JsonValue(JsonValue.ValueType.object));
    }
    private static JsonValue LoadData()
    {
        return GDX.Try(()->LoadData(GDX.GetString("config.json")),()->null);
    }
    public static JsonValue GetJson(String name)
    {
        return data.get(name);
    }
    public static void Save()
    {
        try {
            GDX.WriteToFile("config.json",data.toString());
        }catch (Exception e){}
    }

    //Set value
    public static void Set(String name, Object value)
    {
        if (data.has(name)) data.get(name).set(value+"");
        else data.addChild(name,new JsonValue(value+""));
    }
    public static void SetPref(String name,Object value0)
    {
        GDX.SetPrefString(name,value0+"");
    }

    //Get value
    public static <T> T GetPref(String name,T value0)
    {
        String stValue = GetPref(name);
        if (stValue.equals("")) return GetRemote(name, value0);
        return Reflect.ToBaseType(stValue,value0);
    }
    public static String GetPref(String name)
    {
        return GDX.GetPrefString(name,"");
    }
    public static <T> T GetRemote(String name,T value0)
    {
        return (T)getRemote.Run(name, Get(name,value0));
    }
    public static  <T> T Get(String name, T value0)
    {
        return GDX.Try(()->{
            String stValue = Get(name);
            return Reflect.ToBaseType(stValue,value0);
        },()->value0);
    }
    public static String Get(String name){
        return data.getString(name);
    }
    public static boolean Has(String name)
    {
        return data.has(name);
    }
    public static String ToString()
    {
        return data.toString();
    }
}
