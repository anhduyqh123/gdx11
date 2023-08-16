package GDX11;

import GDX11.IObject.IActor.IFind;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class Screen extends Group implements IFind {
    private static final List<Screen> screens = new ArrayList<>();
    public IGroup iGroup;
    public String name;

    public Screen(String name)
    {
        this.name = name;
        this.iGroup = Asset.i.GetObject(name).Clone();
        iGroup.SetActor(this);
        iGroup.SetIRoot(null);
        iGroup.Refresh();

        iGroup.iParam.SetRun("show", this::Show);
        iGroup.iParam.SetRun("hide", this::Hide);
    }
    private void TryRun(Runnable event)
    {
        if (event!=null) event.run();
    }
    public void Show()
    {
        iGroup.Run("preShow");
        screens.add(this);
    }
    public void Hide()
    {
        iGroup.Run("preHide");
        screens.remove(this);
    }
    public boolean IsLatest()
    {
        return this.equals(GetLatest());
    }
    //Event
    public void Click(String name, Runnable onClick)
    {
        iGroup.FindIActor(name).Click(onClick);
    }
    @Override
    public IGroup GetIGroup() {
        return iGroup;
    }
    public void Run(Runnable cb,float delay){
        iGroup.Run(cb,delay);
    }
    //Event
    public void SetStartShowEvent(Runnable cb){
        iGroup.iParam.SetRun("startShow",cb);
    }
    public void SetStartHideEvent(Runnable cb){
        iGroup.iParam.SetRun("startHide",cb);
    }
    public void SetEndShowEvent(Runnable cb){
        iGroup.iParam.SetRun("endShow",cb);
    }
    public void SetEndHideEvent(Runnable cb){
        iGroup.iParam.SetRun("endHide",cb);
    }
    //static
    public static Screen GetLatest()
    {
        return screens.get(screens.size()-1);
    }
}
