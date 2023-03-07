package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import GDX11.Reflect;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Map;

public class IJson extends Json {
    public static GDX.Func1<IActor,String> getIChild = IObject::Get;
    public static <T> T FromJson(JsonValue js)
    {
        IJson json = new IJson();
        Class type = Reflect.GetClass(js.getString(stClass));
        return (T)json.ToObject(type,Reflect.NewInstance(type),js);
    }
    public static JsonValue ToJson(IActor iActor)
    {
        IJson json = new IJson();
        return json.ToJson(iActor,Reflect.GetDefaultObject(iActor.getClass()),true);
    }

    @Override
    protected Object GetObjectToWrite(Object object, Object defaultObject) {
        try {
            String prefab1 = ((IActor)object).prefab;
            String prefab2 = ((IActor)defaultObject).prefab;
            if (prefab1.equals(prefab2)) return defaultObject;
            return Reflect.Clone(getIChild.Run(prefab1));
        }catch (Exception e){}
        return defaultObject;
    }

    @Override
    protected Object GetObjectToRead(Object object, JsonValue js) {
        String prefab = js.getString("prefab","");
        if (!prefab.equals("")) return Reflect.Clone(getIChild.Run(prefab));
        return object;
    }

    //to clone object -> only change + add, cannot remove
    @Override
    public Map ToMap(Class type, Map map, JsonValue js) {
        return ToMap(type,map, map, js);
    }

    @Override
    protected void MapToJson(String key, Object ob, Object ob0, boolean hasClass, JsonValue js) {
        if (Reflect.Equals(ob,ob0)) return;
        super.MapToJson(key, ob, ob0, hasClass, js);
    }
}
