package GDX11.IObject.IAction;

import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;

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
        Log,
        Screenshot,
        TextureBuffer
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
            case Log:
                GDX.Log(name);
                break;
            case Screenshot:
                GetIActor().iParam.Set(name,ScreenUtils.getFrameBufferTexture());
                break;
            case TextureBuffer:
                GetIActor().iParam.Set(name, Util.GetTextureRegion(GetActor()));
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
