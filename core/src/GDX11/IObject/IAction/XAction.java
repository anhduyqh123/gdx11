package GDX11.IObject.IAction;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class XAction extends IAction{
    public enum Type{
        Runnable,
        DoAction
    }
    public Type type = Type.Runnable;

    @Override
    public void Run() {
        if (type==Type.DoAction) GetIActor().RunAction(name);
        else RunEvent();
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
    private void RunEvent()
    {
        if (Config.Has(name)){
            Config.Get(name,GDX.Runnable1.class).Run(GetIActor());
        }
        else {
            IActor iActor = GetIActor();
            if (iActor.GetIRoot().iParam.Has(name)) iActor = iActor.GetIRoot();
            if (iActor.iParam.Has(name)) iActor.iParam.Get(name,Runnable.class).run();
        }
    }
}
