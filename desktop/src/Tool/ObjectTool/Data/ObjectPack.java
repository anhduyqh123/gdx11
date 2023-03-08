package Tool.ObjectTool.Data;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import GDX11.Scene;
import GDX11.Util;

public class ObjectPack extends IGroup {
    private AssetPackage assetPackage;
    private String pack;
    public ObjectPack(String pack)
    {
        name = pack;
        this.pack = pack;
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
            AssetNode node = new AssetNode(pack, AssetNode.Kind.Object,"",name);
            node.url = url;
            assetPackage.list.add(node);
            assetPackage.Install();
        }
        done.run();
    }
}
