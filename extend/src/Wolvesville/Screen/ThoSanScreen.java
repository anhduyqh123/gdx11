package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global;

import java.util.ArrayList;
import java.util.List;

public class ThoSanScreen extends Screen implements Global {
    public ThoSanScreen(Runnable next) {
        super("KiemGi");
        iGroup.RunAction("thosan");
        IDropDown dropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>(alive);
        list.remove(thosan.player);
        dropDown.SetItems(list);
        dropDown.SetSelected(list.get(0));

        Click("btNext",()->{
            willDead.add(dropDown.selected);
            events.add("Thợ sắn đã bắn "+dropDown.selected);
            Hide();
            next.run();
        });
    }
}
