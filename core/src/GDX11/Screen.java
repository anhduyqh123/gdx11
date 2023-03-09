package GDX11;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

public class Screen extends Group {
    private static final List<Screen> screens = new ArrayList<>();

    public GDX.Runnable onShow,onHide,onShowDone,onHideDone;
    protected IGroup iGroup;
    protected String name;

    public Screen(String name)
    {
        this.name = name;
        this.iGroup = IObject.Get(name).Clone();
        iGroup.SetActor(this);
        iGroup.SetIRoot(Scene.i.ui);

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
    //static
    public static Screen GetLatest()
    {
        return screens.get(screens.size()-1);
    }
}
