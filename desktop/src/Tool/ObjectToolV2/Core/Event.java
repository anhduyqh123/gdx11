package Tool.ObjectToolV2.Core;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IPos;
import GDX11.Reflect;
import GDX11.Scene;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;
import java.util.Map;

public class Event {
    public static Map<String, GDX.Runnable1<Integer>> keyEvent = new HashMap<>();
    public static void AddKeyEvent(String name,GDX.Runnable1<Integer> cb)
    {
        keyEvent.put(name,cb);
    }
    public static void InitControlCamera()
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
                Vector2 dir = p.sub(pos0);
                float len = dir.len()*camera.zoom*1.5f;
                dir.setLength(len);
                Vector2 cPos = new Vector2(camPos).add(-dir.x,dir.y);
                camera.position.set(cPos,0);
            }
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ESCAPE) backRun.run();
                Util.For(keyEvent.values(),cb->cb.Run(event.getKeyCode()));
                return true;
            }
        });
    }
    public static IActor dragIActor;
    private static Runnable backRun = ()->{};
    public static void InitDrag()
    {
        Vector2 p0 = new Vector2();
        Vector2 p = new Vector2();
        GDX.Ref<Boolean> debug = new GDX.Ref<>();
        Scene.i.ui.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (dragIActor ==null || button!=0) return false;
                debug.Set(dragIActor.GetActor().getDebug());
                dragIActor.GetActor().setDebug(true);
                IActor iActor = dragIActor;
                p0.set(x,y);
                p.set(iActor.iPos.GetPosition());
                IPos iPos = Reflect.Clone(iActor.iPos);
                iPos.SetIActor(iActor);
                backRun = ()->{
                    iActor.iPos = iPos;
                    iPos.Refresh();
                };
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragIActor ==null) return;
                Vector2 pos = new Vector2(x,y).sub(p0).add(p);
                Util.Int(pos);
                IActor iActor = dragIActor;
                iActor.iPos.SetPosition(pos);
                Reflect.OnChange(iActor.iPos);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dragIActor ==null) return;
                dragIActor.GetActor().setDebug(debug.Get());
                dragIActor = null;
            }
        });
    }
    public static void ResetCamera()
    {
        OrthographicCamera camera  = (OrthographicCamera) Scene.i.GetStage().getCamera();
        camera.zoom = 1;
        camera.position.set(Scene.i.width/2,Scene.i.height/2,0);
    }
}
