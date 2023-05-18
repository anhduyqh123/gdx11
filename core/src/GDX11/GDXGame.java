package GDX11;

import GDX11.AssetData.AssetData;
import GDX11.IObject.IActor.ILabel;
import GDX11.IObject.IActor.ITextField;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Collection;

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
        new GDX();
        Config.Init();
        audio = new GAudio();
        scene = NewScene();
        asset = NewAssets();
        Scene.i.GetStage().addActor(asset);
        InitShowInfoEvent();
    }
    protected void InitShowInfoEvent()
    {
        GDX.Ref<Long> ref = new GDX.Ref<>();
        scene.GetStage().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Config.Set("button",button);
                if (x>100 || y>100) return false;
                ref.Set(System.currentTimeMillis());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                long left = System.currentTimeMillis() - ref.Get();
                if (left>3000) ShowInfo();
            }
        });
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
        asset.Dispose();
    }

    protected void LoadAssetData() //need to Override
    {
        asset.SetData(GetGameData());
        asset.LoadPackages(()-> FirstLoad(), GetFirstPacks().toArray(new String[0]));//load first package
    }
    protected Collection<String> GetFirstPacks() {
        return asset.data.GetKeys();
    }
    protected void FirstLoad()
    {

    }
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        GDX.WriteToFile(path, Json.ToJson(data).toJson(JsonWriter.OutputType.minimal));
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

    protected AssetData GetGameData()
    {
        String path = "gameAssets.txt";
        try {
            AssetData data = Json.ToObject(GDX.GetString(path));
//            AssetData data = GDX.IsDesktop()?LoadPackages(path):
//                    Json.ToObject(GDX.GetString(path));
            if (data!=null) return data;
        }catch (Exception e){e.printStackTrace(); }
        return new AssetData();
    }
    //Info
    protected void ShowInfo()
    {
        ShowFPS();
        ITextField.NewTextField(Config.Get("installationID"),0,0, Align.bottomLeft,Scene.i.ui2);
    }
    protected void ShowFPS()
    {
        Label lbFPS = ILabel.NewLabel("60FPS",0,Scene.i.height, Align.topLeft,Scene.i.ui2);
        lbFPS.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                lbFPS.setText((int)GDX.GetFPS()+"FPS");
                return false;
            }
        });
    }
}
