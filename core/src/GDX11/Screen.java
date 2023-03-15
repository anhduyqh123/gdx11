package GDX11;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IFind;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

public class Screen extends Group implements IFind {
    private static final List<Screen> screens = new ArrayList<>();

    public GDX.Runnable onShow,onHide,onShowDone,onHideDone;
    public IGroup iGroup;
    public String name;

    public Screen(String name)
    {
        this.name = name;
        this.iGroup = IObject.Get(name).Clone();
        iGroup.SetActor(this);
        iGroup.SetIRoot(null);
        iGroup.Refresh();

        iGroup.iRun.SetRun("startShow",()->TryRun(onShow));
        iGroup.iRun.SetRun("startHide",()->TryRun(onHide));
        iGroup.iRun.SetRun("showDone",()->TryRun(onShowDone));
        iGroup.iRun.SetRun("hideDone",()->TryRun(onHideDone));
    }
    private void TryRun(GDX.Runnable event)
    {
        if (event!=null) event.Run();
    }
    public void Show()
    {
        Scene.i.ui.addActor(this);
        iGroup.RunAction("show");
        setTouchable(Touchable.enabled);
        screens.add(this);
    }
    public void Hide()
    {
        iGroup.RunAction("hide");
        setTouchable(Touchable.disabled);
        screens.remove(this);
    }
    public boolean IsLatest()
    {
        return this.equals(GetLatest());
    }
    //Event
    public void AddClick(String name,Runnable onClick)
    {
        iGroup.FindIActor(name).AddClick(onClick);
    }
    //static
    public static Screen GetLatest()
    {
        return screens.get(screens.size()-1);
    }

    @Override
    public <T extends IActor> T FindIActor(String name) {
        return iGroup.FindIActor(name);
    }
}
