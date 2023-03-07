package GDX11.IObject.IAction;

import GDX11.GDX;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMulAction extends IAction {
    public enum Type{
        None,
        Sequence,
        Parallel
    }
    public List<IAction> list = new ArrayList<>();
    public Type type = Type.Sequence;
    protected GDX.Func<Map<String,IAction>> getMap;

    //IAction
    @Override
    public void Run() {
        Util.For(list, IAction::Run);
    }

    @Override
    public Action Get() {
        ParallelAction mulAction = NewAction();
        Util.For(list, i->mulAction.addAction(i.Get()));
        return mulAction;
    }
    protected ParallelAction NewAction()
    {
        if (type==Type.Sequence) return Actions.sequence();
        return Actions.parallel();
    }
    //IMul

    //data

    protected Map<String,IAction> GetMap()
    {
        if (getMap==null) Install();
        return getMap.Run();
    }
    protected void Install()
    {
        Map<String,IAction> map = new HashMap<>();
        Util.For(list,i->map.put(i.name,i));
        getMap = ()->map;
    }
    public <T extends IAction> T Find(String name)
    {
        if (GetMap().containsKey(name)) return (T)GetMap().get(name);
        for (IAction i : list)
            if (i instanceof IMulAction)
            {
                IMulAction iMul = (IMulAction) i;
                T iAction = iMul.Find(name);
                if (iAction!=null) return iAction;
            }
        return null;
    }
    public IMulAction FindIMul(String name)
    {
        return Find(name);
    }
}
