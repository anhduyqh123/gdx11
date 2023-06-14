package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import Wolvesville.Global;

public class ThoiNienListScreen extends Screen implements Global {

    public ThoiNienListScreen(){
        super("PlayerVote");
        FindITable("table").CloneChild(thoisao.list,this::InitName);
        FindILabel("lbList").SetText("Danh sách đã bị thôi miên: "+thoisao.list.size());
        //AddClick("btNext", this::Hide);
        BtNext(this,()->{});
    }
    private void InitName(String name, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(name);
        iGroup.FindActor("btVote").setVisible(false);
    }
}
