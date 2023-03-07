package Tool.ObjectTool.Data;

import GDX11.AssetData.AssetData;
import GDX11.GDX;
import GDX11.GDXGame;
import GDX11.Scene;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MyGame extends GDXGame {
    public static Color bg = Color.BLACK;
    public static MyGame i;
    public int width,height;
    public Runnable done;
    public MyGame(int width,int height,Runnable done)
    {
        i = this;
        this.width = width;
        this.height = height;
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

        InitControlCamera();
        DebugBorder();
    }
    @Override
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        return data;
    }

    @Override
    protected Scene NewScene() {
        return new Scene(width,height,new PolygonSpriteBatch());
    }

    private void DebugBorder()
    {
        Actor actor = new Actor();
        actor.setSize(Scene.i.width,Scene.i.height);
        actor.setTouchable(Touchable.disabled);
        actor.setDebug(true);
        Scene.i.GetStage().addActor(actor);
    }
    private void InitControlCamera()
    {
        OrthographicCamera camera  = (OrthographicCamera) Scene.i.GetStage().getCamera();
        Vector2 pos0 = new Vector2();
        Vector2 camPos = new Vector2();
        Scene.i.GetStage().addListener(new ClickListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                float delta = amountY* GDX.DeltaTime();
                camera.zoom+=delta;
                return super.scrolled(event, x, y, amountX, amountY);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button!=1) return false;
                pos0.set(Gdx.input.getX(),Gdx.input.getY());
                camPos.set(camera.position.x,camera.position.y);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Vector2 p = new Vector2(Gdx.input.getX(),Gdx.input.getY());
                Vector2 dir = Util.GetDirect(p,pos0);
                float len = dir.len()*camera.zoom*1.5f;
                dir.setLength(len);
                Vector2 cPos = new Vector2(camPos).add(dir.x,-dir.y);
                camera.position.set(cPos,0);
            }
        });
    }
}
