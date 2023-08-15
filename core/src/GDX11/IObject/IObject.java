package GDX11.IObject;

import GDX11.*;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public abstract class IObject extends IBase implements Json.JsonObject {

    public String name = "name";
    public IObject(){}
    public IObject(String name)
    {
        this.name = name;
    }

    public IMap GetIMap()
    {
        return Reflect.GetValue("iMap",this);
    }
    public <T extends IObject> T Clone()
    {
        return Reflect.Clone(this);
    }

    //For Json
    private Object GetPrefabObject(Object object0){
        String prefab = Reflect.GetValue("prefab",this);
        if (prefab==null||prefab.equals("")) return object0;
        if (object0==null) return Get(prefab);
        String prefab0 = Reflect.GetValue("prefab",object0);
        if (prefab0.equals(prefab)) return object0;
        return Get(prefab);
    }
    @Override
    public JsonValue ToJson(Object object0)
    {
        return Json.ObjectToJson(this,GetPrefabObject(object0));
    }
    @Override
    public Object ToObject(JsonValue js)
    {
        Object object = this;
        if (js.has("prefab")) object = Get(js.getString("prefab")).Clone();
        return Json.JsonToObject(js,object);
    }

//    private static Map<String,IObject> map = new HashMap<>();
//    public static <T extends IObject> T Get(String name)
//    {
//        if (!map.containsKey(name)) map.put(name,Json.ToObject(Asset.i.GetString(name)));
//        return (T)map.get(name);
//    }
    public static <T extends IObject> T Get(String name){
        return Asset.i.GetObject(name);
    }
    public static void Save(String url, IObject ic)
    {
        JsonValue jsData = Json.ToJson(ic);
        GDX.WriteToFile(url,jsData.toJson(JsonWriter.OutputType.minimal));
    }
//    private static final Map<String,String> miniMap = LoadMiniMap();
//    private static final Map<String,String> reMap = new HashMap<>();
//    private static int index;
//    private static final List<String> miniFields = Arrays.asList("class","prefab");
//    private static Map LoadMiniMap()
//    {
//        return GDX.Try(()->{
//            String stData = GDX.GetString("miniJson.ob");
//            return Json.ToMap(Json.StringToJson(stData),new HashMap<>(), String.class);
//        }, HashMap::new);
//    }
//    public static void SaveMiniMap()
//    {
//
//    }
//    private static JsonValue UnMini(String name)
//    {
//        JsonValue js = Json.StringToJson(GDX.GetStringByKey(name));
//        return js;
//    }
//    private static JsonValue Mini(String name)
//    {
//        JsonValue js = Get(name).ToJson();
//        Util.For(js,i->{
//            if (miniFields.contains(i.name))
//            {
//                String key = Put(i.asString());
//                if(key!=null) i.set(key);
//            }
//            String key = Put(i.name);
//            if (key!=null) i.setName(key);
//        });
//        return js;
//    }
//    private static String Put(String value){
//        if (reMap.containsKey(value) || value.length()<=5) return null;
//        String key = "$"+index++;
//        reMap.put(value,key);
//        miniMap.put(key,value);
//        return key;
//    }

}
