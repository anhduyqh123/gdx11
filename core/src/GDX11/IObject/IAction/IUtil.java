package GDX11.IObject.IAction;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        Log,
        Screenshot,
        TextureBuffer,
        Bind
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
                GetIActor().iParam.Set(name,Util.GetTextureRegion());
                break;
            case TextureBuffer:
                GetIActor().iParam.Set(name, Util.GetTextureRegion(GetActor()));
                break;
            case Bind://name->bg_1
                String[] arr = name.split(":");
                TextureRegion texture = GetIActor().iParam.Has(arr[0])?GetIActor().iParam.Get(arr[0]):Asset.i.GetTexture(arr[0]);
                texture.getTexture().bind(Integer.parseInt(arr[1]));
                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
