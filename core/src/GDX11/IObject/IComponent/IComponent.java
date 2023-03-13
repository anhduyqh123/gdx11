package GDX11.IObject.IComponent;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IObject;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IComponent extends IObject {
    protected GDX.Func<IActor> getIActor;

    public IComponent(){}
    public IComponent(String name){
        super(name);
    }

    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public <T extends IActor> T GetIActor()
    {
        return (T)getIActor.Run();
    }
    public Actor GetActor()
    {
        return GetIActor().GetActor();
    }

    public void Update(float delta)
    {

    }
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw)
    {
        onDraw.run();
    }

    public void Refresh()
    {

    }
    public void Remove()
    {

    }

    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
