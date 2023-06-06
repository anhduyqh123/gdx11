package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.GDX;
import GDX11.Screen;

import java.util.List;

public class AskPlayerScreen extends Screen {
    public AskPlayerScreen(String name, List<String> list,GDX.Runnable1<String> onSelect) {
        super("AskPlayer");
        FindILabel("lb").SetText(name);
        IDropDown iDropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        iDropDown.onSelect = onSelect;
        iDropDown.SetItems(list);
        iDropDown.SetSelected(list.get(0));

        AddClick("btNext", this::Hide);
    }
}
