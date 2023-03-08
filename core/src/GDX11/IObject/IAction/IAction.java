package GDX11.IObject.IAction;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IObject;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction extends IObject {
    protected GDX.Func<IActor> getIActor;

    public IAction(){}
    public IAction(String name){
        super(name);
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

    protected float GetFloatValue(String stValue)
    {
        return GetIActor().GetParam(stValue);
    }
}
