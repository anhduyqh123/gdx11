package GDX11;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//update 7/10

public class Json {
    protected static final String stClass = "class";

    private List<Object> jsonObject_excluded = new ArrayList<>();
    //<editor-fold desc="Json">
    //FromJsonValue
    protected Object ToObject(Class type,JsonValue js)//nếu là list,map thì type = elementType
    {
        if (Reflect.IsBaseType(type)) return JsonToBaseType(type,js);
        return ToObject(type,Reflect.NewInstance(type),js);
    }
    protected Object ToObject(Class type,Object object,JsonValue js)//nếu là list,map thì type = elementType
    {
        if (js.isNull()) return null;
        if (object instanceof Enum) return Enum.valueOf(type,js.asString());
        if (object instanceof List) return ToList(type,(List) object,js);
        if (object instanceof Map) return ToMap(type,(Map) object,js);
        if (Reflect.IsBaseType(type)) return JsonToBaseType(type,js);

        object = GetObjectToRead(object,js);
        if (CheckJsonObject(object)) return ((JsonObject)object).ToObject(js);
        ReadFields(object,js);
        return object;
    }
    protected Object GetObjectToRead(Object object,JsonValue js)
    {
        return object;
    }
    private void ReadFields(Object object,JsonValue js)
    {
        Map<String, Field> map = Reflect.GetFields(object.getClass());
        Foreach(js,i->{
            if (map.containsKey(i.name))
            {
                Field field = map.get(i.name);
                Object value = NewValue(field,object,i);
                Class type = field.getType();
                if (type.equals(List.class)) type = field.getElementType(0);
                if (type.equals(Map.class)) type = field.getElementType(1);
                Reflect.SetValue(field,object,ToObject(type,value,i));
            }
        });
    }
    private void ReadFields(Object object,Object object0)//object0 is super new 7/10
    {
        Map<String, Field> map = Reflect.GetFields(object0.getClass());
        for (String k : map.keySet())
        {
            Field filed = map.get(k);
            Object value = Reflect.GetValue(filed,object);
            Object value0 = Reflect.GetValue(filed,object0);
            if (Reflect.Equals(value,value0)) continue;
            if (Reflect.IsBaseType(filed.getType()))
                Reflect.SetValue(filed,object,value0);
            else ReadFields(value,value0);
        }
    }

    private Object NewValue(Field field,Object object,JsonValue js)
    {
        if (js.has(stClass))
        {
            Class type = Reflect.GetClass(js.getString(stClass));
            return Reflect.NewInstance(type);
        }
        Object value = Reflect.GetValue(field,object);
        if (value!=null) return value;
        return Reflect.NewInstance(field.getType());
    }
    private Class GetType(Class type,JsonValue js)
    {
        if (js.has(stClass)) return Reflect.GetClass(js.getString(stClass));
        return type;
    }

