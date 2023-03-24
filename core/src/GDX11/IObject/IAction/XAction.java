package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IEvent;
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
            if (IEvent.GameHasRun(name)) IEvent.GetGameRun(name).Run(GetIActor());
            else {
                IActor iActor = GetIActor();
                if (iActor.GetIRoot().iEvent.HasRun(name)) iActor = iActor.GetIRoot();
                if (iActor.iEvent.HasRun(name)) iActor.iEvent.GetRun(name).Run();
            }
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
