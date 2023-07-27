package GDX11.IObject.IComponent;

import GDX11.IObject.IObject;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IComponent extends IObject {
    public boolean active = true;
    public IComponent(){}
    public IComponent(String name){
        super(name);
    }

    public void Update(float delta)
    {

    }
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw)
    {
        superDraw.run();
    }

    public void Refresh()
    {

    }
    public void Remove()
    {

    }

    public <T extends IComponent> T GetIComponent(String name){
        return GetIActor().iComponents.GetIComponent(name);
    }
    protected  <T extends IComponent> T GetIComponent(String name,Class<T> type){
        return GetIComponent(name);
    }
}
