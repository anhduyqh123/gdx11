package GDX11.IObject.IAction;

import GDX11.IObject.IObject;
import com.badlogic.gdx.scenes.scene2d.Action;

public abstract class IAction extends IObject {
    public IAction(){}
    public IAction(String name){
        super(name);
    }

    public abstract void Run();
    public abstract Action Get();

    protected float GetFloatValue(String stValue)
    {
        return GetIActor().GetParam(stValue,0f);
    }
}
