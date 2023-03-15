package GDX11.IObject;

import GDX11.*;
import GDX11.AssetData.AssetNode;
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
    @Override
    public JsonValue ToJson(Object object0)
    {
        String prefab = Reflect.GetValue("prefab",this);
        object0 = prefab==null||prefab.equals("")?object0:Get(prefab);
        return Json.ObjectToJson(this,object0);
    }
    @Override
    public Object ToObject(JsonValue js)
    {
        Object object = this;
        if (js.has("prefab")) object = Get(js.getString("prefab")).Clone();
        return Json.JsonToObject(js,object);
    }

    public static <T extends IObject> T Get(String name)
    {
        AssetNode node = Asset.i.GetNode(name);
        String data = Config.GetRemote(name,GDX.GetString(node.url));
        return Json.ToObject(data);
    }
    public static void Save(String url, IObject ic)
    {
        JsonValue jsData = Json.ToJson(ic);
        GDX.WriteToFile(url,jsData.toJson(JsonWriter.OutputType.minimal));
    }

}
