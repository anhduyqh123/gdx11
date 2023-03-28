package GDX11.IObject;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

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
    public void Dispose() {}
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }

    //Event
    private GDX.Func<Map> onChange;
    private void Install()
    {
        Map map = new HashMap();
        onChange = ()->map;
    }
    private Map<String,Runnable> GetEventMap()
    {
        if (onChange==null) Install();
        return onChange.Run();
    }
    public void AddChangeEvent(String name,Runnable cb)
    {
        GetEventMap().put(name,cb);
    }
    public void OnChange()
    {
        Util.For(GetEventMap().values(), Runnable::run);
    }
}
