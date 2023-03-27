package Blackjack.Screen;

import Extend.PagedScroll.IPagedScroll;
import Extend.PagedScroll.PagedScroll;
import Extend.XItem;
import GDX11.Asset;
import GDX11.GDX;
import GDX11.Screen;

public class ThemeScreen extends Screen {
    private int index;
    public ThemeScreen(Runnable reset,Screen parent) {
        super("Theme");
        IPagedScroll iPaged = FindIActor("page");
        iPaged.ScrollToChild("gr"+ XItem.Get("theme").value);
        onHideDone = ()-> Asset.i.ForceLoadPackages(reset::run,"theme"+index);

        AddClick("btGo",()->{
            index = iPaged.GetActor().current.getZIndex();
            XItem.Get("theme").SetValue(index);
            Hide();
            parent.Hide();
        });
    }
}
