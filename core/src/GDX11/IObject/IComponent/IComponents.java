package GDX11.IObject.IComponent;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IMap;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IComponents extends IComponent {

    public IMap<IComponent> iMap = new IMap<>();
    public transient IMap<IComponent> syncMap;

    public IComponents()
    {
        super("components");
    }
    public void For(GDX.Runnable1<IComponent> cb)
    {
        syncMap.For(cb);
    }
    public void Add(IComponent cp)
    {
        syncMap.Add(cp);
    }
    public void Add(String name,IComponent cp)
    {
        cp.name = name;
        syncMap.Add(cp);
    }
    //
    public <T extends IComponent> T GetIComponent(String name)
    {
        return (T)syncMap.Get(name);
    }
    @Override
    public void SetIActor(IActor iActor) {
        syncMap = new IMap<>(iMap);
        syncMap.onAdd = i->i.SetIActor(GetIActor());
        super.SetIActor(iActor);
        For(i->i.SetIActor(iActor));
    }

    @Override
    public void Update(float delta) {
        For(i->{
            if (i.active) i.Update(delta);
        });
    }
    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw)
    {
        IComponent draw = syncMap.Get("draw");
        if (draw==null || !draw.active) superDraw.run();
        else draw.Draw(batch, parentAlpha, superDraw);
    }

    @Override
    public void Refresh() {
        For(IComponent::Refresh);
    }

    @Override
    public void Remove() {
        For(IComponent::Remove);
    }
}
