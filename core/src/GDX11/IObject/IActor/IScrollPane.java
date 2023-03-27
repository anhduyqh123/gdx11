package GDX11.IObject.IActor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class IScrollPane extends IGroup {
    public String scrollToChild = "";
    @Override
    protected Actor NewActor() {
        return new ScrollPane(null){
            @Override
            public void addActor(Actor actor) {
                setActor(actor);
            }
        };
    }
    @Override
    protected void Clear() {
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        RefreshChild();
        if (scrollToChild.equals("")) return;
        ScrollTo(scrollToChild);
    }
    private void RefreshChild()
    {
        if (iMap.Size()<=0) return;
        IActor iActor = iMap.Get(0);
        iActor.Refresh();
    }
    public void ScrollTo(String childName)
    {
        ScrollTo(FindActor(childName));
    }
    public void ScrollTo(Actor child)
    {
        ScrollPane scroll = GetActor();
        scroll.layout();
        scroll.scrollTo(child.getX(),child.getY()
                ,child.getWidth(),child.getHeight(),true,true);
    }
}
