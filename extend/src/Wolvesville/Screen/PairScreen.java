package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;

import java.util.List;

public class PairScreen extends Screen {
    private List<String> all;
    public PairScreen(List<String> all) {
        super("Pair");
        this.all = all;
        SetDropdown(FindIGroup("dropDown1"));
        SetDropdown(FindIGroup("dropDown2"));
        AddClick("btNext",this::Hide);
    }
    private void SetDropdown(IGroup iGroup){
        IDropDown iDropDown = iGroup.iComponents.GetIComponent("dropDown");
        //iDropDown.onSelect = onSelect;
        iDropDown.SetItems(all);
        iDropDown.SetSelected(all.get(0));
    }
}
