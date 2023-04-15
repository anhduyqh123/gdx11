package Extend;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IShader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class IMask extends IShader {
    private transient List<Actor> maskedActors = new ArrayList<>();
    private transient List<Actor> actors = new ArrayList<>();
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
        UpdateValue();
        superDraw.run();

        batch.setShader(null);
    }

    @Override
    protected void UpdateValue() {
        shader.setUniformi("i_mask", 1);
        shader.setUniformf("v4_mask", maskActor.getX(),maskActor.getY(),maskActor.getWidth(),maskActor.getHeight());
        shader.setUniformf("v4_texture", 0,0,resolution.x,resolution.y);
        super.UpdateValue();
    }
}
