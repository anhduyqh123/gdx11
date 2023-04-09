package Tool.ObjectTool.Data;

import GDX11.GDX;
import GDX11.GTextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.tinify.Source;
import com.tinify.Tinify;

public class GDXTool {
    private static TexturePacker.Settings settings = GetSettings();
    public static void Encode(FileHandle file)
    {
        GTextureLoader.Encode(file);
    }
    public static void Decode(FileHandle file)
    {
        file.writeBytes(GTextureLoader.Decode(file),false);
    }
    public static void GenerateAtlas(String path,String output)
    {
        Pack(new FileHandle(path),output);
    }
    private static void Pack(FileHandle dir,String output)
    {
        if (!dir.isDirectory()) return;
        TexturePacker.process(settings,dir.path(),output,dir.name());
    }
    private static TexturePacker.Settings GetSettings()
    {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.flattenPaths = true;
        settings.combineSubdirectories = true;
        settings.useIndexes = false;
        return settings;
    }

    //Tiny
    public static void Tiny(FileHandle file)
    {
        try {
            Tinify.setKey("cqKXRyMygkGcnQ988FL7gWt3J0fz8Qdy");
            GDX.Log(file.path()+"***tining!");
            Source source = Tinify.fromFile(file.path());
            source.toFile(file.path());
            GDX.Log(file.path()+"***done!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
