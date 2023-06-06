package Wolvesville.Screen;

import Extend.IDropDown;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CupidScreen extends Screen {
    private IDropDown dropDown,dropDown1,dropDown2;
    private List<String> pair;
    public CupidScreen(String name, Map<String,String> map, List<String> pair,List<String> all) {
        super("Cupid");
        dropDown = FindIGroup("dropDown0").iComponents.GetIComponent("dropDown");
        dropDown1 = FindIGroup("dropDown1").iComponents.GetIComponent("dropDown");
        dropDown2 = FindIGroup("dropDown2").iComponents.GetIComponent("dropDown");

        this.pair = pair;
        dropDown.onSelect = vl->{
            map.put(name,vl);
            List<String> list = new ArrayList<>(all);
            list.remove(vl);
            if (pair.size()==0){
                pair.add(list.get(0));
                pair.add(list.get(1));
            }
            SetDrop(0,dropDown1,list);
            SetDrop(1,dropDown2,list);
        };
        dropDown.SetItems(all);
        dropDown.SetSelected(all.get(0));
    }
    private void SetDrop(int index,IDropDown dropDown,List<String> list){
        dropDown.onSelect = vl-> pair.set(index,vl);
        dropDown.SetItems(list);
        dropDown.SetSelected(pair.get(index));
    }
}
