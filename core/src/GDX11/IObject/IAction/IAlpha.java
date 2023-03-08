package GDX11.IObject.IAction;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IAlpha extends IDelay{
    public IInterpolation iInter = IInterpolation.fade;
    public float value;//0->1
    public IAlpha()
    {
        name = "alpha";
    }
    @Override
    public void Run() {
        GetIActor().GetActor().getColor().a = value;
    }

    @Override
    public Action Get() {
        return Actions.alpha(value,GetDuration(),iInter.value);
    }
}
