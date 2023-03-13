package GDX11.IObject.IAction;

import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class XAction extends IAction{
    public enum Type{
        Runnable,
        DoAction
    }
    public Type type = Type.DoAction;

    @Override
    public void Run() {
        GetIActor().RunAction(name);
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
