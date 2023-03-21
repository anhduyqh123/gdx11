package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IBase {
    protected GDX.Func<IActor> getIActor;
    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public <T extends IActor> T GetIActor()
    {
        return (T)getIActor.Run();
    }
    public <T extends Actor> T GetActor()
    {
        return GetIActor().GetActor();
    }
    public <T> T Get(Class<T> type)
    {
        return (T)this;
    }
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
