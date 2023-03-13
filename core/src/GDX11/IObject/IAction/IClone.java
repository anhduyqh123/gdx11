package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IClone extends IMulAction{

    public String childName = "";
    public int poolSize = 1;

    public IClone()
    {
        name = "clone";
        type = Type.Parallel;
    }

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
        GetIGroup().NewPool(childName,poolSize);
    }

    @Override
    public void Run() {
        NewClone();
        super.Run();
    }
    @Override
    public Action Get() {
        return Actions.run(()->{
            NewClone().GetActor().addAction(super.Get());
        });
    }
    private IActor NewClone()
    {
        IActor iClone = GetIGroup().Obtain(childName);
        iMap.For(i->i.SetIActor(iClone));
        return iClone;
    }
    private IGroup GetIGroup()
    {
        return GetIActor();
    }
}
