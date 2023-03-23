package Tool.ObjectTool.Data;

import GDX11.*;
import GDX11.AssetData.AssetData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.utils.JsonWriter;

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
        scene.Act(GDX.DeltaTime());
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.Render();
    }
    public void LoadAssetData() //need to Override
    {
        AssetData data = GetGameData(true);
        asset.SetData(data);
        asset.LoadPackages(done,data.GetKeys().toArray(new String[0]));

        Event.InitControlCamera();
        Event.DebugBorder();
        Event.InitDrag();
    }
    @Override
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        GDX.WriteToFile(path, Json.ToJson(data).toJson(JsonWriter.OutputType.minimal));
        return data;
    }

    @Override
    protected Scene NewScene() {
        return NewScene(new PolygonSpriteBatch());
    }
}
