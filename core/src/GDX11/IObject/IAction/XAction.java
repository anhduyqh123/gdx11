package GDX11.IObject.IAction;

import GDX11.IObject.IRunnable;
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
        else {
            if (IRunnable.GameHasRun(name)) IRunnable.GetGameRun(name).Run(GetIActor());
            if (GetIActor().iRun.HasRun(name)) GetIActor().iRun.GetRun(name).Run();
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
