package Tool.ObjectToolV2.Core;

import GDX11.Asset;
import GDX11.AssetData.AssetData;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.GDX;
import GDX11.Json;
import GDX11.Util;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateAsset {
    private String path = Path.of("").toAbsolutePath().getParent().toString();
    private FileHandle rootDir = new FileHandle(Path.of("").toAbsolutePath().toString());
    private FileHandle androidDir = new FileHandle(path+"/android/assets");
    private List<Runnable> process = new ArrayList<>();
    private List<Runnable> tinyProcess = new ArrayList<>();
    private List<Runnable> encodeProcess = new ArrayList<>();
    public GDX.Runnable1<String> cbProgress;
    public Runnable onFinish;
    public GDX.Func<Boolean> tiny;

    public void Run(String pack)
    {
        if (pack.equals("all")) Generate();
        else Generate(pack);
        new Thread(()->{
            Run("atlas",process);
            if (tiny.Run())
            {
                Tiny(pack);
                Run("tiny",tinyProcess);
            }
            Encode(pack);
            Run("encode",encodeProcess);

            JsonTranslate();
            WritePackages();
            Json.mini.Save(androidDir);
            onFinish.run();
        }).start();
    }
    private void Run(String name,List<Runnable> runnableList)
    {
        for (int i=0;i<runnableList.size();i++)
        {
            Runnable cb = runnableList.get(i);
            int percent = (int)((i+1)*100f/runnableList.size());
            cb.run();
            cbProgress.Run(name+":"+percent+"%");
        }
    }
    private void Generate()
    {
        androidDir.deleteDirectory();
        androidDir.mkdirs();
        List<String> validFiles = Arrays.asList("config","gameAssets");

        for (FileHandle file : rootDir.list())
        {
            if (file.nameWithoutExtension().equals("texture")){
                file.copyTo(androidDir);
                continue;
            }
            if (file.nameWithoutExtension().equals("atlas0"))
            {
                FileHandle dir = androidDir.child("atlas");
                dir.mkdirs();

                for (FileHandle f : file.list())
                {
                    Runnable cb = ()-> GDXTool.GenerateAtlas(f.path(),dir.path());
                    process.add(cb);
                }
                continue;
            }
            if (file.isDirectory()) Generate(file.nameWithoutExtension());
            if (validFiles.contains(file.nameWithoutExtension())) file.copyTo(androidDir);
        }
    }
    private void Generate(String packName)
    {
        FileHandle dir = androidDir.child(packName);
        dir.deleteDirectory();
        dir.mkdirs();
        Util.For(Arrays.asList("data","font","sound","music","spine","texture","shader"),
                name->CopyTo(packName,name,dir));
        MiniJsonObject(Asset.i.GetAssetPackage(packName),dir);
        GenerateAtlas(packName,dir);
        GenerateParticle(packName,dir);
    }
    private void MiniJsonObject(AssetPackage pack,FileHandle dir){
        for (AssetNode n : pack.GetNodes(AssetNode.Kind.Object)){
            JsonValue js = Json.StringToJson(GDX.GetString(n.url));
            Json.mini.Mini(js);
            dir.child("object").child(n.name+"."+n.extension)
                    .writeString(js.toJson(JsonWriter.OutputType.minimal),false);
        }
    }
    private void CopyTo(String packName,String name,FileHandle dir)
    {
        FileHandle dir0 = rootDir.child(packName).child(name);
        if (dir0.exists()) process.add(()->dir0.copyTo(dir));
    }
    private void GenerateAtlas(String packName,FileHandle dir)
    {
        FileHandle dir0 = rootDir.child(packName).child("atlas0");
        if (!dir0.exists()) return;
        Runnable cb = ()->GDXTool.GenerateAtlas(dir0.path(),dir.path()+"/atlas");
        process.add(cb);
    }
    private void GenerateParticle(String packName,FileHandle dir)
    {
        FileHandle dir0 = rootDir.child(packName).child("particle");
        if (!dir0.exists()) return;
        if (GDX.GetFiles(dir0,"png").size()<=5) return;
        Runnable cb = ()->GDXTool.GenerateAtlas(dir0.path(),dir.path()+"/particle");
        process.add(cb);
        for (FileHandle file : GDX.GetFiles(dir0,"p"))
            process.add(()->file.copyTo(dir.child("particle")));
    }
    private void Tiny(String pack)
    {
        FileHandle dir = pack.equals("all")?androidDir:androidDir.child(pack);
        List<FileHandle> files = new ArrayList<>(GDX.GetFiles(dir,"png"));
        files.addAll(GDX.GetFiles(dir,"jpg"));
        for (FileHandle file : files)
            tinyProcess.add(()->GDXTool.Tiny(file));
    }
    private void Encode(String pack)
    {
        FileHandle dir = pack.equals("all")?androidDir:androidDir.child(pack);
        List<FileHandle> files = new ArrayList<>(GDX.GetFiles(dir,"png"));
        files.addAll(GDX.GetFiles(dir,"jpg"));
        for (FileHandle file : files)
            encodeProcess.add(()->GDXTool.Encode(file));
    }

    public List<String> GetPacks()
    {
        List<String> list = new ArrayList<>();
        list.add("all");
        for (FileHandle dir : rootDir.list())
            if (dir.isDirectory()) list.add(dir.nameWithoutExtension());
        return list;
    }
    private void WritePackages() {
        AssetData data = new AssetData();
        data.LoadPackages(androidDir);
        FileHandle assetFile = androidDir.child("gameAssets.txt");
        JsonValue jsData = Json.ToJson(data);//Json.mini.Mini(jsData);
        String stData = jsData.toJson(JsonWriter.OutputType.minimal);
        assetFile.writeString(stData.replace(androidDir.path()+"/",""),false);
    }
    private void JsonTranslate(){
        AssetNode node = Asset.i.GetNode("translate");
        if (node==null) return;
        JsonValue js = GDXTool.CSVToJson("translate");
        String path = androidDir.path()+"/"+node.url;
        new FileHandle(path).delete();
        path = path.replace("csv","json");
        new FileHandle(path).writeString(js.toJson(JsonWriter.OutputType.minimal),false);
    }
}
