package Tool.ObjectTool.Data;

import GDX11.*;
import GDX11.AssetData.AssetData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Collection;
import java.util.List;

public class MyGame extends GDXGame {
    public static Color bg = Color.BLACK;
    public static MyGame i;
    private Runnable done;
    public MyGame(Runnable done)
    {
        i = this;
        this.done = done;
    }

    @Override
    public void render() {
        GdxAI.getTimepiece().update(GDX.DeltaTime());
        scene.Act(GDX.DeltaTime());
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.Render();
    }
    public void LoadAssetData()
    {
        super.LoadAssetData();
        Event.InitControlCamera();
        Event.DebugBorder();
        Event.InitDrag();
    }
    protected Collection<String> GetFirstPacks() {
        return asset.data.GetKeys();
    }

    @Override
    protected void FirstLoad() {
        done.run();
    }

    @Override
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        //GDX.WriteToFile(path, Json.ToJson(data).toJson(JsonWriter.OutputType.minimal));
        return data;
    }

    @Override
    protected Scene NewScene() {
        return NewScene(new PolygonSpriteBatch());
    }
}
