package GDX11.AssetData;

import GDX11.GDX;
import GDX11.Json;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.*;

public class AssetData{
    private Map<String, AssetPackage> packs = new HashMap<>(); //all pack

    public AssetData(){}
    public boolean Contains(String pack)
    {
        return packs.containsKey(pack);
    }
    public AssetPackage Get(String pack)
    {
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
    public void LoadPackages(String path)
    {
        for(FileHandle child : GDX.GetFile(path).list())
        {
            if (!child.isDirectory()) continue;
            LoadPackage(child.nameWithoutExtension(), child.path().replace("./",""));
        }
    }
    public void LoadPackages()
    {
        LoadPackages(".");
    }
    public void LoadPackage(String packName,String path)
    {
        NewPackage(packName,path);
        LoadBitmapFonts(packName,path+"/font");
        LoadAtlas(packName,path+"/atlas"); //load atlas before texture
        LoadTextures(packName,path+"/texture");
        LoadParticles(packName,path+"/particle");
        LoadSpines(packName,path+"/spine");

        ReadFileToAsset(packName, AssetNode.Kind.Data, GDX.GetFile(path+"/data"),"");
        ReadFileToAsset(packName, AssetNode.Kind.Sound, GDX.GetFile(path+"/sound"),"");
        ReadFileToAsset(packName, AssetNode.Kind.Music, GDX.GetFile(path+"/music"),"");
        ReadFileToAsset(packName, AssetNode.Kind.Object, GDX.GetFile(path+"/object"),"ob");
        ReadFileToAsset(packName, AssetNode.Kind.Data, GDX.GetFile(path+"/shader"),"");
    }
    public void LoadAtlas(String pack, String path)
    {
        FileHandle dir = GDX.GetFile(path);
        List<AssetNode> list = ReadFileToAsset(pack, AssetNode.Kind.TextureAtlas,dir,"atlas");

        for(AssetNode n : list)
        {
            FileHandle file = GDX.GetFile(n.url);
            TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(file,file.parent(),false);
            List<AssetNode> elements = new ArrayList<>();
            for(TextureAtlas.TextureAtlasData.Region r : data.getRegions())
                elements.add(new AssetNode(pack, AssetNode.Kind.None,n.name,r.name));
            packs.get(pack).Add(elements);
        }
    }

    public void LoadBitmapFonts(String pack, String path)
    {
        FileHandle dir = GDX.GetFile(path);
        ReadFileToAsset(pack, AssetNode.Kind.BitmapFont,dir,"fnt");
    }
    public void LoadParticles(String pack, String path)
    {
        FileHandle dir = GDX.GetFile(path);
        ReadFileToAsset(pack, AssetNode.Kind.Particle,dir,"p");
        ReadFileToAsset(pack, AssetNode.Kind.None,dir,"atlas"); //particle_atlas.atlas
    }
    public void LoadTextures(String pack, String path)
    {
        if (!Contains(pack)) NewPackage(pack,path);
        AssetPackage assetPackage = packs.get(pack);
        FileHandle dir = GDX.GetFile(path);

        for(FileHandle f : dir.list())
            if (f.isDirectory()){
                if (assetPackage.Contain(f.name())) continue;
                ReadFileToAsset(pack, AssetNode.Kind.Texture,f,"");
            }
            else AddNode(assetPackage.list,pack, AssetNode.Kind.Texture,f,"");
    }
    public void LoadSpines(String pack, String path)
    {
        FileHandle dir = GDX.GetFile(path);
        ReadFileToAsset(pack, AssetNode.Kind.Spine,dir,"json");
        ReadFileToAsset(pack, AssetNode.Kind.Spine,dir,"skel");
    }

    public List<AssetNode> ReadFileToAsset(String pack, AssetNode.Kind kind, FileHandle dir, String extension){
        if (!Contains(pack)) NewPackage(pack,dir.path());
        List<AssetNode> list = ReadFileToList(pack, kind, dir, extension);
        packs.get(pack).Add(list);
        return list;
    }
    private static List<AssetNode> ReadFileToList(String pack, AssetNode.Kind kind, FileHandle dir, String extension)
    {
        List<AssetNode> list = new ArrayList<>();
        if (dir==null) return list;
        for(FileHandle child : dir.list())
            if (child.isDirectory()) list.addAll(ReadFileToList(pack,kind,child,extension));
            else AddNode(list,pack,kind,child,extension);
        return list;
    }
    private static void AddNode(List<AssetNode> list, String pack, AssetNode.Kind kind, FileHandle dir, String extension)
    {
        if (dir.extension().equals("DS_Store")) return;
        if(!extension.equals("") && !dir.extension().equals(extension)) return;
        list.add(new AssetNode(pack,kind,dir));
    }
    private void NewPackage(String pack,String url)
    {
        packs.put(pack,new AssetPackage(url));
    }
}
