package GDX11.AssetData;

import GDX11.GDX;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.*;

public class AssetData {
    private Map<String, AssetPackage> packs = new HashMap<>(); //all pack

    public AssetData(){}
    public boolean Contains(String pack)
    {
        return packs.containsKey(pack);
    }
    public AssetPackage Get(String pack)
    {
        if (!Contains(pack)) packs.put(pack,new AssetPackage());
        return packs.get(pack);
    }
    public void For(GDX.Runnable1<AssetPackage> cb)
    {
        for (AssetPackage p : packs.values())
            cb.Run(p);
    }
    public Set<String> GetKeys()
    {
        return packs.keySet();
    }
    public List<AssetNode> GetNodes(AssetNode.Kind kind)
    {
        List<AssetNode> list = new ArrayList<>();
        For(p->list.addAll(p.GetNodes(kind)));
        return list;
    }
    public void Install()
    {
        for(AssetPackage assetPackage : packs.values())
            assetPackage.Install();
    }
    public void LoadPackages()
    {
        FileHandle root = GDX.GetFile(".");
        List<String> defaultPackages = Arrays.asList("atlas","atlas0","texture");
        for (FileHandle dir : root.list())
        {
            if (!dir.isDirectory()) continue;
            if (defaultPackages.contains(dir.nameWithoutExtension())) continue;
            LoadPackage(dir);
        }
        for(FileHandle dir : root.child("texture").list())
            LoadAsset(dir.nameWithoutExtension(), AssetNode.Kind.Texture,dir,"");
        for(FileHandle dir : root.child("atlas0").list())
            LoadAsset(dir.nameWithoutExtension(), AssetNode.Kind.Texture,dir,"");
        for (FileHandle file : GDX.GetFiles(root.child("atlas"),"atlas"))
            LoadAtlasData(file.nameWithoutExtension(),file);
    }
    private void LoadPackage(FileHandle dir)
    {
        String pack = dir.nameWithoutExtension();
        LoadAtlas(pack,dir.child("atlas"));
        LoadAsset(pack, AssetNode.Kind.Texture,dir.child("atlas0"),"");
        LoadAsset(pack, AssetNode.Kind.Texture,dir.child("texture"),"");
        LoadAsset(pack, AssetNode.Kind.BitmapFont,dir.child("font"),"fnt");
        LoadAsset(pack, AssetNode.Kind.Particle,dir.child("particle"),"p");
        LoadAsset(pack, AssetNode.Kind.None,dir.child("particle"),"atlas");
        LoadAsset(pack, AssetNode.Kind.Spine,dir.child("spine"),"json");
        LoadAsset(pack, AssetNode.Kind.Spine,dir.child("spine"),"skel");
        LoadAsset(pack,AssetNode.Kind.Data,dir.child("data"),"");
        LoadAsset(pack,AssetNode.Kind.Sound,dir.child("sound"),"");
        LoadAsset(pack,AssetNode.Kind.Music,dir.child("music"),"");
        LoadAsset(pack,AssetNode.Kind.Object,dir.child("object"),"ob");
        LoadAsset(pack,AssetNode.Kind.Data,dir.child("shader"),"");
    }
    private void LoadAsset(String pack, AssetNode.Kind kind,FileHandle dir,String extension)
    {
        for (FileHandle file : GDX.GetFiles(dir,extension))
            Get(pack).Add(new AssetNode(pack,kind,file));
    }
    private void LoadAtlas(String pack,FileHandle dir)
    {
        for (FileHandle file : GDX.GetFiles(dir,"atlas"))
            LoadAtlasData(pack,file);
    }
    private void LoadAtlasData(String pack,FileHandle file)//file atlas
    {
        Get(pack).Add(new AssetNode(pack, AssetNode.Kind.TextureAtlas,file));
        TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(file,file.parent(),false);
        for(TextureAtlas.TextureAtlasData.Region r : data.getRegions())
            Get(pack).Add(new AssetNode(pack, AssetNode.Kind.None,file.nameWithoutExtension(),r.name));
    }
}