    public List ToList(Class type,List list,JsonValue js)
    {
        list.clear();
        Foreach(js,i->{
            Class childType = GetType(type,i);
            list.add(ToObject(childType,i));
        });
        return list;
    }
    public Map ToMap(Class type,Map map,JsonValue js)
    {
        //return ToMap(type,Reflect.NewInstance(map.getClass()),map,js);
        return ToMap(type,new HashMap(),map,js);
    }
    protected Map ToMap(Class type,Map newMap,Map map,JsonValue js)
    {
        Foreach(js,i->{
            Object object = map.get(i.name);
            Class childType = GetType(type,i);
            if (object==null) object = Reflect.NewInstance(childType);
            if (!childType.equals(object.getClass())) //new 7/10
            {
                Object object0 = object;
                object = Reflect.NewInstance(childType);
                ReadFields(object,object0);
            }
            newMap.put(i.name,ToObject(childType,object,i));
        });
        return newMap;
    }
    private <T> T JsonToBaseType(Class<T> type,JsonValue js)
    {
        return Reflect.ToBaseType(js.asString(),type);
    }
    //ToJsonValue
    protected JsonValue ObjectToJson(Object object)
    {
        return ToJson(object,Reflect.GetDefaultObject(object.getClass()),false,this::WriteFields);
    }
    protected JsonValue ToJson(Object object, Object defaultObject, boolean hasClass, List<String> fieldNames)
    {
        return ToJson(object, defaultObject, hasClass,((ob, df, js) -> {
            WriteFields(ob,df,js,Reflect.GetFields(object.getClass(),fieldNames));
        }));
    }
    protected JsonValue ToJson(Object object, Object defaultObject, boolean hasClass)
    {
        return ToJson(object, defaultObject, hasClass,this::WriteFields);
    }
    protected JsonValue ToJson(Object object, Object defaultObject, boolean hasClass,OnWriteFields onWriteFields)
    {
        if (object==null) return new JsonValue(JsonValue.ValueType.nullValue);
        Class type = object.getClass();
        if (Reflect.IsBaseType(type)) return BaseTypeToJson(object);
        if (object instanceof  Enum) return new JsonValue(object.toString());
        if (object instanceof List) return ListToJson((List) object,hasClass);
        //if (object instanceof JsonObject) return ((JsonObject)object).ToJson();
        if (CheckJsonObject(object)) return ((JsonObject)object).ToJson();

        if (defaultObject==null) defaultObject = Reflect.NewInstance(type);
        if (object instanceof Map) return MapToJson((Map) object,(Map) defaultObject,hasClass);
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        if (hasClass) js.addChild(stClass, BaseTypeToJson(type.getName()));
        defaultObject = GetObjectToWrite(object, defaultObject);
        onWriteFields.Run(object,defaultObject,js);
        return js;
    }

    protected Object GetObjectToWrite(Object object, Object defaultObject)
    {
        return defaultObject;
    }

    private void WriteFields(Object object, Object defaultObject, JsonValue js,Map<String,Field> fieldMap)
    {
        for(Field f : fieldMap.values())
        {
            Object value = Reflect.GetValue(f,object);
            Object value0 = Reflect.GetValue(f,defaultObject);
            if (Reflect.Equals(value,value0)) continue;
            value0 = GetValue0(value,value0);
            js.addChild(f.getName(), ToJson(value,value0,HasClass(f.getType(),value)));
        }
    }
    private Object GetValue0(Object value,Object value0)//lấy default nếu value0 là class super
    {
        if (value==null||value0==null) return value0;
        Class type = value.getClass();
        if (value0.getClass().equals(type)) return value0;
        return Reflect.GetDefaultObject(type);
    }
    private void WriteFields(Object object, Object defaultObject, JsonValue js)
    {
        WriteFields(object, defaultObject, js,Reflect.GetFields(object.getClass()));
    }
    private boolean HasClass(Class type,Object object)
    {
        if (object==null) return false;
        if (object instanceof Map || object instanceof List) return false;
        return !type.equals(object.getClass());
    }
    private boolean HasSuperClass(Object object)
    {
        if (object==null) return false;
        Class superClass = object.getClass().getSuperclass();
        if (superClass.equals(Object.class)) return false;
        return true;
    }
    private JsonValue ListToJson(List list, boolean hasClass)
    {
        JsonValue js = new JsonValue(JsonValue.ValueType.array);
        for(Object child : list)
        {
            if (!Reflect.IsValidClass(child.getClass())) continue;
            boolean isClass = HasSuperClass(child)?true:hasClass;
            js.addChild(ToJson(child,null,isClass));
            //js.addChild(ToJson(child,Reflect.GetDefaultObject(child.getClass()),isClass));
        }
        return js;
    }
    private JsonValue MapToJson(Map map, Map defaultMap, boolean hasClass)
    {
        JsonValue js = new JsonValue(JsonValue.ValueType.object);
        for(Object k : map.keySet())
        {
            Object value = map.get(k);
            if (!Reflect.IsValidClass(value.getClass())) continue;
            Object value0 = defaultMap.get(k);
            boolean isClass = HasSuperClass(value)?true:hasClass;
            MapToJson(k.toString(),value,value0,isClass,js);
        }
        return js;
    }
    protected void MapToJson(String key,Object ob,Object ob0, boolean hasClass,JsonValue js)
    {
        js.addChild(key, ToJson(ob,ob0,hasClass));
    }
    private JsonValue BaseTypeToJson(Object object)
    {
        Class type = object.getClass();
        if (type == Integer.class) return new JsonValue((int)object);
        if (type == Float.class) return new JsonValue(object+"");
        if (type == Long.class) return new JsonValue((long)object);
        if (type == Double.class) return new JsonValue((double)object);
        if (type == Short.class) return new JsonValue((short)object);
        if (type == Byte.class) return new JsonValue((byte)object);
        if (type == String.class) return new JsonValue((String)object);
        if (type == Boolean.class) return new JsonValue((boolean)object);
        if (type == Character.class) return new JsonValue((char)object);
        return null;
    }

