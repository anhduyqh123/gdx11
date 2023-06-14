package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import GDX11.Util;
import Wolvesville.Global;

import java.util.ArrayList;
import java.util.List;

public class KiemGiScreen extends Screen implements Global {
    public KiemGiScreen(Runnable next) {
        super("KiemGi");
        IDropDown dropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>();
        Util.For(wolves,w->{
            if (alive.contains(w.player)) list.add(w.player);
        });
        dropDown.onSelect = vl->{
            FindActor("btNext").setVisible(true);
        };
        dropDown.SetItems(list);
        dropDown.SetView("?");

        FindActor("btNext").setVisible(false);
        Click("btNext",()->{
            //willDead.add(dropDown.selected);
            nextDie.add(dropDown.selected);
            events.add(dropDown.selected+ " là sói bên trái kiếm gỉ");
            Hide();
            next.run();
        });
    }
}
