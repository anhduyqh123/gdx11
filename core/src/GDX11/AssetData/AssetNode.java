package GDX11.AssetData;

import com.badlogic.gdx.files.FileHandle;

public class AssetNode {
    public enum Kind {
        None,
        TextureAtlas,
        Texture,
        Sound,
        Music,
        BitmapFont,
        Particle,
        Data,
        Spine,
        Object
    }

    public Kind kind = Kind.None;
    public String name = "";
    public String url = "";
    public String extension = "";
    public String pack="";
    public String atlas="";

    public AssetNode(){}
    public AssetNode(String pack, Kind kind, String atlas, String name)
    {
        this.pack = pack;
        this.kind = kind;
        this.name = name;
        this.atlas = atlas;
        this.extension = "none";
        this.url = "none";
    }
    public AssetNode(String pack, Kind kind, FileHandle fileHandle)
    {
        this.pack = pack;
        this.kind = kind;
        this.name = fileHandle.nameWithoutExtension();
        this.extension = fileHandle.extension();
        this.url = fileHandle.path();
    }
}
