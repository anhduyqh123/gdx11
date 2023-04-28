package Tool.ObjectToolV2.Core;

import Extend.Spine.SpineAsset;
import GDX11.Asset;
import GDX11.AssetData.AssetData;
import GDX11.GDX;
import GDX11.GDXGame;
import GDX11.Scene;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MyGame extends GDXGame {
    public static Color bg = Color.BLACK;
    private final static Actor border = new Actor();

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
        Event.InitDrag();
        InitBorder();
    }
    private void InitBorder()
    {
        border.setDebug(true);
        border.setSize(scene.width,scene.height);
        border.setTouchable(Touchable.disabled);
        scene.GetStage().addActor(border);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        border.setSize(scene.width,scene.height);
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

    @Override
    protected Asset NewAssets() {
        return new SpineAsset();
    }

    protected void InitShowInfoEvent()
    {
        GDX.Ref<Long> ref = new GDX.Ref<>();
        scene.GetStage().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (x>100 || y>100) return false;
                ref.Set(System.currentTimeMillis());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                long left = System.currentTimeMillis() - ref.Get();
                if (left>1000) ShowFPS();
            }
        });
    }
}
