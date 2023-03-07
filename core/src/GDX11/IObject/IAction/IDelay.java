package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IDelay extends IAction{
    public String duration = "0";
    public IDelay()
    {
        super("delay");
    }
    @Override
    public void Run() {}

    @Override
    public Action Get() {
        return Actions.delay(GetIActor().iParam.GetValueFromString(duration));
    }
    public void SetDuration(Object value)
    {
        duration = value+"";
    }
}
