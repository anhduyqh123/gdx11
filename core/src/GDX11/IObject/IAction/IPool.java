package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IPool extends IMulAction{
    public enum State{
        Init,
        Obtain
    }
    public State state = State.Init;
    public String childName = "";
    public int fillSize = 1;

    @Override
    public void Run() {
        switch (state){
            case Init:
                GetIGroup().NewPool(childName,fillSize);
                break;
            case Obtain:
                Obtain();
                break;
            default:
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
    private void Obtain()
    {
        IActor iClone = GetIGroup().Obtain(childName);
        iMap.For(i->i.SetIActor(iClone));
        iClone.GetActor().addAction(super.Get());//only DoAction
    }
    private IGroup GetIGroup()
    {
        return GetIActor();
    }
}
