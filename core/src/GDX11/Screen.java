package GDX11;

import GDX11.IObject.IActor.IFind;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import com.badlogic.gdx.scenes.scene2d.Group;

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

        iGroup.iParam.Set("startShow",(Runnable)()->TryRun(onShow));
        iGroup.iParam.Set("startHide",(Runnable)()->TryRun(onHide));
        iGroup.iParam.Set("showDone",(Runnable)()->TryRun(onShowDone));
        iGroup.iParam.Set("hideDone",(Runnable)()->TryRun(onHideDone));
        iGroup.iParam.Set("show",(Runnable)this::Show);
        iGroup.iParam.Set("hide",(Runnable)this::Hide);
    }
    private void TryRun(GDX.Runnable event)
    {
        if (event!=null) event.Run();
    }
    public void Show()
    {
        Scene.i.ui.addActor(this);
        iGroup.Run("show");
        screens.add(this);
    }
    public void Hide()
    {
        iGroup.Run("hide");
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
    public IGroup GetIGroup() {
        return iGroup;
    }
}
