package GDX11.IObject;

import GDX11.*;
import com.badlogic.gdx.utils.JsonValue;

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
    private <T extends IObject> T Get(String name){
        return Asset.i.GetObject(name);
    }

}
