package GDX11.IObject.IAction;


import GDX11.Actions.CountAction;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ICountAction extends IDelay {
    public IInterpolation iInter = IInterpolation.linear;
    public float start,end;
    public String param = "count";

    public ICountAction()
    {
        name = "count";
    }

    @Override
    public void Run() {
        GetIActor().iParam.Set(param,end);
    }

    @Override
    public Action Get() {
        return CountAction.Get(f->GetIActor().iParam.Set(param,f),start,end,GetDuration(),iInter.value);
    }

    public void Set(float start,float end)
    {
        this.start = start;
        this.end = end;
    }
}
