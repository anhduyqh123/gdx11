package Tool.ObjectToolV2.Core;

import GDX11.*;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class PackObject extends IGroup {
    private AssetPackage assetPackage;

    public PackObject(){}
    public PackObject(String pack)
    {
        name = pack;
        assetPackage = Asset.i.GetAssetPackage(pack);
    }
    public void Renew()
    {
        assetPackage = Asset.i.GetAssetPackage(name);
        iMap.Clear();
        Util.For(assetPackage.GetNodes(AssetNode.Kind.Object),n->{
            IActor iActor = Asset.i.GetObject(n.name);
            iMap.Add(iActor);
        });
    }

    @Override
    protected void OnAddChild(IActor iActor) {
        iActor.SetIRoot(Scene.i.ui);
    }

    @Override
    public void Refresh() {

    }

    public void Save(String name,Runnable done,Runnable newFile)
    {
        String url = assetPackage.GetUrl("object/"+name+".ob");
        IActor ic = iMap.Get(name);
        JsonValue jsData = Json.ToJson(ic);
        GDX.WriteToFile(url,jsData.toJson(JsonWriter.OutputType.minimal));
        done.run();
        if (!assetPackage.Contain(name)) newFile.run();
    }

    @Override
    protected void OnRemoveChild(IActor iActor) {
        super.OnRemoveChild(iActor);
        Delete(iActor);
    }

    public void Delete(IActor iActor) {
        AssetNode node = assetPackage.Get(iActor.name);
        try {
            FileHandle file = new FileHandle(node.url);
            file.delete();
        }catch (Exception e){}
    }
}
