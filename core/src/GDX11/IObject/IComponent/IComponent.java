package GDX11.IObject.IComponent;

import GDX11.IObject.IObject;
import GDX11.Reflect;
import com.badlogic.gdx.graphics.g2d.Batch;

public class IComponent extends IObject {

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
}
