package GDX11.IObject.IAction;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IPos;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IMove extends IDelay{
    public IInterpolation iInter = IInterpolation.fade;
    public IPos iPos = new IPos();

    public IMove()
    {
        name = "move";
    }

    @Override
    public void Run() {
        iPos.SetIActor(GetIActor());
        Vector2 pos = iPos.GetPosition();
        GetIActor().SetPosition(pos,iPos.GetAlign());
    }

    @Override
    public Action Get() {
        iPos.SetIActor(GetIActor());
        Vector2 pos = iPos.GetPosition();
        return Actions.moveToAligned(pos.x,pos.y, iPos.GetAlign(),GetDuration(),iInter.value);
    }

}
