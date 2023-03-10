package GDX11;

import GDX11.AssetData.AssetData;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GDXGame extends ApplicationAdapter {
    protected Scene scene;
    protected Asset asset;
    protected GAudio audio;

    @Override
    public void pause() {
        if (audio!=null)
            audio.PauseMusic();
    }

    @Override
    public void resume() {
        if (audio!=null)
            audio.ResumeMusic();
    }
    @Override
    public void create() {
        Init();
        LoadAssetData();
    }
    protected void Init()
    {
        audio = new GAudio();
        scene = NewScene();
        asset = NewAssets();
        Scene.i.GetStage().addActor(asset);
    }

    @Override
    public void resize(int width, int height) {
        scene.Resize(width, height);
    }

    @Override
    public void render() {
        scene.Act(GDX.DeltaTime());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.Render();
    }

    @Override
    public void dispose() {
        scene.Dispose();
    }

    public void LoadAssetData() //need to Override
    {
        asset.SetData(GetGameData(true));
        asset.LoadPackages(()->{
            //done loading
        },"first");//load first package
    }
    protected AssetData LoadPackages(String path)
    {
        AssetData data = new AssetData();
        data.LoadPackage("first","first/");

        //GDX.WriteToFile(path, Json.ToJsonData(data));
        return data;
    }
    protected Scene NewScene()
    {
        return NewScene(new SpriteBatch());
    }
    protected Scene NewScene(Batch batch)
    {
        return new Scene(Config.Get("game_width",720),Config.Get("game_height",1280),batch);
    }
    protected Asset NewAssets()
    {
        return new Asset();
    }

    protected AssetData GetGameData(boolean makeNew)
    {
        try {
            AssetData data = makeNew?LoadPackages(GetPathData()):
                    Json.ToObject(GDX.GetString(GetPathData()));
            if (data!=null) return data;
        }catch (Exception e){e.printStackTrace(); }
        return new AssetData();
    }
    protected String GetPathData()
    {
        return "gameAssets.txt";
    }
}
