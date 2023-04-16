package Extend;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IShader;
import GDX11.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMask extends IShader {
    private final transient List<Actor> maskedActors = new ArrayList<>();
    private final transient List<Actor> actors = new ArrayList<>();
    private final transient Map<Actor, ShaderProgram> map = new HashMap<>();
    private transient IImage mask;

    public IMask()
    {
        fragName = "txtMask";
    }

    @Override
    protected void Init() {
        InitMaskGroup();
        InitTextureMode();
    }
    protected void InitMaskGroup()
    {
        maskedActors.clear();
        actors.clear();
        IGroup iGroup = GetIActor();
        if (!iGroup.iMap.Has("mask")) return;
        mask = iGroup.FindIActor("mask");

        int index = iGroup.iMap.list.indexOf(mask);
        iGroup.ForIChild(a->{
            int i = iGroup.iMap.list.indexOf(a);
            if (i<index) maskedActors.add(a.GetActor());
            else actors.add(a.GetActor());
        });
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (mask==null){
            superDraw.run();
            return;
        }
        DrawTextureMode(batch,parentAlpha,superDraw);
    }

    @Override
    protected void DefaultUniform() {
        shader.setUniformi("i_mask", 1);
    }

    private void DrawChildren(List<Actor> list,Batch batch,float parentAlpha)
    {
        Util.For(list,a->{
            if (a.isVisible()) a.draw(batch, parentAlpha);
        });
    }

    private void InitTextureMode()
    {
        ShaderProgram.pedantic = false;
        Util.Bind(mask.texture,1);
        Util.For(maskedActors,a-> map.put(a,NewShader()));
    }
    private void DrawTextureMode(Batch batch, float parentAlpha, Runnable superDraw)
    {
        Util.For(maskedActors,a->{
            shader = map.get(a);
            batch.setShader(shader);
            shader.bind();
            DefaultUniform();
            UpdateUniform();

            Vector2 pos = new Vector2();
            Actor maskActor = mask.GetActor();
            maskActor.localToActorCoordinates(a,pos);
            shader.setUniformf("resolution", new Vector2(a.getWidth(),a.getHeight()));
            shader.setUniformf("v4_mask", pos.x,pos.y,maskActor.getWidth(),maskActor.getHeight());

            if (a.isVisible()) a.draw(batch, parentAlpha);
            batch.setShader(null);
        });
        DrawChildren(actors,batch,parentAlpha);
    }
}
