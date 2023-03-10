package GDX11;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;

import java.util.List;
import java.util.Map;

public class Json {

    private static final String stClass = "class";

    //write
    public static JsonValue ToJson(Object object)
    {
        return ToJson(object,Reflect.GetDefaultObject(object.getClass()));
    }
    public static JsonValue ToJson(Object object,Object object0)
    {
        if (object==null) return new JsonValue(JsonValue.ValueType.nullValue);
        Class type = object.getClass();
        if (Reflect.IsBaseType(type)) return BaseToJson(object);
        if (object instanceof  Enum) return new JsonValue(object.toString());
        if (object instanceof  List) return ListToJson((List) object,(List)object0);
        if (object instanceof  Map) return MapToJson((Map) object,(Map)object0);
        Method toJson = Reflect.GetMethod(type,"ToJson",Object.class);
        if (toJson!=null) return Reflect.RunMethod(toJson,object,object0);
        return ObjectToJson(object, object0);
    }
    public static JsonValue BaseToJson(Object object)
    {
        Class type = object.getClass();
        if (type == Integer.class) return new JsonValue((int)object);
        if (type == Float.class) return new JsonValue(object+"");
        if (type == Long.class) return new JsonValue((long)object);
        if (type == Double.class) return new JsonValue((double)object);
        if (type == Short.class) return new JsonValue((short)object);
        if (type == Byte.class) return new JsonValue((byte)object);
        if (type == Boolean.class) return new JsonValue((boolean)object);
        if (type == Character.class) return new JsonValue((char)object);
        return new JsonValue((String)object);
    }
    public static JsonValue MapToJson(Map map,Map map0)
    {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        Util.For(map.keySet(),key->{
            Object object = map.get(key);
            Object object0 = map0!=null && map0.containsKey(key)?map0.get(key)
                    :Reflect.GetDefaultObject(object.getClass());
            json.addChild(key.toString(),ToJson(object,object0));
        });
        return json;
    }
    public static JsonValue MapToJson(Map map)
    {
        return MapToJson(map,null);
    }
    public static JsonValue ListToJson(List list,List list0)
    {
        JsonValue json = new JsonValue(JsonValue.ValueType.array);
        Util.ForIndex(list,i->{
            Object object = list.get(i);
            Object object0 = list0!=null && list0.size()>=list.size()?list0.get(i)
                    :Reflect.GetDefaultObject(object.getClass());
            json.addChild(ToJson(object,object0));
        });
        return json;
    }
    public static JsonValue ListToJson(List list)
    {
        return ListToJson(list,null);
    }
    public static JsonValue ObjectToJson(Object object,Object object0)
    {
        Class type = object.getClass();
        if (object0==null) object0 = Reflect.GetDefaultObject(type);
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild(stClass,new JsonValue(type.getName()));
        for(Field f : Reflect.GetFields(object.getClass()).values())
        {
            Object value = Reflect.GetValue(f,object);
            Object value0 = Reflect.GetValue(f,object0);
            if (Reflect.Equals(value,value0)) continue;
            json.addChild(f.getName(),ToJson(value,value0));
        }
        return json;
    }
    public static <T> T ToObject(String data)
    {
        return ToObject(StringToJson(data));
    }
    public static <T> T ToObject(JsonValue js)
    {
        Class type = Reflect.GetClass(js.getString(stClass));
        return ToObject(js,Reflect.NewInstance(type),null);
    }
    private static <T> T ToObject(JsonValue js,Object object,Field field)
    {
        Class type = object.getClass();
        if (Reflect.IsBaseType(type)) return Reflect.ToBaseType(js.asString(),type);
        if (object instanceof  Enum) return (T)Enum.valueOf(type,js.asString());
        if (object instanceof List) return (T)ToList(js,(List) object,field.getElementType(0));
        if (object instanceof Map) return (T)ToMap(js,(Map) object,field.getElementType(1));
        Method toObject = Reflect.GetMethod(type,"ToObject",JsonValue.class);
        if (toObject!=null) return Reflect.RunMethod(toObject,object,js);
        return JsonToObject(js, object);
    }
    public static Map ToMap(JsonValue js,Map map,Class elementType)
    {
        Util.For(js,i-> map.put(i.name,ToObject(i,NewElement(i,elementType),null)));
        return map;
    }
    public static List ToList(JsonValue js,List list,Class elementType)
    {
        Util.For(js,i-> list.add(ToObject(i,NewElement(i,elementType),null)));
        return list;
    }
    private static <T> T NewElement(JsonValue js,Class elementType)//Ưu tiên lấy class trong json
    {
        elementType = js.has(stClass)?Reflect.GetClass(js.getString(stClass)):elementType;
        return Reflect.NewInstance(elementType);
    }
    public static <T> T JsonToObject(JsonValue js,Object object)
    {
        Map<String,Field> map = Reflect.GetFields(object.getClass());
        Util.For(js,i->{
            if (!map.containsKey(i.name)) return;
            Field field = map.get(i.name);
            Object value = Reflect.GetValue(field,object);
            Reflect.SetValue(field,object,ToObject(i,value,field));
        });
        return (T)object;
    }
    //<editor-fold desc="Other">
    private static final JsonReader jsonReader = new JsonReader();
    public static JsonValue StringToJson(String stJson)
    {
        return jsonReader.parse(stJson);
    }
    //</editor-fold>
}