    interface OnWriteFields
    {
        void Run(Object object, Object defaultObject, JsonValue js);
    }
    //</editor-fold>


    //<editor-fold desc="Static To JsonValue">
    public static String ToJsonData(Object object)
    {
        return ToJsonData(object,JsonWriter.OutputType.minimal);
    }
    public static String ToJsonData(Object object, JsonWriter.OutputType outputType)
    {
        return ToJson(object).toJson(outputType);
    }
    public static String ToJsonData(Object object, List<String> fieldNames)
    {
        return ToJson(object,false,fieldNames).toJson(JsonWriter.OutputType.minimal);
    }

    public static JsonValue ToJson(Object object)
    {
        return ToJson(object,false);
    }
    public static JsonValue ToJson(Object object, boolean hasClass)
    {
        Json json = new Json();
        return json.ToJson(object,Reflect.GetDefaultObject(object.getClass()),hasClass);
    }
    public static JsonValue ToJson(Object object, boolean hasClass,List<String> fieldNames)
    {
        Json json = new Json();
        return json.ToJson(object,Reflect.GetDefaultObject(object.getClass()),hasClass,fieldNames);
    }
    //</editor-fold>
    //<editor-fold desc="Static From JsonValue">
    public static void ReadFields(Object object,String data)
    {
        new Json().ReadFields(object, StringToJson(data));
    }

    public static <T> List<T> ToList(Class<T> elementType,JsonValue js)
    {
        Json json = new Json();
        return json.ToList(elementType,new ArrayList(),js);
    }
    public static <T> Map<String,T> ToMap(Class<T> elementType,JsonValue js)
    {
        Json json = new Json();
        return json.ToMap(elementType,new HashMap(),js);
    }
    public static <T> T FromJson(Class<T> type,String jsData)
    {
        return FromJson(type, StringToJson(jsData));
    }
    public static <T> T FromJson(String jsData)
    {
        return FromJson(StringToJson(jsData));
    }
    public static <T> T FromJson(JsonValue js)
    {
        Class type = Reflect.GetClass(js.getString(stClass));
        return (T)FromJson(type,js);
    }
    public static <T> T FromJson(Class<T> type,JsonValue js)
    {
        Json json = new Json();
        return (T)json.ToObject(type,js);
        //return (T)json.ToObject(type,Reflect.NewInstance(type),js);
    }
    //</editor-fold>
    //<editor-fold desc="Other">
    private static final JsonReader jsonReader = new JsonReader();
    public static JsonValue StringToJson(String stJson)
    {
        return jsonReader.parse(stJson);
    }
    public static void Foreach(JsonValue json, GDX.Runnable1<JsonValue> cb)
    {
        for (JsonValue child = json.child; child != null; child = child.next)
            cb.Run(child);
    }
    //</editor-fold>
    //<editor-fold desc="JsonObject">
    private boolean CheckJsonObject(Object object)
    {
        return object instanceof JsonObject && !jsonObject_excluded.contains(object);
    }
    public static abstract class JsonObject
    {
        protected void ReadJson(JsonValue js)
        {
            Json json = new Json();
            json.jsonObject_excluded.add(this);
            json.ToObject(getClass(),this,js);
        }
        public <T> T ToObject(JsonValue js){
            ReadJson(js);
            return (T)this;
        }
        public JsonValue ToJson(){
            Json json = new Json();
            json.jsonObject_excluded.add(this);
            return json.ObjectToJson(this);
        }
    }

    //</editor-fold>
    //<editor-fold desc="Fold">
    //</editor-fold>
}
