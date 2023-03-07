package GDX11.IObject.IAction;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.Reflect;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction {
    public String name = "action";
    protected GDX.Func<IActor> getIActor;

    public IAction(){}
    public IAction(String name){
        this.name = name;
    }
    public void SetIActor(IActor iActor)
    {
        getIActor = ()->iActor;
    }
    public IActor GetIActor()
    {
        return getIActor.Run();
    }

    public abstract void Run();
    public abstract Action Get();
    @Override
    public boolean equals(Object obj) {
        return Reflect.equals(this,obj);
    }
}
