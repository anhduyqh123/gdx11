package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.Screen;
import Wolvesville.Global1;

import java.util.ArrayList;
import java.util.List;

public class ThoiSaoScreen extends Screen implements Global1 {
    private IDropDown dropDown,dropDown1,dropDown2;
    public ThoiSaoScreen() {
        super("ThoiSao");
        dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");

        dropDown.onSelect = vl->{
            thoisao.SetPlayer(vl);
            map.put(vl,thoisao);
            List<String> list = new ArrayList<>(leftName);
            list.remove(vl);
            SetDrop(1,dropDown1,list);
            SetDrop(2,dropDown2,list);
        };
        dropDown.SetItems(leftName);
        dropDown.SetSelected(leftName.get(0));

        AddClick("btNext",()->{
            thoisao.Thoi();
            leftName.remove(thoisao.player);
            Hide();
        });
    }
    private void SetDrop(int index,IDropDown dropDown,List<String> list){
        dropDown.onSelect = vl-> thoisao.SetPair(index,vl);
        dropDown.SetItems(list);
        dropDown.SetSelected(list.get(index));
    }
}
