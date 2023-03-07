package GDX11.AssetData;

import GDX11.GDX;
import GDX11.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetPackage {
    public String url = "";
    public List<AssetNode> list = new ArrayList<>(); //all file
    private Map<String, AssetNode> map = new HashMap<>();
    public List<AssetNode> assetNodes = new ArrayList<>(); //main files not included atlas
    public List<AssetNode> loadableNode = new ArrayList<>(); //files to loaded

    public AssetPackage(){}
    public AssetPackage(String url){
        this.url = url;
    }
    public void Install()
    {
        map.clear();
        assetNodes.clear();
        loadableNode.clear();

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
    public String GetAtlasUrl()
    {
        return GetUrl("atlas");
    }
    public String GetTextureUrl()
    {
        return GetUrl("texture");
    }
    public String GetParticleUrl()
    {
        return GetUrl("particle");
    }
    public String GetUrl(String extend)
    {
        return url+"/"+extend;
    }
}
