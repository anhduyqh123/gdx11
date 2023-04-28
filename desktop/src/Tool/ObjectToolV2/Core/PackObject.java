package Tool.ObjectToolV2.Core;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import GDX11.Scene;
import GDX11.Util;
import com.badlogic.gdx.files.FileHandle;

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
        iMap.Clear();
        Util.For(assetPackage.GetNodes(AssetNode.Kind.Object),n->{
            IActor iActor = IObject.Get(n.name);
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

    public void Save(String name,Runnable done)
    {
        String url = assetPackage.GetUrl("object/"+name+".ob");
        IActor ic = iMap.Get(name);
        IObject.Save(url,ic);
        if (!assetPackage.Contain(name))
        {
            AssetNode node = new AssetNode(assetPackage.name, AssetNode.Kind.Object,"",name);
            node.url = url;
            assetPackage.list.add(node);
            assetPackage.Clear();
            assetPackage.Install();
            Asset.i.PushMapAssetNode(assetPackage.name);
        }
        done.run();
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
