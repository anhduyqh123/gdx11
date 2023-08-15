package GDX11.AssetData;

import GDX11.GDX;
import GDX11.IObject.IObject;
import GDX11.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetPackage {
    public String name = "";
    public List<AssetNode> list = new ArrayList<>(); //all file
    private Map<String, AssetNode> map = new HashMap<>();
    public List<AssetNode> assetNodes = new ArrayList<>(); //main files not included atlas
    public List<AssetNode> loadableNode = new ArrayList<>(); //files to loaded
    public final Map<String, IObject> iObjectMap = new HashMap<>();
    public AssetPackage(){}
    public AssetPackage(String name){
        this.name = name;
    }
    public void Clear()
    {
        map.clear();
        assetNodes.clear();
        loadableNode.clear();
    }
    public void Install()
    {
        Util.For(list,n->{
            if (map.containsKey(n.name)) GDX.Error("trùng tên:"+n.pack+":"+n.url+"*"+map.get(n.name).url);
            map.put(n.name,n);

            if (n.kind!= AssetNode.Kind.None) loadableNode.add(n);
            if (n.kind!= AssetNode.Kind.TextureAtlas) assetNodes.add(n);
        });
    }
    public void Add(List<AssetNode> nodes)
    {
        list.addAll(nodes);
    }
    public void Add(AssetNode node)
    {
        list.add(node);
    }
    public boolean Contain(String name)
    {
        return map.containsKey(name);
    }
    public AssetNode Get(String name)
    {
        return map.get(name);
    }
    public List<AssetNode> GetNodes(AssetNode.Kind kind)
    {
        List<AssetNode> l = new ArrayList<>();
        for(AssetNode n : list)
            if (n.kind==kind) l.add(n);
        return l;
    }
    public String GetUrl(String extend)
    {
        return name+"/"+extend;
    }
}
