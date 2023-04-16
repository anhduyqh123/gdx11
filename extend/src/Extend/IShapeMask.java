package Extend;

import Extend.IShape.IShape;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IComponent;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class IShapeMask extends IComponent {
    private final transient List<Actor> maskedActors = new ArrayList<>();
    private final transient List<Actor> actors = new ArrayList<>();
    private transient IImage mask;

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
    public void Refresh() {
        GDX.PostRunnable(()->GDX.Try(this::InitMaskGroup));
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (mask==null){
            superDraw.run();
            return;
        }
        DrawShapeMode(batch, parentAlpha);
    }

    private void DrawChildren(List<Actor> list,Batch batch,float parentAlpha)
    {
        batch.begin();//attention
        Util.For(list, a->{
            if (a.isVisible()) a.draw(batch, parentAlpha);
        });
        batch.end();
    }

    private void DrawShapeMode(Batch batch, float parentAlpha)
    {
        batch.end();
        DrawShapeMask(batch,parentAlpha);
        batch.begin();
    }
    private void DrawShapeMask(Batch batch, float parentAlpha)
    {
        /* Clear our depth buffer info from previous frame. */
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        /* Enable depth writing. */
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        /* Disable RGBA color writing. */
        Gdx.gl.glColorMask(false, false, false, false);
        /* Render mask elements. */
        IShape shape = mask.iComponents.GetIComponent("shape");
        shape.Draw(batch,parentAlpha);

        /* Enable RGBA color writing. */
        Gdx.gl.glColorMask(true, true, true, true);

        /* Set the depth function to LESS. */
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

        DrawChildren(maskedActors,batch,parentAlpha);

        /* Disable depth writing. */
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        DrawChildren(actors,batch,parentAlpha);
    }
}
