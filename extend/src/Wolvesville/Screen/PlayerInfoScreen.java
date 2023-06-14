package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import Wolvesville.Global;

public class PlayerInfoScreen extends Screen implements Global {
    public PlayerInfoScreen(){
        super("PlayerInfo");
        FindITable("table").CloneChild(allName,this::InitName);
        FindILabel("lbList").SetText("Danh sách người chơi: "+allName.size());
        Click("btClose", this::Hide);
    }
    private void InitName(String name, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(cupid.Contains(name)?name+"[YELLOW](cặp)":name);
        iGroup.FindILabel("lb1").SetText(map.get(name)!=null?map.get(name).name:"");
        iGroup.FindILabel("lb2").RunAction(alive.contains(name)?"alive":"die");
    }
}
