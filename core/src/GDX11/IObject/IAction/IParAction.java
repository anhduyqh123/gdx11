package GDX11.IObject.IAction;

import GDX11.Actors.Particle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParAction extends IAction{
    @Override
    public void Run() {
        Particle par = GetActor();
        par.Start();
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
