package Tool.ObjectTool.Data;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import GDX11.Scene;
import GDX11.Util;
import com.badlogic.gdx.files.FileHandle;

public class ObjectPack extends IGroup {
    private AssetPackage assetPackage;
    private String pack;
    public ObjectPack(String pack)
    {
        this.pack = pack;
        assetPackage = Asset.i.GetAssetPackage(pack);
    }
    public void Renew()
    {
        list.clear();
        map.clear();
        Util.For(assetPackage.GetNodes(AssetNode.Kind.Object),n->{
            IActor iActor = IObject.Get(n.name);
            AddChild(n.name, iActor);
        });
    }

    @Override
    public void AddChild(String name, IActor iActor) {
        super.AddChild(name, iActor);
        iActor.SetIRoot(Scene.i.ui);
    }

    @Override
    public void Remove(String name) {
        super.Remove(name);
        AssetNode node = assetPackage.Get(name);
        try {
            FileHandle file = new FileHandle(node.url);
            file.delete();
        }catch (Exception e){}
    }

    @Override
    public void Move(String childName, int dir) {

    }

    @Override
    public void Refresh() {

    }

    public void Save(String name,Runnable done)
    {
        String url = assetPackage.GetUrl("object/"+name+".ob");
        IActor ic = map.get(name);
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
