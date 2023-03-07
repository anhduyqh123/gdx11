package GDX11.IObject;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class IObject {

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
