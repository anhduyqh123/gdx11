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

    //get value
    public static void Set(String name, Object value)
    {
        if (data.has(name)) data.get(name).set(value+"");
        else data.addChild(name,new JsonValue(value+""));
    }

    //get value
    public static  <T> T GetRemote(String name,T value0)
    {
        return (T)getRemote.Run(name, Get(name,value0));
    }
    public static  <T> T Get(String name, T value0)
    {
        try {
            String result = data.getString(name);
            return Reflect.ToBaseType(result,value0);
        }catch (Exception e){}
        return value0;
    }
    public static String ToString()
    {
        return data.toString();
    }
}
