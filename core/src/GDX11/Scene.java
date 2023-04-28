package GDX11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.*;

public class Scene {
    public static Scene i;
    public float scaleX,scaleY,scale;
    public int width,height,width0,height0;
    private Batch batch;
    private final Map<String,Stage> mapStage = new LinkedHashMap<>();
    public Group ui=new Group(),ui2=new Group();
    public Scene(int width0, int height0, Batch batch)
    {
        i = this;
        this.width0 = width0;
        this.height0 = height0;
        this.batch = batch;
        InitStage();
        InitSize();
        Gdx.input.setInputProcessor(GetStage());
    }
    private void InitStage()
    {
        NewStage("ui");
        GetStage().addActor(ui);
        GetStage().addActor(ui2);
    }
    private void InitSize()
    {
        width = (int)GetStage().getViewport().getWorldWidth();
        height = (int)GetStage().getViewport().getWorldHeight();
        scaleX = width*1f/ width0;
        scaleY = height*1f/ height0;
        scale = Math.max(scaleX,scaleY);
    }
    public Stage NewStage(String name)
    {
        Stage stage = new Stage(new ExtendViewport(width0,height0),batch);
        mapStage.put(name,stage);
        return stage;
    }
    public Stage GetStage(String name)
    {
        return mapStage.get(name);
    }
    public Stage GetStage()
    {
        return GetStage("ui");
    }
    //event
    public void Act(float delta)
    {
        for (Stage stage : mapStage.values())
            stage.act(delta);
    }
    public void Dispose()
    {
        for (Stage stage : mapStage.values())
            stage.dispose();
    }
    public void Render()
    {
        for (Stage stage : mapStage.values())
            stage.draw();
    }
    public void Resize(int width,int height)
    {
        for (Stage stage : mapStage.values())
        {
            stage.getViewport().setScreenSize(width,height);
            stage.getViewport().update(width,height);
            OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
            camera.setToOrtho(false,stage.getViewport().getWorldWidth(),stage.getViewport().getWorldHeight());
        }
        width0 = width;
        height0 = height;
        InitSize();
    }

    //Transform
    public static void AddActorKeepTransform(Actor actor, Group group)
    {
        Vector2 stageScale = GetStageScale(actor);
        float stageRotate = GetStageRotation(actor);

        Vector2 ori = new Vector2(actor.getOriginX(),actor.getOriginY());
        Vector2 pos = actor.localToStageCoordinates(new Vector2(ori));
        group.stageToLocalCoordinates(pos).sub(ori);
        actor.setPosition(pos.x,pos.y);
        group.addActor(actor);

        Vector2 scale = StageToLocalScale(stageScale,actor);
        actor.setScale(scale.x,scale.y);
        actor.setRotation(StageToLocalRotation(stageRotate,actor));
    }
    private static List<Actor> GetConnect(Actor actor)
    {
        List<Actor> list = new ArrayList<>();
        while (actor!=null) {
            list.add(actor);
            actor = actor.getParent();
        }
        return list;
    }
    public static float StageToLocalRotation(float rotation, Actor actor)
    {
        return rotation - GetStageRotation(actor.getParent());
    }
    public static float GetStageRotation(Actor actor)
    {
        GDX.Ref<Float> ref = new GDX.Ref<>(0f);
        Util.For(GetConnect(actor),a->ref.Set(ref.Get()+a.getRotation()));
        return ref.Get();
    }
    public static float SetStageRotation(Actor actor,float rotation)
    {
        Actor parent = actor.getParent();
        while (parent!=null) {
            rotation -= parent.getRotation();
            parent = parent.getParent();
        }
        actor.setRotation(rotation);
        return rotation;
    }
    //Scale
    public static Vector2 StageToLocalScale(Vector2 scale, Actor actor)
    {
        Vector2 sScale = GetStageScale(actor.getParent());
        return scale.scl(1f/sScale.x,1f/sScale.y);
    }
    public static Vector2 GetStageScale(Actor actor)
    {
        GDX.Ref<Vector2> ref = new GDX.Ref<>(new Vector2(1,1));
        Util.For(GetConnect(actor),a->ref.Set(ref.Get().scl(a.getScaleX(),a.getScaleY())));
        return ref.Get();
    }
    //position
    public static Vector2 GetPosition(Actor actor)
    {
        return GetPosition(actor, Align.bottomLeft);
    }
    public static Vector2 GetPosition(Actor actor, int align)
    {
        return new Vector2(actor.getX(align),actor.getY(align));
    }
    public static Vector2 GetLocal(Actor actor, int align)
    {
        return GetPosition(actor,align).sub(GetPosition(actor));
    }
    public static Vector2 GetStagePosition(Actor actor, Vector2 local)
    {
        return actor.localToStageCoordinates(new Vector2(local));
    }
    public static Vector2 GetStagePosition(Actor actor)
    {
        return GetStagePosition(actor,Align.bottomLeft);
    }
    public static Vector2 GetStagePosition(Actor actor, int align)
    {
        return GetStagePosition(actor,GetLocal(actor,align));
    }
    public static void SetPosition(Actor actor,Vector2 pos,int align)
    {
        actor.setPosition(pos.x,pos.y,align);
    }
    public static void SetPosition(Actor actor,Vector2 pos)
    {
        SetPosition(actor,pos,Align.bottomLeft);
    }
    public static void SetStagePosition(Actor actor,Vector2 pos)
    {
        SetStagePosition(actor,pos,Align.bottomLeft);
    }
    public static void SetStagePosition(Actor actor,Vector2 pos,int align)
    {
        Vector2 p = actor.getParent().stageToLocalCoordinates(new Vector2(pos));
        SetPosition(actor,p,align);
    }
}
