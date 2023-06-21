package GDX11.IObject.IAction;

import GDX11.Asset;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IImage;
import GDX11.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class XAction extends IAction{
    public enum Type{
        Runnable,
        DoAction,
        Bind
    }
    public Type type = Type.Runnable;
    public String xName = "";

    @Override
    public void Run() {
        switch (type){
            case Runnable: RunEvent();
            break;
            case DoAction: GetIActor().RunAction(xName);
                break;
            case Bind: Bind();
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
    private void RunEvent()
    {
        if (Config.i.Has(xName)){
            Config.i.Get(xName, GDX.Runnable1.class).Run(GetIActor());
        }
        else {
            IActor iActor = GetIActor();
            if (iActor.GetIRoot().iParam.Has(xName)) iActor = iActor.GetIRoot();
            if (iActor.iParam.Has(xName)) iActor.iParam.GetRun(xName).run();
        }
    }
    private void Bind()
    {
        int unit = 0;
        TextureRegion texture = null;
        if (xName.contains(":"))
        {
            String[] arr = xName.split(":");
            texture = GetIActor().iParam.Has(arr[0])?GetIActor().iParam.Get(arr[0]): Asset.i.GetTexture(arr[0]);
            unit = Integer.parseInt(arr[1]);
        }
        else {
            unit = Integer.parseInt(xName);
            IImage iImage = GetIActor();
            texture = iImage.GetTexture();
        }
        Util.Bind(texture.getTexture(),unit);
    }
}
