package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Global1;

import java.util.ArrayList;
import java.util.List;

public class CupidScreen extends Screen implements Global1 {
    private IDropDown dropDown,dropDown1,dropDown2;
    public CupidScreen() {
        super("Cupid");
        dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");

        dropDown.onSelect = vl->{
            cupid.SetPlayer(vl);
            map.put(vl,cupid);
            List<String> list = new ArrayList<>(leftName);
            list.remove(vl);
            SetDrop(1,dropDown1,list);
            SetDrop(2,dropDown2,list);
        };
        dropDown.SetItems(leftName);
        dropDown.SetSelected(leftName.get(0));

        AddClick("btNext",()->{
            leftName.remove(cupid.player);
            Hide();
        });
    }
    private void SetDrop(int index,IDropDown dropDown,List<String> list){
        dropDown.onSelect = vl-> cupid.SetPair(index,vl);
        dropDown.SetItems(list);
        dropDown.SetSelected(list.get(index));
    }
}
