package Extend.Spine;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.esotericsoftware.spine.*;

public class ISpine extends IActor {
    //ISpine
    public String spine = "",skin="",animation="";

    @Override
    protected Actor NewActor() {
        GSpine actor = GDX.Try(()->GSpine.NewSpine(GetData()), GSpine::new);
        actor.onUpdate = this::OnUpdate;
        actor.onDraw = this::OnDraw;
        actor.onRemove = this::OnRemove;
        return actor;
    }

    @Override
    protected void Connect() {
        super.Connect();
        SkeletonData data = GetData();
        iParam.Set("dw",(GDX.Func<Object>) data::getWidth);
        iParam.Set("dh",(GDX.Func<Object>) data::getHeight);
    }
    public void RefreshContent()
    {
        GDX.Try(()->{
            GSpine gSpine = GetActor();
            gSpine.SetSkin(skin);
            gSpine.SetAnimation(animation,true);
        });
    }
    //Spine
    public void MakeNew()//call when spine change
    {
        SetActor(NewActor());
        GSpine gSpine = GetActor();
        skin = gSpine.GetSkinNames()[0];
        animation = gSpine.GetAnimationNames()[0];
        Connect();
        Refresh();
    }
    private SkeletonData GetData()
    {
        return Asset.i.Get(spine,SkeletonData.class);
    }
}
