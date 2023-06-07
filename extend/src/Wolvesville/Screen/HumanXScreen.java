package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global1;
import Wolvesville.Model.Card;
import Wolvesville.Model.HumanX;

import java.util.List;

public class HumanXScreen extends Screen implements Global1 {
    public HumanXScreen(Card fc) {
        super("Baove");
        FindILabel("lb").SetText(fc.name);
        FindILabel("lb1").SetText(fc.wantTo);

        IDropDown dropDown = FindIGroup("dropDown").iComponents.GetIComponent("dropDown");
        IDropDown dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");

        SetMainDropdown(fc,dropDown);

        HumanX humanX = (HumanX)fc;
        dropDown1.onSelect = vl->{
            ShowFunc(vl);
            humanX.SetTarget(vl);
        };
        List<String> list = humanX.GetValidList(alive);
        dropDown1.SetItems(list);
        dropDown1.SetSelected(list.get(0));

        AddClick("btNext",()->{
            leftName.remove(fc.player);
            Hide();
        });
    }
    protected void ShowFunc(String name){

    }
}
