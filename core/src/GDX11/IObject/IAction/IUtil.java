package GDX11.IObject.IAction;

import GDX11.GDX;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IUtil extends IAction{
    public enum Type
    {
        Visible,
        Disable,
        EnableTouch,
        DisableTouch,
        Remove,
        ToFront,
        ToBack,
        ClearAction,
        RefreshContent,
        BaseRefresh,
        Log
    }
    public Type type = Type.Visible;

    public IUtil()
    {
        super("util");
    }
    @Override
    public void Run() {
        switch (type)
        {
            case Visible:
                GetActor().setVisible(true);
                break;
            case Disable:{
                GetActor().setVisible(false);
                break;
            }
            case EnableTouch:
                GetActor().setTouchable(Touchable.enabled);
                break;
            case DisableTouch:
                GetActor().setTouchable(Touchable.disabled);
                break;
            case Remove:
                GetActor().remove();
                break;
            case ToBack:
                GetActor().toBack();
                break;
            case ToFront:
                GetActor().toFront();
                break;
            case ClearAction:
                GetActor().clearActions();
                break;
            case RefreshContent:
                GetIActor().RefreshContent();
                break;
            case BaseRefresh:
                GetIActor().BaseRefresh();
                break;
            case Log:
                GDX.Log(name);
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
