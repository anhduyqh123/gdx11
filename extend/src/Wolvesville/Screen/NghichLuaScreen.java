package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;

import java.util.ArrayList;
import java.util.List;

public class NghichLuaScreen extends Screen implements Global {
    public NghichLuaScreen(Runnable next) {
        super("KiemGi");
        iGroup.RunAction("nghichlua");
        IDropDown dropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        List<String> list = new ArrayList<>();
        for (Card soi : wolves)
            if (soi.Alive()) list.add(soi.player);
        dropDown.SetItems(list);
        dropDown.SetSelected(list.get(0));

        Click("btNext",()->{
            willDead.add(dropDown.selected);
            events.add("Nghịch lửa, sói bên trái là "+dropDown.selected+" chết!");
            Hide();
            next.run();
        });
    }
}
