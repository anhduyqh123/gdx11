package Tool.ObjectToolV2.Core;

import GDX11.GDX;
import GDX11.GTextureLoader;
import GDX11.Util;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.JsonValue;
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
    static {
        Tinify.setKey("cqKXRyMygkGcnQ988FL7gWt3J0fz8Qdy");
    }
    public static void Tiny(FileHandle file)
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
    //CSV
    public static JsonValue CSVToJson(String name){//translate
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        String[][] table = Util.ReadCSVByKey(name);
        Util.For(0,table.length-1, i->{
            String[] arr = table[i];
            String key = arr[0];
            Util.For(1,arr.length-1,j->{
                String code = table[0][j];
                String value = arr[j];
                if (!json.has(key)) json.addChild(key,new JsonValue(JsonValue.ValueType.object));
                json.get(key).addChild(code,new JsonValue(Format(value)));
            });
        });
        return json;
    }
    private static String Format(String st)
    {
        if (st.equals("")) return st;
        char ch0 = st.charAt(0),ch1 = st.charAt(st.length()-1);
        if (st.contains(",") && ch0=='\"' && ch1=='\"') return st.substring(1,st.length()-2);
        return st;
    }
}
