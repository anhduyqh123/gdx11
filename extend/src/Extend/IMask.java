package Extend;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IShader;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMask extends IShader {
    private transient List<Actor> maskedActors = new ArrayList<>();
    private transient List<Actor> actors = new ArrayList<>();
    private transient Map<Actor, ShaderProgram> map = new HashMap<>();
    private transient Actor maskActor;
    public IMask()
    {
        fragName = "smask2";
    }
    @Override
    public void Refresh() {
        super.Refresh();
        IGroup iGroup = GetIActor();
        if (!iGroup.iMap.Has("mask")) return;
        IImage mask = iGroup.FindIActor("mask");
        GDX.PostRunnable(()->{
            Asset.i.GetTexture(mask.texture).getTexture().bind(1);
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
            Util.For(maskedActors,a-> map.put(a,NewShader()));
        });
        maskActor = mask.GetActor();

        int index = iGroup.iMap.list.indexOf(mask);
        iGroup.ForIChild(a->{
            int i = iGroup.iMap.list.indexOf(a);
            if (i<index) maskedActors.add(a.GetActor());
            else actors.add(a.GetActor());
        });
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (shader==null || maskActor==null){
            superDraw.run();
            return;
        }
        batch.setShader(shader);
        shader.bind();
        DefaultUniform();
        UpdateUniform();

        Util.For(maskedActors,a->{
            shader = map.get(a);
            batch.setShader(shader);
            shader.bind();
            DefaultUniform();
            UpdateUniform();

            Vector2 pos = new Vector2();
            maskActor.localToActorCoordinates(a,pos);
            shader.setUniformf("resolution", new Vector2(a.getWidth(),a.getHeight()));
            shader.setUniformf("v4_mask", pos.x,pos.y,maskActor.getWidth(),maskActor.getHeight());
            if (a.isVisible()) a.draw(batch, parentAlpha);
            batch.setShader(null);
        });
        batch.setShader(null);
        Util.For(actors,a->{
            if (a.isVisible()) a.draw(batch, parentAlpha);
        });
    }

    @Override
    protected void DefaultUniform() {
        shader.setUniformi("i_mask", 1);
    }
}
