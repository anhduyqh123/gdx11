package GDX11.IObject.IActor;

import GDX11.GDX;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class IScrollPane extends IGroup {
    public boolean overscrollX = true,overscrollY = true;
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
    }
    private void RefreshChild()
    {
        ScrollPane scroll = GetActor();
        scroll.setOverscroll(overscrollX,overscrollY);
        if (iMap.Size()<=0) return;
        IActor iActor = iMap.Get(0);
        iActor.Refresh();
    }
    public void ScrollTo(String childName,boolean immediate)
    {
        ScrollTo(FindActor(childName),immediate);
    }
    public void ScrollTo(Actor child,boolean immediate)
    {
        ScrollPane scroll = GetActor();
        scroll.layout();
        Vector2 pos = child.localToActorCoordinates(scroll.getActor(),new Vector2());
        scroll.scrollTo(pos.x,pos.y,child.getWidth(),child.getHeight(),true,true);
        if (immediate) scroll.updateVisualScroll();
    }
    public void SetPercentX(float percent,boolean immediate){
        ScrollPane scroll = GetActor();
        scroll.layout();
        scroll.setScrollPercentX(percent);
        if (immediate) scroll.updateVisualScroll();
    }
    public void SetPercentY(float percent,boolean immediate){
        ScrollPane scroll = GetActor();
        scroll.layout();
        scroll.setScrollPercentY(percent);
        if (immediate) scroll.updateVisualScroll();
    }
}
