package GDX11.IObject.IAction;

import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ICompAction extends IAction{
    public enum Type{
        Active,
        UnActive
    }
    public String comName = "";
    public Type type = Type.Active;
    @Override
    public void Run() {
        IComponent comp = GetIActor().iComponents.GetIComponent(comName);
        switch (type){
            case Active:
                comp.active = true;
                break;
            case UnActive:
                comp.active = false;
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
