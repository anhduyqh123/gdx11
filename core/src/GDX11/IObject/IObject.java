package GDX11.IObject;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import GDX11.Reflect;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public abstract class IObject {

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
    public IObject Clone()
    {
        return Reflect.Clone(this);
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    public static IActor Get(String name)
    {
        AssetNode node = Asset.i.GetNode(name);
        String data = GDX.GetRemote(name,GDX.GetString(node.url));
        JsonValue jsData = Json.StringToJson(data);
        return IJson.FromJson(jsData);
    }
    public static void Save(String url, IActor ic)
    {
        JsonValue jsData = IJson.ToJson(ic);
        GDX.WriteToFile(url,jsData.toJson(JsonWriter.OutputType.minimal));
    }

}
