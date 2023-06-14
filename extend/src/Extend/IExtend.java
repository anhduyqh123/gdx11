package Extend;

import GDX11.Asset;
import GDX11.IObject.IAction.IAction;
import GDX11.IObject.IActor.IImage;
import GDX11.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IExtend extends IAction {
    public enum Type{
        Screenshot,
        TextureBuffer,
        Bind
    }
    public Type type = Type.Screenshot;
    @Override
    public void Run() {
        switch (type)
        {
            case Screenshot:
                GetIActor().iParam.Set(name, Util.GetScreenshot());
                break;
            case TextureBuffer:
                GetIActor().iParam.Set(name, Util.GetTextureRegion(GetActor()));
                break;
            case Bind://name->bg:1,1->image bind
                Bind();
                break;
            default:
        }
    }
    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
    private void Bind()
    {
        int unit = 0;
        TextureRegion texture = null;
        if (name.contains(":"))
        {
            String[] arr = name.split(":");
            texture = GetIActor().iParam.Has(arr[0])?GetIActor().iParam.Get(arr[0]):Asset.i.GetTexture(arr[0]);
            unit = Integer.parseInt(arr[1]);
        }
        else {
            unit = Integer.parseInt(name);
            IImage iImage = GetIActor();
            texture = iImage.GetTexture();
        }
        Util.Bind(texture.getTexture(),unit);
    }
}
