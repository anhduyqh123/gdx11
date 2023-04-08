package Tool.ObjectTool.Data;

import GDX11.GDX;
import GDX11.GTextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.tinify.Source;
import com.tinify.Tinify;

import java.util.Arrays;
import java.util.List;

public class GDXTool {
    private static TexturePacker.Settings settings = GetSettings();
    private final static List<String> encodeExtension = Arrays.asList("png","jpg");

    public static void Encode(String path)
    {
        FileHandle dir = new FileHandle(path);
        for (FileHandle f : dir.list())
        {
            if (f.isDirectory()) Encode(f.path());
            else
            if (encodeExtension.contains(f.extension())) GTextureLoader.Encode(f);
        }
    }
    public static void Decode(String path)
    {
        FileHandle dir = new FileHandle(path);
        for (FileHandle f : dir.list())
        {
            if (f.isDirectory()) Decode(f.path());
            else
                if (encodeExtension.contains(f.extension())) Decode(f);
        }
    }
    private static void Decode(FileHandle file)
    {
        file.writeBytes(GTextureLoader.Decode(file),false);
    }
    public static void GenerateMainAtlas(String path,String output)
    {
        GenerateMainAtlas(new FileHandle(path),output);
    }
    private static void GenerateMainAtlas(FileHandle dir,String output)
    {
        for(FileHandle f : dir.list())
        {
            if (!f.isDirectory()) continue;
            if (f.name().charAt(0)!='a') continue;
            GenerateAtlas(f,output);
        }
    }
    public static void GenerateAtlas(String path,String output)
    {
        Pack(new FileHandle(path),output);
    }
    private static void GenerateAtlas(FileHandle dir,String output)
    {
        Pack(dir,output);
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
    public static void Tiny(String path)
    {
        FileHandle dir = new FileHandle(path);
        for (FileHandle f : dir.list())
        {
            if (f.isDirectory()) Tiny(f.path());
            else
            if (encodeExtension.contains(f.extension())) Tiny(f);
        }
    }
    private static void Tiny(FileHandle file)
    {
        try {
            GDX.Log(file.path()+"***tining!");
            Source source = Tinify.fromFile(file.path());
            source.toFile(file.path());
            GDX.Log(file.path()+"***done!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
