package GDX11.IObject.IAction;

import GDX11.Actors.Particle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IParAction extends IAction{
    public enum State{
        Play,
        Reset,
        Stop
    }
    public State state = State.Play;
    @Override
    public void Run() {
        Particle par = GetActor();
        switch (state)
        {
            case Play:
                par.Play();
                break;
            case Reset:
                par.Reset();
                break;
            case Stop:
                par.Stop();
                break;
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
