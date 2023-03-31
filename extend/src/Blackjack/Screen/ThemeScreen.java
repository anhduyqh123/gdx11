package Blackjack.Screen;

import Blackjack.Controller.GConfig;
import Extend.PagedScroll.IPagedScroll;
import Extend.XItem;
import GDX11.Asset;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ILabel;
import GDX11.Screen;
import GDX11.Util;

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

        Util.For(0,5,i->{
            boolean unlock = GConfig.IsUnlockTheme(i);
            IGroup gr = FindIGroup("gr"+i);
            ILabel lbx = gr.FindILabel("lb0x");
            lbx.SetText(ToSortMoney(GConfig.GetMinCoin(i)));
            gr.FindIGroup("lock").FindIActor("lb").iParam.Set("lv",GConfig.GetUnlockLevel(i));
            if (unlock) gr.Run("unlock");
        });
        iPaged.GetActor().AddScrollEvent(ia->{
            index = ia.getZIndex();
            //FindActor("btGo").setVisible(GConfig.IsUnlockTheme(index));
        });
    }
    private static String ToSortMoney(int value)
    {
        String st = value+"";
        if (st.length()<=3) return st;
        int len = st.length()%3;
        if (len==0) len = 3;
        String head = st.substring(0,len);
        if (st.length()<=6) return head+"K";
        if (st.length()<=9) return head+"M";
        return head+"B";
    }
}
