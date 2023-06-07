package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global1;
import Wolvesville.Model.Card;

public class AskPlayerScreen extends Screen implements Global1 {
    public AskPlayerScreen(Card fc) {
        super("AskPlayer");
        FindILabel("lb").SetText(fc.name);
        IDropDown iDropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        SetMainDropdown(fc,iDropDown);

        AddClick("btNext",()->{
            leftName.remove(fc.player);
            Hide();
        });
    }
}
