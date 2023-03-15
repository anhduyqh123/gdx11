package GDX11.IObject.IAction;

import GDX11.GDX;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ISizeAction extends IDelay{
    public enum Type{
        Rotate,
        Scale,
        Origin
    }
    public String value = "0";
    public Type type = Type.Rotate;
    public IInterpolation iInter = IInterpolation.fade;

    public ISizeAction()
    {
        name = "size";
    }

    @Override
    public void Run() {
        switch (type)
        {
            case Rotate:
                GetIActor().iSize.SetRotation(value);
                break;
            case Scale:
                GetIActor().iSize.SetScale(value);
                break;
            case Origin:
                GetIActor().iSize.SetOrigin(value);
                break;
        }
    }

    @Override
    public Action Get() {
        if (type==Type.Rotate) return Actions.rotateTo(0,GetDuration(),iInter.value);
        if (type==Type.Scale)
        {
            Vector2 scale  = GetIActor().iSize.GetScale(value);
            return Actions.scaleTo(scale.x,scale.y,GetDuration(),iInter.value);
        }
        return Actions.run(this::Run);
    }
}
