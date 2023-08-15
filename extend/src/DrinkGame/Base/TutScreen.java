package DrinkGame.Base;

import Extend.PagedScroll.IPagedScroll;
import GDX11.Screen;

public class TutScreen extends Screen {
    protected int index=1,max;
    public TutScreen() {
        this(2);
    }
    public TutScreen(int max) {
        super("Tut");
        Click("btLeft",this::Left);
        Click("btRight",this::Right);
        this.max = max;
        Refresh();
        IPagedScroll iPage = FindIActor("page");
        iPage.GetActor().AddScrollEvent(a->{
            index = a.getZIndex()+1;
            Refresh();
        });
    }
    protected void Refresh()
    {
        FindActor("btLeft").setVisible(index>1);
        FindActor("btRight").setVisible(index<max);
    }
    private void Scroll(){
        IPagedScroll iPage = FindIActor("page");
        iPage.ScrollToChild("tut"+index);
        Refresh();
    }
    private void Left()
    {
        index--;
        if (index<1) index = 1;
        Scroll();
    }
    private void Right()
    {
        index++;
        if (index>max) index = max;
        Scroll();
    }
}
