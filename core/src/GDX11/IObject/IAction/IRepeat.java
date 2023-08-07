package GDX11.IObject.IAction;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IRepeat extends IMulAction{
    public int value = -1;//-1->forever
    public IRepeat()
    {
        name = "repeat";
    }
    @Override
    public void Run() {
        if (iMap.Size()>0)
            iMap.Get(0).Run();
    }

    @Override
    public Action Get() {
        if (iMap.Size()<=0) return Actions.sequence();
        return Actions.repeat(value,super.Get());
        //return Actions.repeat(value,iMap.Get(0).Get());
    }
}
