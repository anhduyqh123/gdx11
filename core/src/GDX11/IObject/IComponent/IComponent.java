package GDX11.IObject.IComponent;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IComponent {
    protected GDX.Func<IActor> getIActor;

    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public IActor GetIActor()
    {
        return getIActor.Run();
    }
    public Actor GetActor()
    {
        return GetIActor().GetActor();
    }

    public void OnUpdate(float delta)
    {

    }
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw)
    {
        onDraw.run();
    }

    public void OnRefresh()
    {

    }
    public void OnRemove()
    {

    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
