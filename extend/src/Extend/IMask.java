package Extend;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IComponent.IShader;
import GDX11.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class IMask extends IShader {
    private final transient List<Actor> maskedActors = new ArrayList<>();
    private final transient List<Actor> actors = new ArrayList<>();
    private transient IImage mask;

    public IMask()
    {
        fragName = "txtMask";
    }

    @Override
    protected void Init() {
        InitMaskGroup();
        ShaderProgram.pedantic = false;
        Util.Bind(mask.texture,1);
        Util.For(maskedActors, this::InitMaskActor);
        shader = NewShader();
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
    private void InitMaskActor(Actor actor)
    {
        if (actor instanceof Group)
        {
            for (Actor a : ((Group) actor).getChildren())
                InitMaskActor(a);
            return;
        }
        IActor iActor = IActor.GetIActor(actor);
        iActor.iComponents.Add("draw",new IComponent(){
            @Override
            public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
                if (mask==null || !IMask.this.active) superDraw.run();
                else
                    DrawMaskActor(actor,batch,parentAlpha,superDraw);
            }
        });
    }

    @Override
    protected void DefaultUniform() {
        TextureRegion trMask = Asset.i.GetTexture(mask.texture);
        shader.setUniformf("region_mask",trMask.getU(),trMask.getV(),trMask.getU2(),trMask.getV2());
        shader.setUniformi("u_mask", 1);
    }
    private void DrawMaskActor(Actor a,Batch batch,float parentAlpha,Runnable superDraw)
    {
        batch.setShader(shader);
        shader.bind();

        DefaultUniform();
        UpdateUniform();

        Vector2 pos = new Vector2();
        Actor maskActor = mask.GetActor();
        maskActor.localToActorCoordinates(a,pos);
        shader.setUniformf("resolution", new Vector2(a.getWidth(),a.getHeight()));
        shader.setUniformf("bound_mask", pos.x,pos.y,maskActor.getWidth(),maskActor.getHeight());
        IImage iImage = IActor.GetIActor(a);
        TextureRegion tr = Asset.i.GetTexture(iImage.texture);
        Color color = a.getColor();
        shader.setUniformf("actor_color",color.r,color.g,color.b,color.a*parentAlpha);
        shader.setUniformf("region_txt",tr.getU(),tr.getV(),tr.getU2(),tr.getV2());

        superDraw.run();
        batch.setShader(null);
    }
}
