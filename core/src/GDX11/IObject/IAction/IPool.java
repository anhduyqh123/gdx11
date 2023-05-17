package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IPool extends IMulAction{
    public enum Type{
        Init,
        Obtain
    }
    public Type type = Type.Init;
    public String childName = "";
    public int fillSize = 1;

    @Override
    public void Run() {
        switch (type){
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
        iClone.GetActor().addAction(super.Get());
    }
    private IGroup GetIGroup()
    {
        return GetIActor();
    }
}
