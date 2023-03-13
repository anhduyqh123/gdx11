package GDX11.IObject.IComponent;

import GDX11.GDX;
import GDX11.IObject.IMap;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IComponents extends IComponent {

    public IMap<IComponent> iMap = new IMap<>();
    {
        iMap.onAdd = i->i.SetIActor(GetIActor());
    }

    public IComponents()
    {
        super("components");
    }
    public boolean ForWith(String name,GDX.Runnable1<IComponent> cb)
    {
        for (String key : iMap.GetMap().keySet())
            if (key.startsWith(name)) cb.Run(iMap.Get(key));
        return true;
    }
    public void For(GDX.Runnable1<IComponent> cb)
    {
        iMap.For(cb);
    }
    public void Add(IComponent cp)
    {
        iMap.GetMap().put(cp.name,cp);
    }

    @Override
    public void Update(float delta) {
        For(i->i.Update(delta));
    }
    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable onDraw)
    {
        boolean defaultDraw = true;
        for (IComponent i : iMap.list)
        {
            if (i.name.startsWith("draw")){
                i.Draw(batch, parentAlpha, onDraw);
                defaultDraw = false;
            }
        }
        if (defaultDraw) onDraw.run();
    }

    @Override
    public void Remove() {
        For(IComponent::Remove);
    }
}
