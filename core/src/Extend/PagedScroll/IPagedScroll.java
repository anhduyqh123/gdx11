package Extend.PagedScroll;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IParam;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IPagedScroll extends IGroup {
    public boolean horizontal = true;
    public float flingTime = 0.1f;
    public float spaceX,spaceY;
    public String scrollToChild = "";
    public String align = "center";
    public boolean scaleChild;

    @Override
    protected Actor NewActor() {
        return new PagedScroll();
    }

    @Override
    public void Refresh() {
        InitActor();
        BaseRefresh();
        SetPage();
    }
    private void SetPage()
    {
        GetActor().setFlingTime(flingTime);
        GetActor().scaleChildDist = scaleChild;
        GetActor().spaceX = spaceX;
        GetActor().spaceY = spaceY;
        ForIChild(IActor::Refresh);
        Pad();
        if (iMap.Size()<=0) return;
        GetActor().layout();
        String scrollChild = scrollToChild.equals("")?iMap.Get(0).name:scrollToChild;
        ScrollToChild(scrollChild);
    }
    private void Pad()
    {
        if (iMap.Size()<=0) return;
        if (horizontal){
            float pad = GetActor().getWidth()/2-iMap.Get(0).GetActor().getWidth()/2;
            GetActor().content.padLeft(pad).padRight(pad);
            GetActor().content.align(IParam.GetAlign(align));
        }
    }
    public PagedScroll GetActor()
    {
        return super.GetActor();
    }
    public void ScrollToChild(String name)
    {
        GetActor().ScrollTo(FindActor(name));
    }
}
