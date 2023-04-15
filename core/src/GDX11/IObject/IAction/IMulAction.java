package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IMap;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

public class IMulAction extends IAction {
    public enum Type{
        Sequence,
        Parallel
    }
    public Type type = Type.Sequence;
    public IMap<IAction> iMap = new IMap<>();
    {
        iMap.onAdd = i->i.SetIActor(GetIActor());
    }

    public IMulAction(){
        super("multi");
    }
    //IAction
    @Override
    public void Run() {
        iMap.For(IAction::Run);
    }

    @Override
    public Action Get() {
        ParallelAction mulAction = NewAction();
        iMap.For(i->mulAction.addAction(i.Get()));
        return mulAction;
    }
    protected ParallelAction NewAction()
    {
        if (type==Type.Sequence) return Actions.sequence();
        return Actions.parallel();
    }
    //IMul

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
        iMap.For(i->i.SetIActor(iActor));
    }

    public IMulAction FindIMul(String name)
    {
        return Find(name);
    }
    public <T extends IAction> T Find(String name)
    {
        return (T)iMap.Find(name);
    }
    public <T extends IAction> T Find(String name,Class<T> type)
    {
        return Find(name);
    }
    public boolean Contain(String name)
    {
        return iMap.Has(name);
    }
}
